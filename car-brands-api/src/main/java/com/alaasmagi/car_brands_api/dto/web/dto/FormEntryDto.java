package com.alaasmagi.car_brands_api.dto.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FormEntryDto {
    private UUID id;
    private String fullName;
    private String contactPhone;
    private boolean validDrivingLicense;
    private List<CarDto> selectedCars;
}