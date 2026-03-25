package com.alaasmagi.car_brands_api.dto.data_access.mapper;

import com.alaasmagi.car_brands_api.domain.FormEntry;
import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import com.alaasmagi.car_brands_api.dto.data_access.dto.FormEntryEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FormEntryEntityMapper {
    public FormEntryEntity Map(FormEntry formEntry) {
        FormEntryEntity entity = new FormEntryEntity();
        entity.setId(formEntry.getId());
        entity.setFullName(formEntry.getFullName());
        entity.setContactPhone(formEntry.getContactPhone());
        entity.setValidDrivingLicense(formEntry.isValidDrivingLicense());
        entity.setSelectedCars(new ArrayList<>(formEntry.getSelectedCarIds().stream()
                .map(id -> { CarEntity car = new CarEntity(); car.setId(id); return car; })
                .toList()));
        return entity;
    }

    public FormEntry Map(FormEntryEntity entity) {
        FormEntry formEntry = new FormEntry();
        formEntry.setId(entity.getId());
        formEntry.setFullName(entity.getFullName());
        formEntry.setContactPhone(entity.getContactPhone());
        formEntry.setValidDrivingLicense(entity.isValidDrivingLicense());
        formEntry.setSelectedCarIds(new ArrayList<>(entity.getSelectedCars().stream()
                .map(CarEntity::getId)
                .toList()));
        return formEntry;
    }
}
