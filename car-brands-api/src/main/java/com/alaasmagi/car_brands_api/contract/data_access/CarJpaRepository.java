package com.alaasmagi.car_brands_api.contract.data_access;

import com.alaasmagi.car_brands_api.dto.data_access.dto.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarEntity, UUID> {
}
