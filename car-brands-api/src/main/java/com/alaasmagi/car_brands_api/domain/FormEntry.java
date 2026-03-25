package com.alaasmagi.car_brands_api.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FormEntry {
    private UUID id;
    private String fullName;
    private String contactPhone;
    private boolean validDrivingLicense;
    private List<UUID> selectedCarIds;
}
