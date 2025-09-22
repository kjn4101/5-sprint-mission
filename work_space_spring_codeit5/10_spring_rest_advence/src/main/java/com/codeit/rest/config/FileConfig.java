package com.codeit.rest.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Getter
@Configuration
public class FileConfig {

    private String uploadDir;

    @PostConstruct
    public void init() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            uploadDir = "C:/uploads/";
        } else {
            uploadDir = "/var/uploads/";
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("업로드 디렉토리 생성 완료: " + uploadDir);
            } else {
                System.err.println("업로드 디렉토리 생성 실패: " + uploadDir);
            }
        }
    }
}
