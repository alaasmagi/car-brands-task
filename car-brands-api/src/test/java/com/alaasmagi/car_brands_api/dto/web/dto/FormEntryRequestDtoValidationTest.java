package com.alaasmagi.car_brands_api.dto.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormEntryRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validRequestHasNoViolations() {
        FormEntryRequestDto request = new FormEntryRequestDto();
        request.setFullName("Mati Maasikas");
        request.setContactPhone("+3725555555");
        request.setValidDrivingLicense(true);
        request.setSelectedCarIds(List.of(UUID.randomUUID()));

        Set<ConstraintViolation<FormEntryRequestDto>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void missingOrEmptyRequiredFieldsProduceViolations() {
        FormEntryRequestDto request = new FormEntryRequestDto();
        request.setFullName("");
        request.setContactPhone("");
        request.setValidDrivingLicense(null);
        request.setSelectedCarIds(List.of());

        Set<ConstraintViolation<FormEntryRequestDto>> violations = validator.validate(request);

        assertEquals(4, violations.size());
        assertTrue(hasViolationFor(violations, "fullName"));
        assertTrue(hasViolationFor(violations, "contactPhone"));
        assertTrue(hasViolationFor(violations, "validDrivingLicense"));
        assertTrue(hasViolationFor(violations, "selectedCarIds"));
    }

    @Test
    void selectedCarIdsDoesNotAllowNullItems() {
        FormEntryRequestDto request = new FormEntryRequestDto();
        request.setFullName("Mati Maasikas");
        request.setContactPhone("+3725555555");
        request.setValidDrivingLicense(true);
        request.setSelectedCarIds(Arrays.asList((UUID) null));

        Set<ConstraintViolation<FormEntryRequestDto>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(hasViolationFor(violations, "selectedCarIds[0].<list element>"));
    }

    private boolean hasViolationFor(Set<ConstraintViolation<FormEntryRequestDto>> violations, String propertyPath) {
        return violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals(propertyPath));
    }
}
