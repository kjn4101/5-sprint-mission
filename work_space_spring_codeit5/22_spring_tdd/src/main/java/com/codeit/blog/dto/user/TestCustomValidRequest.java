package com.codeit.blog.dto.user;

import com.codeit.blog.validator.MinimumAge;
import com.codeit.blog.validator.OneDecimalPlace;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record TestCustomValidRequest(

        @NotNull(message = "나이는 필수 입력입니다")
        @MinimumAge(value = 14, message = "만 14세 이상만 가입할 수 있습니다")
        Integer age,

        @NotNull(message = "점수는 필수 입력입니다")
        @OneDecimalPlace(message = "점수는 소수점 첫째 자리까지만 입력 가능합니다")
//        @Digits(integer = 5, fraction = 1)
        Double score
) {
}
