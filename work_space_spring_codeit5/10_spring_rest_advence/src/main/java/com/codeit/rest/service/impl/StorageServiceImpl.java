package com.codeit.rest.service.impl;

import com.codeit.rest.config.FileConfig;
import com.codeit.rest.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Service("storageService")
public class StorageServiceImpl implements StorageService {

    private final FileConfig fileConfig;

    @Override
    public boolean store(String path, InputStream in) throws IOException {
        Path baseDir = Paths.get(fileConfig.getUploadDir()).toAbsolutePath().normalize();
        Path target  = baseDir.resolve(path).normalize();
        Files.createDirectories(target.getParent());
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        return Files.exists(target);
    }

    @Override
    public void delete(String path) throws IOException {
        Path baseDir = Paths.get(fileConfig.getUploadDir()).toAbsolutePath().normalize();
        Path target  = baseDir.resolve(path).normalize();
        Files.deleteIfExists(target);
    }
}
