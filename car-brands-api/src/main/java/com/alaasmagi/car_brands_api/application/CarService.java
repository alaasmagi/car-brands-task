package com.alaasmagi.car_brands_api.application;

import com.alaasmagi.car_brands_api.contract.application.ICarService;
import com.alaasmagi.car_brands_api.contract.data_access.ICarRepository;
import com.alaasmagi.car_brands_api.domain.Car;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService implements ICarService {

    private final ICarRepository carRepository;

    public CarService(ICarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarById(UUID id) {
        return carRepository.findById(id);
    }

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(UUID id, Car car) {
        car.setId(id);
        return carRepository.update(car);
    }

    @Override
    public void deleteCar(UUID id) {
        carRepository.delete(id);
    }
}
