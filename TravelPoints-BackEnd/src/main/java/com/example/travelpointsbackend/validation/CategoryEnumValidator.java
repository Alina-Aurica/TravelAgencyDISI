package com.example.travelpointsbackend.validation;

import com.example.travelpointsbackend.model.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CategoryEnumValidator implements ConstraintValidator<ValidCategory, String> {
    @Override
    public void initialize(ValidCategory constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        try {
            Category.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
