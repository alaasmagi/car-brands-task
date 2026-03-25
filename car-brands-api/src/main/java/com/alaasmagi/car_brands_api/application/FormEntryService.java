package com.alaasmagi.car_brands_api.application;

import com.alaasmagi.car_brands_api.contract.application.IFormEntryService;
import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryRepository;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FormEntryService implements IFormEntryService {

    private final IFormEntryRepository formEntryRepository;

    public FormEntryService(IFormEntryRepository formEntryRepository) {
        this.formEntryRepository = formEntryRepository;
    }

    @Override
    public FormEntry createFormEntry(FormEntry formEntry) {
        return formEntryRepository.save(formEntry);
    }

    @Override
    public FormEntry updateFormEntry(UUID id, FormEntry formEntry) {
        formEntry.setId(id);
        return formEntryRepository.update(formEntry);
    }

    @Override
    public Optional<FormEntry> getFormEntryById(UUID id) {
        return formEntryRepository.findById(id);
    }

    @Override
    public void deleteFormEntry(UUID id) {
        formEntryRepository.delete(id);
    }
}
