package com.alaasmagi.car_brands_api.data_access;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryJpaRepository;
import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:sqlite:build/form-entry-repository-test.db",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class FormEntryRepositoryTest {

    @Autowired
    private FormEntryRepository formEntryRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private IFormEntryJpaRepository formEntryJpaRepository;

    @Autowired
    private CarJpaRepository carJpaRepository;

    @BeforeEach
    void setUp() {
        formEntryJpaRepository.deleteAll();
        carJpaRepository.deleteAll();
    }

    @Test
    void saveAndFindByIdPersistFormEntryWithSelectedCars() {
        Car selectedCar = carRepository.save(createCar("BMW"));
        FormEntry formEntry = createFormEntry(selectedCar.getId());

        FormEntry saved = formEntryRepository.save(formEntry);
        Optional<FormEntry> found = formEntryRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Mati Maasikas", found.get().getFullName());
        assertEquals(List.of(selectedCar.getId()), found.get().getSelectedCarIds());
    }

    @Test
    void updateChangesExistingFormEntry() {
        Car firstCar = carRepository.save(createCar("Audi"));
        Car secondCar = carRepository.save(createCar("BMW"));
        FormEntry saved = formEntryRepository.save(createFormEntry(firstCar.getId()));

        saved.setFullName("Kati Kask");
        saved.setSelectedCarIds(List.of(secondCar.getId()));

        FormEntry updated = formEntryRepository.update(saved);

        assertEquals("Kati Kask", updated.getFullName());
        assertEquals(List.of(secondCar.getId()), updated.getSelectedCarIds());
        assertEquals("Kati Kask", formEntryRepository.findById(saved.getId()).orElseThrow().getFullName());
    }

    @Test
    void updateMissingFormEntryThrows() {
        FormEntry missing = createFormEntry(UUID.randomUUID());
        missing.setId(UUID.randomUUID());

        assertThrows(JpaObjectRetrievalFailureException.class, () -> formEntryRepository.update(missing));
    }

    @Test
    void deleteRemovesExistingFormEntry() {
        Car selectedCar = carRepository.save(createCar("BMW"));
        FormEntry saved = formEntryRepository.save(createFormEntry(selectedCar.getId()));

        formEntryRepository.delete(saved.getId());

        assertFalse(formEntryRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void deleteMissingFormEntryThrows() {
        UUID missingId = UUID.randomUUID();

        assertThrows(JpaObjectRetrievalFailureException.class, () -> formEntryRepository.delete(missingId));
    }

    private Car createCar(String name) {
        Car car = new Car();
        car.setName(name);
        return car;
    }

    private FormEntry createFormEntry(UUID carId) {
        FormEntry formEntry = new FormEntry();
        formEntry.setFullName("Mati Maasikas");
        formEntry.setContactPhone("+3725555555");
        formEntry.setValidDrivingLicense(true);
        formEntry.setSelectedCarIds(List.of(carId));
        return formEntry;
    }
}
