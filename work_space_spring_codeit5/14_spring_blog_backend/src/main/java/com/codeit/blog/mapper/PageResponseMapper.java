package com.codeit.blog.mapper;

import com.codeit.blog.dto.base.SimplePageResponse;
import com.codeit.blog.dto.base.SlicePageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

    // Page<Entity> → SimplePageResponse<Entity>
    default  <T> SimplePageResponse<T> toResponse(Page<T> page) {
        return SimplePageResponse.<T>builder()
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .items(page.getContent())
                .build();
    }


    // Page<Entity> → SimplePageResponse<Dto>
    default  <T, R> SimplePageResponse<R> toResponse(Page<T> page, List<R> items) {
        return SimplePageResponse.<R>builder()
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .items(items)
                .build();
    }

    // Slice → SlicePageResponse
    default <T> SlicePageResponse<T> toSlice(Slice<T> slice, Object nextCursor) {
        return SlicePageResponse.<T>builder()
                .size(slice.getSize())
                .hasNext(slice.hasNext())
                .nextCursor(nextCursor)
                .items(slice.getContent())
                .build();
    }

    // Slice → SlicePageResponse
    default <T, R> SlicePageResponse<R> toSlice(Slice<T> slice, List<R> items, Object nextCursor) {
        return SlicePageResponse.<R>builder()
                .size(slice.getSize())
                .hasNext(slice.hasNext())
                .nextCursor(nextCursor)
                .items(items)
                .build();
    }
}
