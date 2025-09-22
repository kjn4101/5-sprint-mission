package com.codeit.rest.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "에러 정보")
public class ApiError {
    @Schema(description = "에러 코드")
    private String code;        // NOT_FOUND, BAD_REQUEST ...

    @Schema(description = "에러 메시지")
    private String message;     // 에러 원인 출력 ex) 사용자 id를 찾을수 없습니다.
}
