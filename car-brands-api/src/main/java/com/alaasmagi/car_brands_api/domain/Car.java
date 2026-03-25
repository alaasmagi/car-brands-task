package com.alaasmagi.car_brands_api.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class Car {
    private UUID id;
    private String name;
    private UUID parentId;
}
