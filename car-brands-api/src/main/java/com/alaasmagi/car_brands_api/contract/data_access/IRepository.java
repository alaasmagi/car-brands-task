package com.alaasmagi.car_brands_api.contract.data_access;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T> {
    Optional<T> findById(UUID id);
    T save(T entity);
}