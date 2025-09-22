package com.codeit.blog.service;

import com.codeit.blog.dto.file.AttachFileResponse;
import com.codeit.blog.mapper.AttachFileMapper;
import com.codeit.blog.repository.FileRepository;
import com.codeit.blog.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {
    final private FileRepository fileRepository;
    final private AttachFileMapper attachFileMapper;

    @Transactional(readOnly = true)
    public AttachFileResponse findById(Long id) {
        return attachFileMapper.toAttachFileResponse(fileRepository.findById(id).orElseThrow());
    }
}
