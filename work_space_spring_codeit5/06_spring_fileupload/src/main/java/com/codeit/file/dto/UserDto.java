package com.codeit.file.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UserDto {
    private String username;
    private String name;

    // 2025-08-14 
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private MultipartFile avatar; // 파일 업로드용

}
