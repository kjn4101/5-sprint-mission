package com.codeit.blog.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, Integer> {

    private int minAge;

    // 초기화
    @Override
    public void initialize(MinimumAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    // 검사로직
    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext constraintValidatorContext) {
        if (age == null) return true; // @NotNull은 여기서 처리하지 않음
        return age >= minAge;
    }
}
