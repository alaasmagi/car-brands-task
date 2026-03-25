package com.alaasmagi.car_brands_api.data_access;

import com.alaasmagi.car_brands_api.contract.data_access.CarJpaRepository;
import com.alaasmagi.car_brands_api.contract.data_access.ICarRepository;
import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import com.alaasmagi.car_brands_api.dto.data_access.mapper.CarEntityMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CarRepository implements ICarRepository {

    private final CarJpaRepository carJpaRepository;
    private final CarEntityMapper carEntityMapper;

    public CarRepository(CarJpaRepository carJpaRepository, CarEntityMapper carEntityMapper) {
        this.carJpaRepository = carJpaRepository;
        this.carEntityMapper = carEntityMapper;
    }

    @Override
    public List<Car> findAll() {
        return carJpaRepository.findAll().stream()
                .map(carEntityMapper::Map)
                .toList();
    }

    @Override
    public Optional<Car> findById(UUID id) {
        return carJpaRepository.findById(id)
                .map(carEntityMapper::Map);
    }

    @Override
    public Car save(Car car) {
        CarEntity entity = carEntityMapper.Map(car);
        return carEntityMapper.Map(carJpaRepository.save(entity));
    }

    @Override
    public Car update(Car car) {
        if (!carJpaRepository.existsById(car.getId())) {
            throw new EntityNotFoundException("Car not found: " + car.getId());
        }
        CarEntity entity = carEntityMapper.Map(car);
        return carEntityMapper.Map(carJpaRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!carJpaRepository.existsById(id)) {
            throw new EntityNotFoundException("Car not found: " + id);
        }
        carJpaRepository.deleteById(id);
    }
}

