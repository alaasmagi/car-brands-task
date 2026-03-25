package com.alaasmagi.car_brands_api.dto.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CarRequestDto {
    @NotBlank
    private String name;
    private UUID parentId;
}
