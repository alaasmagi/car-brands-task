package com.alaasmagi.car_brands_api.contract.data_access;

import com.alaasmagi.car_brands_api.domain.Car;

import java.util.List;
import java.util.UUID;

public interface ICarRepository extends IRepository<Car> {
    List<Car> findAll();
    Car update(Car car);
    void delete(UUID id);
}
