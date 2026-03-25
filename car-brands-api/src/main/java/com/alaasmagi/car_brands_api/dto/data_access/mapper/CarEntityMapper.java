package com.alaasmagi.car_brands_api.dto.data_access.mapper;

import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarEntityMapper {

    public CarEntity Map(Car car) {
        CarEntity entity = new CarEntity();
        entity.setId(car.getId());
        entity.setName(car.getName());
        entity.setParentId(car.getParentId());
        return entity;
    }

    public Car Map(CarEntity entity) {
        Car car = new Car();
        car.setId(entity.getId());
        car.setName(entity.getName());
        car.setParentId(entity.getParentId());
        return car;
    }
}
