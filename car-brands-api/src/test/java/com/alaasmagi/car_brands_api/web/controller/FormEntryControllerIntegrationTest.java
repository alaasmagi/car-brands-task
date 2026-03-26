package com.alaasmagi.car_brands_api.web.controller;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryJpaRepository;
import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:sqlite:build/form-entry-controller-test.db",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FormEntryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IFormEntryJpaRepository formEntryJpaRepository;

    @Autowired
    private CarJpaRepository carJpaRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        formEntryJpaRepository.deleteAll();
        carJpaRepository.deleteAll();
    }

    @Test
    void createFormEntryReturnsCreatedAndExpandedSelectedCars() throws Exception {
        CarEntity car = new CarEntity();
        car.setName("BMW");
        CarEntity savedCar = carJpaRepository.save(car);

        HttpResponse<String> response = sendRequest("POST", "/api/form-entries", """
                {
                  "fullName": "Mati Maasikas",
                  "contactPhone": "+3725555555",
                  "validDrivingLicense": true,
                  "selectedCarIds": ["%s"]
                }
                """.formatted(savedCar.getId()));

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("\"fullName\":\"Mati Maasikas\""));
        assertTrue(response.body().contains("\"contactPhone\":\"+3725555555\""));
        assertTrue(response.body().contains("\"validDrivingLicense\":true"));
        assertTrue(response.body().contains("\"selectedCars\":["));
        assertTrue(response.body().contains("\"id\":\"" + savedCar.getId() + "\""));
        assertTrue(response.body().contains("\"name\":\"BMW\""));
    }

    @Test
    void invalidFormEntryRequestReturnsBadRequestWithApiErrorResponse() throws Exception {
        HttpResponse<String> response = sendRequest("POST", "/api/form-entries", """
                {
                  "fullName": "",
                  "contactPhone": "",
                  "selectedCarIds": []
                }
                """);

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"status\":400"));
        assertTrue(response.body().contains("\"error\":\"Bad Request\""));
        assertTrue(response.body().contains("\"message\":\"Validation failed\""));
        assertTrue(response.body().contains("fullName:"));
        assertTrue(response.body().contains("contactPhone:"));
        assertTrue(response.body().contains("validDrivingLicense:"));
        assertTrue(response.body().contains("selectedCarIds:"));
    }

    @Test
    void getMissingFormEntryReturnsNotFound() throws Exception {
        HttpResponse<String> response = sendRequest("GET", "/api/form-entries/" + UUID.randomUUID(), null);

        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteMissingFormEntryReturnsNotFoundWithApiErrorResponse() throws Exception {
        HttpResponse<String> response = sendRequest("DELETE", "/api/form-entries/" + UUID.randomUUID(), null);

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"status\":404"));
        assertTrue(response.body().contains("\"error\":\"Not Found\""));
        assertTrue(response.body().contains("FormEntry not found"));
    }

    @Test
    void getFormEntryByIdReturnsSavedFormEntryWithExpandedCars() throws Exception {
        CarEntity car = new CarEntity();
        car.setName("Audi");
        CarEntity savedCar = carJpaRepository.save(car);

        var entity = new com.alaasmagi.car_brands_api.dto.data_access.dto.FormEntryEntity();
        entity.setFullName("Mari Maasikas");
        entity.setContactPhone("+3725000000");
        entity.setValidDrivingLicense(true);
        entity.setSelectedCars(List.of(savedCar));
        var savedEntry = formEntryJpaRepository.save(entity);

        HttpResponse<String> response = sendRequest("GET", "/api/form-entries/" + savedEntry.getId(), null);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + savedEntry.getId() + "\""));
        assertTrue(response.body().contains("\"fullName\":\"Mari Maasikas\""));
        assertTrue(response.body().contains("\"name\":\"Audi\""));
    }

    @Test
    void updateFormEntryReturnsUpdatedFormEntry() throws Exception {
        CarEntity initialCar = new CarEntity();
        initialCar.setName("BMW");
        CarEntity savedInitialCar = carJpaRepository.save(initialCar);

        CarEntity updatedCar = new CarEntity();
        updatedCar.setName("Mercedes-Benz");
        CarEntity savedUpdatedCar = carJpaRepository.save(updatedCar);

        var entity = new com.alaasmagi.car_brands_api.dto.data_access.dto.FormEntryEntity();
        entity.setFullName("Mari Maasikas");
        entity.setContactPhone("+3725000000");
        entity.setValidDrivingLicense(true);
        entity.setSelectedCars(List.of(savedInitialCar));
        var savedEntry = formEntryJpaRepository.save(entity);

        HttpResponse<String> response = sendRequest("PUT", "/api/form-entries/" + savedEntry.getId(), """
                {
                  "fullName": "Mari Tamm",
                  "contactPhone": "+3725111111",
                  "validDrivingLicense": true,
                  "selectedCarIds": ["%s"]
                }
                """.formatted(savedUpdatedCar.getId()));

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + savedEntry.getId() + "\""));
        assertTrue(response.body().contains("\"fullName\":\"Mari Tamm\""));
        assertTrue(response.body().contains("\"contactPhone\":\"+3725111111\""));
        assertTrue(response.body().contains("\"name\":\"Mercedes-Benz\""));
    }

    @Test
    void deleteExistingFormEntryReturnsNoContent() throws Exception {
        CarEntity car = new CarEntity();
        car.setName("BMW");
        CarEntity savedCar = carJpaRepository.save(car);

        var entity = new com.alaasmagi.car_brands_api.dto.data_access.dto.FormEntryEntity();
        entity.setFullName("Mati Maasikas");
        entity.setContactPhone("+3725555555");
        entity.setValidDrivingLicense(true);
        entity.setSelectedCars(List.of(savedCar));
        var savedEntry = formEntryJpaRepository.save(entity);

        HttpResponse<String> response = sendRequest("DELETE", "/api/form-entries/" + savedEntry.getId(), null);

        assertEquals(204, response.statusCode());
        assertTrue(formEntryJpaRepository.findById(savedEntry.getId()).isEmpty());
    }

    @Test
    void createFormEntryWithMissingSelectedCarReturnsNotFound() throws Exception {
        HttpResponse<String> response = sendRequest("POST", "/api/form-entries", """
                {
                  "fullName": "Mati Maasikas",
                  "contactPhone": "+3725555555",
                  "validDrivingLicense": true,
                  "selectedCarIds": ["%s"]
                }
                """.formatted(UUID.randomUUID()));

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"status\":404"));
        assertTrue(response.body().contains("Car not found"));
    }

    private HttpResponse<String> sendRequest(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json");

        if (body == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
        }

        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
}
