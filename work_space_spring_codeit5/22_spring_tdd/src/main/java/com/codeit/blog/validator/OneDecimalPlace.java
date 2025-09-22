package com.codeit.blog.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OneDecimalPlaceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneDecimalPlace {
    String message() default "정수를 허용하지 않고, 소수점은 첫째 자리까지만 입력 가능합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
