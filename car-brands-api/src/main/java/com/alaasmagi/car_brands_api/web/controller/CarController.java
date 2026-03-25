package com.alaasmagi.car_brands_api.web.controller;

import com.alaasmagi.car_brands_api.contract.application.ICarService;
import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.dto.web.dto.CarDto;
import com.alaasmagi.car_brands_api.dto.web.dto.CarRequestDto;
import com.alaasmagi.car_brands_api.dto.web.dto.FormEntryRequestDto;
import com.alaasmagi.car_brands_api.dto.web.mapper.CarDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final ICarService carService;
    private final CarDtoMapper carDtoMapper;

    public CarController(ICarService carService, CarDtoMapper carDtoMapper) {
        this.carService = carService;
        this.carDtoMapper = carDtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> cars = carService.getAllCars().stream()
                .map(carDtoMapper::Map)
                .toList();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable UUID id) {
        return carService.getCarById(id)
                .map(carDtoMapper::Map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarDto> createCar(@RequestBody @Valid CarRequestDto requestDto) {
        Car car = carDtoMapper.Map(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(carDtoMapper.Map(carService.createCar(car)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable UUID id, @RequestBody @Valid CarRequestDto requestDto) {
        Car car = carDtoMapper.Map(requestDto);
        return ResponseEntity.ok(carDtoMapper.Map(carService.updateCar(id, car)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
