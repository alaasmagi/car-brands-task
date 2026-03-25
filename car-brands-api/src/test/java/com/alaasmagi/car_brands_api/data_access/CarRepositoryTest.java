package com.alaasmagi.car_brands_api.data_access;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
import com.alaasmagi.car_brands_api.domain.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:sqlite:build/car-repository-test.db",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarJpaRepository carJpaRepository;

    @BeforeEach
    void setUp() {
        carJpaRepository.deleteAll();
    }

    @Test
    void saveAndFindByIdPersistCar() {
        Car car = new Car();
        car.setName("BMW");

        Car saved = carRepository.save(car);
        Optional<Car> found = carRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("BMW", found.get().getName());
    }

    @Test
    void findAllReturnsSavedCars() {
        carRepository.save(createCar("Audi"));
        carRepository.save(createCar("Mercedes-Benz"));

        List<Car> cars = carRepository.findAll();

        assertEquals(2, cars.size());
    }

    @Test
    void updateChangesExistingCar() {
        Car saved = carRepository.save(createCar("Audi"));
        saved.setName("Audi Updated");

        Car updated = carRepository.update(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Audi Updated", updated.getName());
        assertEquals("Audi Updated", carRepository.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    void updateMissingCarThrows() {
        Car missing = createCar("Missing");
        missing.setId(UUID.randomUUID());

        assertThrows(JpaObjectRetrievalFailureException.class, () -> carRepository.update(missing));
    }

    @Test
    void deleteRemovesExistingCar() {
        Car saved = carRepository.save(createCar("BMW"));

        carRepository.delete(saved.getId());

        assertFalse(carRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void deleteMissingCarThrows() {
        UUID missingId = UUID.randomUUID();

        assertThrows(JpaObjectRetrievalFailureException.class, () -> carRepository.delete(missingId));
    }

    private Car createCar(String name) {
        Car car = new Car();
        car.setName(name);
        return car;
    }
}
