package com.alaasmagi.car_brands_api.contract.data_access;

import com.alaasmagi.car_brands_api.domain.FormEntry;

import java.util.UUID;

public interface IFormEntryRepository extends IRepository<FormEntry> {
    FormEntry update(FormEntry formEntry);
    void delete(UUID id);
}
