package com.codeit.blog.storage;

import com.codeit.blog.config.FileConfig;
import com.codeit.blog.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStorage {

    private final FileConfig fileConfig;

    public void saveAvatarFile(String username, MultipartFile avatarFile) {
        File dir = fileConfig.getAvatarUploadDirFile();
        File dest = new File(dir, username);
        // 기존 파일 있으면 삭제
        if (dest.exists()) {
            boolean deleted = dest.delete();
            if (!deleted) {
                throw new RuntimeException("기존 아바타 삭제 실패: " + dest.getAbsolutePath());
            }
        }
        try {
            avatarFile.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("아바타 파일 저장 실패: " + dest.getAbsolutePath(), e);
        }
        System.out.println("아바타 저장 완료: " + dest.getAbsolutePath());
    }


    public void deleteAvatarFile(String username) {
        File dir = fileConfig.getAvatarUploadDirFile();
        File dest = new File(dir, username);
        // 기존 파일 있으면 삭제
        if (dest.exists()) {
            boolean deleted = dest.delete();
            if (!deleted) {
                throw new RuntimeException("기존 아바타 삭제 실패: " + dest.getAbsolutePath());
            }
        }
        System.out.println("아바타 삭제 완료: " + dest.getAbsolutePath());
    }


    public BinaryContent saveAttachFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다");
        }

        Path attachDir = fileConfig.getAttachFileUploadDirFile().toPath();

        // 원본 파일명, 확장자
        String originalName = file.getOriginalFilename();
        String ext = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";

        // 날짜 + UUID 기반 리네임
        String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss")
                .format(java.time.LocalDateTime.now());
        String renamed = timestamp + "_" + UUID.randomUUID().toString().substring(0,8) + ext;
        Path dest = attachDir.resolve(renamed);

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + dest.toAbsolutePath(), e);
        }

        return BinaryContent.builder()
                .originFileName(originalName)
                .renamedFileName(renamed)
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();
    }


    public void deleteAllAttachments(Collection<BinaryContent> files) {
        Path attachDir = fileConfig.getAttachFileUploadDirFile().toPath();

        for (BinaryContent bc : files) {
            if (bc.getRenamedFileName() == null) continue;
            try {
                Files.deleteIfExists(attachDir.resolve(bc.getRenamedFileName()));
            } catch (IOException e) {
                throw new RuntimeException("첨부 파일 삭제 실패: " +
                        attachDir.resolve(bc.getRenamedFileName()).toAbsolutePath(), e);
            }
        }
    }



}
