package com.codeit.blog.mapper;

import com.codeit.blog.dto.file.AttachFileResponse;
import com.codeit.blog.entity.BinaryContent;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Mapper(componentModel = "spring")
public interface AttachFileMapper {

    AttachFileResponse toAttachFileResponse(BinaryContent file);

    @AfterMapping
    default void setUrl(BinaryContent file, @MappingTarget AttachFileResponse response) {
        if (file.getRenamedFileName() != null) {
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/attachments/")
                    .toUriString();
            response.setUrl(baseUrl + file.getRenamedFileName());
        }
        if (file.getId() != null) {
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/")
                    .path(String.valueOf(file.getId()))
                    .path("/download")
                    .toUriString();
            response.setDownloadUrl(downloadUrl);
        }
    }
}
