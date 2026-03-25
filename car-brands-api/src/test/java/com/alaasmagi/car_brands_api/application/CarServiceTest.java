package com.alaasmagi.car_brands_api.application;

import com.alaasmagi.car_brands_api.contract.data_access.ICarRepository;
import com.alaasmagi.car_brands_api.domain.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private ICarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void getAllCarsReturnsRepositoryResult() {
        List<Car> cars = List.of(createCar(UUID.randomUUID(), "BMW"), createCar(UUID.randomUUID(), "Audi"));
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertSame(cars, result);
        verify(carRepository).findAll();
    }

    @Test
    void getCarByIdReturnsRepositoryResult() {
        UUID id = UUID.randomUUID();
        Optional<Car> expected = Optional.of(createCar(id, "Mercedes-Benz"));
        when(carRepository.findById(id)).thenReturn(expected);

        Optional<Car> result = carService.getCarById(id);

        assertSame(expected, result);
        verify(carRepository).findById(id);
    }

    @Test
    void createCarDelegatesToRepositorySave() {
        Car car = createCar(null, "BMW");
        Car savedCar = createCar(UUID.randomUUID(), "BMW");
        when(carRepository.save(car)).thenReturn(savedCar);

        Car result = carService.createCar(car);

        assertSame(savedCar, result);
        verify(carRepository).save(car);
    }

    @Test
    void updateCarSetsIdBeforeDelegatingToRepository() {
        UUID id = UUID.randomUUID();
        Car car = createCar(null, "Audi");
        Car updatedCar = createCar(id, "Audi");
        when(carRepository.update(car)).thenReturn(updatedCar);

        Car result = carService.updateCar(id, car);

        assertEquals(id, car.getId());
        assertSame(updatedCar, result);
        verify(carRepository).update(car);
    }

    @Test
    void deleteCarDelegatesToRepositoryDelete() {
        UUID id = UUID.randomUUID();

        carService.deleteCar(id);

        verify(carRepository).delete(id);
    }

    private Car createCar(UUID id, String name) {
        Car car = new Car();
        car.setId(id);
        car.setName(name);
        return car;
    }
}
