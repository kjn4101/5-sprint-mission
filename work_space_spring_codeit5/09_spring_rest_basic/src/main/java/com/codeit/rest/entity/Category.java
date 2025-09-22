package com.codeit.rest.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    TECHNOLOGY("기술", "프로그래밍, IT, 소프트웨어 관련"),
    LIFESTYLE("라이프스타일", "일상, 취미, 자기계발"),
    BUSINESS("비즈니스", "경영, 마케팅, 창업"),
    EDUCATION("교육", "학습, 강의, 지식 공유"),
    ENTERTAINMENT("엔터테인먼트", "영화, 음악, 게임, 문화");

    private final String displayName;
    private final String description;
}
