package com.codeit.blog.controller;

import com.codeit.blog.config.FileConfig;
import com.codeit.blog.dto.file.AttachFileResponse;
import com.codeit.blog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;
    private final FileConfig fileConfig;

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        AttachFileResponse file = fileService.findById(id);

        File attachDir = fileConfig.getAttachFileUploadDirFile();
        Path path = new File(attachDir, file.getRenamedFileName()).toPath();

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new FileNotFoundException("File not found: " + file.getRenamedFileName());
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = UriUtils.encode(file.getOriginFileName(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()))
                .body(resource);
    }
}
