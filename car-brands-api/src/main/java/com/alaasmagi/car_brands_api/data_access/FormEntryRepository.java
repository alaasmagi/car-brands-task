package com.alaasmagi.car_brands_api.data_access;

import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryJpaRepository;
import com.alaasmagi.car_brands_api.contract.data_access.IFormEntryRepository;
import com.alaasmagi.car_brands_api.domain.FormEntry;
import com.alaasmagi.car_brands_api.dto.data_access.dto.FormEntryEntity;
import com.alaasmagi.car_brands_api.dto.data_access.mapper.FormEntryEntityMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FormEntryRepository implements IFormEntryRepository {

    private final IFormEntryJpaRepository formEntryJpaRepository;
    private final FormEntryEntityMapper formEntryEntityMapper;

    public FormEntryRepository(IFormEntryJpaRepository formEntryJpaRepository, FormEntryEntityMapper formEntryEntityMapper) {
        this.formEntryJpaRepository = formEntryJpaRepository;
        this.formEntryEntityMapper = formEntryEntityMapper;
    }

    @Override
    public Optional<FormEntry> findById(UUID id) {
        return formEntryJpaRepository.findById(id)
                .map(formEntryEntityMapper::Map);
    }

    @Override
    public FormEntry save(FormEntry formEntry) {
        FormEntryEntity entity = formEntryEntityMapper.Map(formEntry);
        return formEntryEntityMapper.Map(formEntryJpaRepository.save(entity));
    }

    @Override
    public FormEntry update(FormEntry formEntry) {
        if (!formEntryJpaRepository.existsById(formEntry.getId())) {
            throw new EntityNotFoundException("FormEntry not found: " + formEntry.getId());
        }
        FormEntryEntity entity = formEntryEntityMapper.Map(formEntry);
        return formEntryEntityMapper.Map(formEntryJpaRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!formEntryJpaRepository.existsById(id)) {
            throw new EntityNotFoundException("FormEntry not found: " + id);
        }
        formEntryJpaRepository.deleteById(id);
    }
}
