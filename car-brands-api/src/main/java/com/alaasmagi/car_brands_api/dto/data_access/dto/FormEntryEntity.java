package com.alaasmagi.car_brands_api.dto.data_access.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_entries")
@Getter
@Setter
public class FormEntryEntity extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String contactPhone;

    @Column(nullable = false)
    private boolean validDrivingLicense;

    @ManyToMany
    @JoinTable(
            name = "car_form_entry",
            joinColumns = @JoinColumn(name = "form_entry_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    private List<CarEntity> selectedCars = new ArrayList<>();
}