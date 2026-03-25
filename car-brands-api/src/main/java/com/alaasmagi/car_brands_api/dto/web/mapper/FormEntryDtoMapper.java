package com.alaasmagi.car_brands_api.dto.web.mapper;

import com.alaasmagi.car_brands_api.domain.Car;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import com.alaasmagi.car_brands_api.dto.web.dto.FormEntryDto;
import com.alaasmagi.car_brands_api.dto.web.dto.FormEntryRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FormEntryDtoMapper {

    private final CarDtoMapper carDtoMapper;

    public FormEntryDtoMapper(CarDtoMapper carDtoMapper) {
        this.carDtoMapper = carDtoMapper;
    }

    public FormEntry Map(FormEntryRequestDto dto) {
        FormEntry formEntry = new FormEntry();
        formEntry.setFullName(dto.getFullName());
        formEntry.setContactPhone(dto.getContactPhone());
        formEntry.setValidDrivingLicense(dto.getValidDrivingLicense());
        formEntry.setSelectedCarIds(dto.getSelectedCarIds());
        return formEntry;
    }

    public FormEntryDto Map(FormEntry formEntry, List<Car> selectedCars) {
        FormEntryDto dto = new FormEntryDto();
        dto.setId(formEntry.getId());
        dto.setFullName(formEntry.getFullName());
        dto.setContactPhone(formEntry.getContactPhone());
        dto.setValidDrivingLicense(formEntry.isValidDrivingLicense());
        dto.setSelectedCars(selectedCars.stream()
                .map(carDtoMapper::Map)
                .toList());
        return dto;
    }
}
