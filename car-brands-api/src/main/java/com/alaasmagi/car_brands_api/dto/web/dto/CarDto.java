package com.alaasmagi.car_brands_api.dto.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CarDto {
    private UUID id;
    private String name;
    private UUID parentId;
}