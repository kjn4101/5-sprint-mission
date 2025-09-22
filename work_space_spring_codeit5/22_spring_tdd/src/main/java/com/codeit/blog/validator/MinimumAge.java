package com.codeit.blog.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinimumAgeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinimumAge {
    String message() default "만 {value}세 이상만 가입할 수 있습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value(); // 최소 나이
}

