package com.alaasmagi.car_brands_api.contract.application;

import com.alaasmagi.car_brands_api.domain.FormEntry;

import java.util.Optional;
import java.util.UUID;

public interface IFormEntryService {
    FormEntry createFormEntry(FormEntry formEntry);
    FormEntry updateFormEntry(UUID id, FormEntry formEntry);
    Optional<FormEntry> getFormEntryById(UUID id);
    void deleteFormEntry(UUID id);
}
