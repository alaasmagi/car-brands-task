package com.alaasmagi.car_brands_api.dto.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FormEntryRequestDto {
    @NotBlank
    private String fullName;

    @NotBlank
    private String contactPhone;

    @NotNull
    private Boolean validDrivingLicense;

    @NotEmpty
    private List<@NotNull UUID> selectedCarIds;
}
