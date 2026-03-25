package com.alaasmagi.car_brands_api.dto.web.mapper;

import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.dto.web.dto.CarDto;
import com.alaasmagi.car_brands_api.dto.web.dto.CarRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CarDtoMapper {

    public Car Map(CarRequestDto dto) {
        Car car = new Car();
        car.setName(dto.getName());
        car.setParentId(dto.getParentId());
        return car;
    }

    public CarDto Map(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setName(car.getName());
        dto.setParentId(car.getParentId());
        return dto;
    }
}