package com.alaasmagi.car_brands_api.web.controller;

import com.alaasmagi.car_brands_api.contract.application.ICarService;
import com.alaasmagi.car_brands_api.contract.application.IFormEntryService;
import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import com.alaasmagi.car_brands_api.dto.web.dto.FormEntryDto;
import com.alaasmagi.car_brands_api.dto.web.dto.FormEntryRequestDto;
import com.alaasmagi.car_brands_api.dto.web.mapper.FormEntryDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/form-entries")
public class FormEntryController {

    private final IFormEntryService formEntryService;
    private final ICarService carService;
    private final FormEntryDtoMapper formEntryDtoMapper;

    public FormEntryController(IFormEntryService formEntryService, ICarService carService, FormEntryDtoMapper formEntryDtoMapper) {
        this.formEntryService = formEntryService;
        this.carService = carService;
        this.formEntryDtoMapper = formEntryDtoMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormEntryDto> getFormEntryById(@PathVariable UUID id) {
        return formEntryService.getFormEntryById(id)
                .map(formEntry -> ResponseEntity.ok(formEntryDtoMapper.Map(formEntry, resolveSelectedCars(formEntry))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FormEntryDto> createFormEntry(@RequestBody @Valid FormEntryRequestDto requestDto) {
        FormEntry formEntry = formEntryDtoMapper.Map(requestDto);
        FormEntry saved = formEntryService.createFormEntry(formEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(formEntryDtoMapper.Map(saved, resolveSelectedCars(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormEntryDto> updateFormEntry(@PathVariable UUID id, @RequestBody @Valid FormEntryRequestDto requestDto) {
        FormEntry formEntry = formEntryDtoMapper.Map(requestDto);
        FormEntry updated = formEntryService.updateFormEntry(id, formEntry);
        return ResponseEntity.ok(formEntryDtoMapper.Map(updated, resolveSelectedCars(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormEntry(@PathVariable UUID id) {
        formEntryService.deleteFormEntry(id);
        return ResponseEntity.noContent().build();
    }

    private List<Car> resolveSelectedCars(FormEntry formEntry) {
        return formEntry.getSelectedCarIds().stream()
                .map(carId -> carService.getCarById(carId)
                        .orElseThrow(() -> new EntityNotFoundException("Car not found: " + carId)))
                .toList();
    }
}
