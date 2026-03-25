package com.alaasmagi.car_brands_api.dto.data_access.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "cars")
@Getter
@Setter
public class CarEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private UUID parentId;
}
