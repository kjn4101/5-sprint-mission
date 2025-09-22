package com.codeit.blog.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OneDecimalPlaceValidator implements ConstraintValidator<OneDecimalPlace, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
        if( value == null ) return true;
        String str = String.valueOf(value);
        int index = str.indexOf('.');
        if( index == -1 ) return false; // 정수 허용
        return str.length() - index - 1 == 1; // 소수점 한자리까지 허용
    }
}
