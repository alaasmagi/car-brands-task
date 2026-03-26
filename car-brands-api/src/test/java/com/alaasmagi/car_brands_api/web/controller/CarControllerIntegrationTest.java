package com.alaasmagi.car_brands_api.web.controller;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:sqlite:build/car-controller-test.db",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CarControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CarJpaRepository carJpaRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        carJpaRepository.deleteAll();
    }

    @Test
    void createCarReturnsCreatedAndPersistsCar() throws Exception {
        HttpResponse<String> response = sendJsonRequest("POST", "/api/cars", """
                {
                  "name": "BMW"
                }
                """);

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("\"name\":\"BMW\""));
        assertEquals(1, carJpaRepository.count());
    }

    @Test
    void blankNameReturnsBadRequestWithApiErrorResponse() throws Exception {
        HttpResponse<String> response = sendJsonRequest("POST", "/api/cars", """
                {
                  "name": ""
                }
                """);

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"status\":400"));
        assertTrue(response.body().contains("\"error\":\"Bad Request\""));
        assertTrue(response.body().contains("\"message\":\"Validation failed\""));
        assertTrue(response.body().contains("name:"));
    }

    @Test
    void deleteMissingCarReturnsNotFoundWithApiErrorResponse() throws Exception {
        UUID missingId = UUID.randomUUID();

        HttpResponse<String> response = sendRequest("DELETE", "/api/cars/" + missingId, null);

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"status\":404"));
        assertTrue(response.body().contains("\"error\":\"Not Found\""));
        assertTrue(response.body().contains("Car not found"));
    }

    @Test
    void getCarByIdReturnsSavedCar() throws Exception {
        CarEntity entity = new CarEntity();
        entity.setName("Audi");
        CarEntity saved = carJpaRepository.save(entity);

        HttpResponse<String> response = sendRequest("GET", "/api/cars/" + saved.getId(), null);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + saved.getId() + "\""));
        assertTrue(response.body().contains("\"name\":\"Audi\""));
    }

    @Test
    void getAllCarsReturnsSavedCars() throws Exception {
        CarEntity bmw = new CarEntity();
        bmw.setName("BMW");
        carJpaRepository.save(bmw);

        CarEntity audi = new CarEntity();
        audi.setName("Audi");
        carJpaRepository.save(audi);

        HttpResponse<String> response = sendRequest("GET", "/api/cars", null);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"name\":\"BMW\""));
        assertTrue(response.body().contains("\"name\":\"Audi\""));
    }

    @Test
    void getMissingCarReturnsNotFound() throws Exception {
        HttpResponse<String> response = sendRequest("GET", "/api/cars/" + UUID.randomUUID(), null);

        assertEquals(404, response.statusCode());
    }

    @Test
    void updateCarReturnsUpdatedCar() throws Exception {
        CarEntity entity = new CarEntity();
        entity.setName("Audi");
        CarEntity saved = carJpaRepository.save(entity);

        HttpResponse<String> response = sendJsonRequest("PUT", "/api/cars/" + saved.getId(), """
                {
                  "name": "Audi Sport"
                }
                """);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + saved.getId() + "\""));
        assertTrue(response.body().contains("\"name\":\"Audi Sport\""));
        assertEquals("Audi Sport", carJpaRepository.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    void deleteExistingCarReturnsNoContent() throws Exception {
        CarEntity entity = new CarEntity();
        entity.setName("Volvo");
        CarEntity saved = carJpaRepository.save(entity);

        HttpResponse<String> response = sendRequest("DELETE", "/api/cars/" + saved.getId(), null);

        assertEquals(204, response.statusCode());
        assertTrue(carJpaRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void invalidCarIdReturnsBadRequestWithApiErrorResponse() throws Exception {
        HttpResponse<String> response = sendRequest("GET", "/api/cars/not-a-uuid", null);

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"status\":400"));
        assertTrue(response.body().contains("\"error\":\"Bad Request\""));
    }

    private HttpResponse<String> sendJsonRequest(String method, String path, String body) throws IOException, InterruptedException {
        return sendRequest(method, path, body);
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
