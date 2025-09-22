package com.codeit.file.controller;

import com.codeit.file.config.FileConfig;
import com.codeit.file.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 파일 다운로드 또는 리스트 제공
@Controller
@RequiredArgsConstructor
public class FileViewController {

    private final FileConfig fileConfig;

    @GetMapping("/files")
    public String listUploadedFiles(ModelMap model) {
        File folder = new File(fileConfig.getUploadDir());

        if(!folder.exists() || !folder.isDirectory()){
            model.addAttribute("files", List.of());
            return "file/file-list";
        }

        // 파일명 리스트
        List<String> fileNames = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .toList();
        model.addAttribute("files", fileNames);
        return "file/file-list";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        String uploadDir = fileConfig.getUploadDir();
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
