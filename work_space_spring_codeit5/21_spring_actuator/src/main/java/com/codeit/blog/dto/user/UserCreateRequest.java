package com.codeit.blog.dto.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotBlank(message = "사용자 이름은 필수입니다")
        @Size(min = 3, max = 50, message = "사용자 이름은 3자 이상 50자 이하여야 합니다")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 60, message = "비밀번호는 8자 이상 60자 이하여야 합니다")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$",
                message = "비밀번호는 숫자, 문자, 특수문자를 포함해야 합니다"
        )
        String password,

        @NotNull
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        @Size(max = 100, message = "이메일은 100자 이하여야 합니다")
        String email,

        @NotBlank(message = "닉네임은 필수입니다")
        @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다")
        String nickname,

        @PastOrPresent(message = "생일은 과거여야 합니다.")
        LocalDate birthday
) {
}
