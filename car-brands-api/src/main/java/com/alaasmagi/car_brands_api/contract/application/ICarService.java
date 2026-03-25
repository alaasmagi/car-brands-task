package com.alaasmagi.car_brands_api.contract.application;

import com.alaasmagi.car_brands_api.domain.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICarService {
    List<Car> getAllCars();
    Optional<Car> getCarById(UUID id);
    Car createCar(Car car);
    Car updateCar(UUID id, Car car);
    void deleteCar(UUID id);
}