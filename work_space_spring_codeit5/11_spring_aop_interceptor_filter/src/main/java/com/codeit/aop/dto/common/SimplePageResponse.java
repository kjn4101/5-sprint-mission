package com.codeit.aop.dto.user.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SimplePageResponse<E> {
    private int page; // 요청한 페이지
    private int size; // item의 사이즈(보여줄 목록의 사이즈)
    private int totalPages; // 페이지의 총 갯수
    private long totalElements; // 아이템의 총 갯수
    private List<E> elements; // 요소 리스트
}
