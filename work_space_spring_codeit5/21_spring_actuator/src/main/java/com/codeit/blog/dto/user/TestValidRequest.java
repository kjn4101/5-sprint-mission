package com.codeit.blog.dto.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record TestValidRequest(
//        @NotNull // null만 -> 사용하지 말것!! 쓸일 있을때만
//        @NotBlank //  null + 공백
        @NotBlank(message = "이름은 필수 값입니다.")
        @Size(min = 2, max = 20, message = "이름은 2~20자 이내로 입력하세요.")
        @Pattern(regexp = "^[가-힣]{2,20}$", message = "이름은 2~20자의 한글만 입력 가능합니다.")
        String name,

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 주소 양식을 입력하세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력하세요.")
        @Pattern(
                regexp = "^[A-Za-z0-9_!@#$%^&*()]{8,20}$",
                message = "패스워드는 영문/숫자/언더스코어+_!@#$%^&*() 조합 8~16자로 입력해 주시기 바랍니다."
        )
        @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 이하로 입력하세요.")
        String password,

        @Positive(message = "나이는 양수만 입력하세요.")
        @Max(value = 120, message = "나이는 120세 이하로 입력하세요.")
        Integer age,

        @NotNull(message = "체크인 날짜를 입력하세요.")
        @FutureOrPresent(message = "체크인은 오늘 이후로 날짜 선택이 가능합니다.")
        LocalDate checkIn,

        @NotNull(message = "체크아웃 날짜를 입력하세요.")
        @Future(message = "체크아웃 미래 날짜로만 가능합니다.")
        LocalDate checkOut,

        @Pattern(
                regexp = "^(01[016789])[-]?\\d{3,4}[-]?\\d{4}$",
                message = "휴대폰 번호는 010-1234-5678 형식으로 입력해 주세요."
        )
        String mobile,

        @Pattern(
                regexp = "^[A-Za-z0-9_]{3,16}$",
                message = "아이디는 영문/숫자/언더스코어 조합 3~16자로 입력해 주시기 바랍니다."
        )
        String username,

        @NotEmpty(message = "취미는 최소 1개 이상 선택하세요.")
        List<@NotBlank String> hobby
) {
}
