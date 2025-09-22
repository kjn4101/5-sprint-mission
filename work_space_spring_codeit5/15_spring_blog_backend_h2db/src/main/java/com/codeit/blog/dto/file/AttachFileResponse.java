package com.codeit.blog.dto.file;

import com.codeit.blog.dto.post.PostOnlyIdResponse;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachFileResponse {
    private Long id;
    private String originFileName;
    private String renamedFileName;
    private Long size;
    private String contentType;
    private String url;
    private String downloadUrl;
    private Instant createdAt;
}
