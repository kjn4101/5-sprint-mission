-- DROP DATABASE IF EXISTS spring_data;
-- CREATE DATABASE spring_data ENCODING = 'UTF8';

DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- users 테이블
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    nickname   VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- posts 테이블
CREATE TABLE posts
(
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(1000) NOT NULL,
    content    VARCHAR(5000) NOT NULL,
    tags       VARCHAR(500),
    category   VARCHAR(50)   NOT NULL,
    author_id  BIGINT        NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_blog_author FOREIGN KEY (author_id) REFERENCES users (id)
);

-- comments 테이블
CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    content    VARCHAR(1000) NOT NULL,
    author_id  BIGINT        NOT NULL,
    post_id    BIGINT        NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_comment_blog FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);



INSERT INTO users ( created_at, username, password, email, nickname)
VALUES
    ( NOW() - INTERVAL '5 days', 'test01', '1234', 'test01@email.com', '스프링마스터'),
    ( NOW() - INTERVAL '4 days', 'test02', '1234', 'test02@email.com', '야근하는개발자'),
    ( NOW() - INTERVAL '3 days', 'test03', '1234', 'test03@email.com', '판교러'),
    (NOW() - INTERVAL '2 days', 'test04', '1234', 'test04@email.com', '코드장인'),
    ( NOW() - INTERVAL '1 days', 'test05', '1234', 'test05@email.com', '디버깅요정');


-- Blog 더미 데이터
INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
 (NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
 (NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
 (NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3),
 (NOW() - INTERVAL '27 days', NOW() - INTERVAL '27 days', '주말 등산 후기', '북한산에 다녀온 주말 이야기.', 'daily,hiking', 'LIFESTYLE', 4),
 (NOW() - INTERVAL '26 days', NOW() - INTERVAL '26 days', 'Spring Security 인증과 인가', 'Spring Security로 인증과 인가 구현하기.', 'spring,security,auth', 'TECHNOLOGY', 5),
 (NOW() - INTERVAL '25 days', NOW() - INTERVAL '25 days', '동네 맛집 탐방기', '집 근처 새로 생긴 국밥집에 다녀왔습니다.', 'daily,food', 'LIFESTYLE', 1),
 (NOW() - INTERVAL '24 days', NOW() - INTERVAL '24 days', 'Docker 컨테이너 활용', 'Docker로 개발 환경을 표준화하는 방법.', 'docker,devops', 'TECHNOLOGY', 2),
 (NOW() - INTERVAL '23 days', NOW() - INTERVAL '23 days', '가족 나들이', '주말에 가족과 함께 근교 나들이를 다녀왔습니다.', 'daily,family', 'LIFESTYLE', 3),
 (NOW() - INTERVAL '22 days', NOW() - INTERVAL '22 days', 'MySQL 성능 최적화', '인덱스와 쿼리 최적화를 통한 MySQL 성능 향상.', 'mysql,database,performance', 'TECHNOLOGY', 4),
 ( NOW() - INTERVAL '21 days', NOW() - INTERVAL '21 days', '영화 감상 기록', '최근 본 영화의 감상평을 남깁니다.', 'daily,movie', 'LIFESTYLE', 5),
 ( NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days', 'Kubernetes 배포 전략', 'Blue-Green과 Rolling Update 전략 비교.', 'kubernetes,devops', 'TECHNOLOGY', 1),
 ( NOW() - INTERVAL '19 days', NOW() - INTERVAL '19 days', '주말 자전거 여행', '한강 자전거 코스를 달린 후기.', 'daily,bicycle', 'LIFESTYLE', 2),
 ( NOW() - INTERVAL '18 days', NOW() - INTERVAL '18 days', 'Redis 캐시 활용법', 'Redis를 활용한 애플리케이션 캐싱 전략.', 'redis,cache', 'TECHNOLOGY', 3),
 ( NOW() - INTERVAL '17 days', NOW() - INTERVAL '17 days', '반려동물 이야기', '우리집 강아지와 보낸 하루.', 'daily,pet', 'LIFESTYLE', 4),
 ( NOW() - INTERVAL '16 days', NOW() - INTERVAL '16 days', 'Git 브랜치 전략', 'Git Flow와 Trunk Based Development 비교.', 'git,vcs', 'TECHNOLOGY', 5),
 ( NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days', '책 읽는 시간', '최근 읽은 책에 대한 감상문.', 'daily,book', 'LIFESTYLE', 1),
 ( NOW() - INTERVAL '14 days', NOW() - INTERVAL '14 days', 'Spring Batch 대용량 처리', 'Spring Batch를 활용한 대용량 데이터 처리 방법.', 'spring,batch', 'TECHNOLOGY', 2),
 ( NOW() - INTERVAL '13 days', NOW() - INTERVAL '13 days', '집안 인테리어 DIY', '방을 새롭게 꾸미는 DIY 프로젝트.', 'daily,diy', 'LIFESTYLE', 3),
 ( NOW() - INTERVAL '12 days', NOW() - INTERVAL '12 days', 'AWS S3 파일 업로드', 'AWS S3를 활용한 파일 업로드 구현.', 'aws,s3', 'TECHNOLOGY', 4),
 ( NOW() - INTERVAL '11 days', NOW() - INTERVAL '11 days', '주말 캠핑 이야기', '가족과 함께한 캠핑의 즐거움.', 'daily,camping', 'LIFESTYLE', 5),
 ( NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days', 'Elasticsearch 검색 구현', 'Spring Data Elasticsearch를 이용한 검색 기능.', 'elasticsearch,search', 'TECHNOLOGY', 1),
 ( NOW() - INTERVAL '9 days', NOW() - INTERVAL '9 days', '아침 조깅 루틴', '매일 아침 5km 조깅을 시작했습니다.', 'daily,jogging', 'LIFESTYLE', 2),
 ( NOW() - INTERVAL '8 days', NOW() - INTERVAL '8 days', 'Kafka 메시지 처리', 'Apache Kafka를 활용한 실시간 메시징 시스템.', 'kafka,messaging', 'TECHNOLOGY', 3),
 ( NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days', 'Spring Framework 도전', 'Spring Framework MVC 공부 했습니다. 생각보다 어렵네요.', 'spring,cloud', 'TECHNOLOGY', 2),
 ( NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days', '주말 요리 도전', '파스타를 처음으로 만들어본 날.', 'daily,cooking', 'LIFESTYLE', 4),
 ( NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days', 'GraphQL API 설계', 'GraphQL을 이용한 유연한 API 설계.', 'graphql,api', 'TECHNOLOGY', 5),
 ( NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', '친구와의 저녁식사', '오랜만에 친구와 함께한 즐거운 식사.', 'daily,friends', 'LIFESTYLE', 1),
 ( NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days', 'Spring Cloud 마이크로서비스', 'Spring Cloud로 마이크로서비스 아키텍처 구축.', 'spring,cloud', 'TECHNOLOGY', 2),
 ( NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', '주말 영화관 나들이', '새로 개봉한 영화를 보러 갔습니다. 재밌었습니다.', 'daily,movie', 'LIFESTYLE', 3),
 ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 'CI/CD 파이프라인 구축', 'Jenkins와 GitHub Actions를 활용한 자동 배포.', 'cicd,jenkins', 'TECHNOLOGY', 4);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 프레임워크 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', '스프링 JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 프레임워크 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', '스프링 JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);

INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 부트 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', 'JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO posts (created_at, updated_at, title, content, tags, category, author_id) VALUES
( NOW() - INTERVAL '30 days', NOW() - INTERVAL '30 days', '스프링 프레임워크 REST API 설계', 'Spring Boot와 RESTful API 설계 방법을 다룹니다.', 'spring,rest,api', 'TECHNOLOGY', 1),
( NOW() - INTERVAL '29 days', NOW() - INTERVAL '29 days', '오늘의 커피 일기', '아침에 마신 라떼가 너무 맛있어서 기록합니다.', 'daily,coffee', 'LIFESTYLE', 2),
( NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', '스프링 JPA 연관관계 매핑 가이드', 'JPA에서 연관관계를 매핑하는 다양한 방법과 주의사항.', 'jpa,hibernate,mapping', 'TECHNOLOGY', 3);


INSERT INTO comments (created_at, updated_at, content, author_id, post_id) VALUES
  ( NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', 'REST API 글 잘 봤습니다.', 2, 1),
  ( NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days', '도움이 많이 되었어요.', 3, 1),
  ( NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', '커피 얘기 공감합니다!', 1, 2),
  ( NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', '연관관계 매핑 설명이 유익하네요.', 5, 3),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 'Hibernate 부분도 정리 부탁드려요.', 4, 3),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '저도 북한산 다녀왔습니다.', 2, 4),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 'Security 설정 이해에 도움 됩니다.', 3, 5),
  ( NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days', '국밥집 위치가 궁금하네요!', 4, 6),
  ( NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days', 'Docker Compose 글도 보고 싶습니다.', 5, 7),
  ( NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days', '실무 적용 예시 기대합니다.', 1, 7),
  ( NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', '가족 나들이 보기 좋네요.', 2, 8),
  ( NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days', 'MySQL 인덱스 부분이 특히 좋았습니다.', 3, 9),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', '영화 감상평 잘 읽었습니다.', 4, 10),
  (NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '저도 같은 영화 봤어요!', 5, 10),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', '쿠버네티스 전략 비교가 유익합니다.', 1, 11),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '자전거 코스 정보 감사해요.', 2, 12),
  (NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', 'Redis 캐시 활용법 도움됩니다.', 3, 13),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', '강아지 사진도 있나요?', 4, 14),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Git Flow 설명이 명확합니다.', 5, 15),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Trunk Based도 써봤습니다.', 1, 15),
  ( NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', '책 추천도 해주세요!', 2, 16),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Batch 작업 성능 팁 감사합니다.', 3, 17),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'DIY 프로젝트 멋지네요.', 4, 18),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'S3 업로드 코드 예시 부탁드려요.', 5, 19),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '캠핑 분위기 전해지네요.', 1, 20),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Elasticsearch 적용해봐야겠어요.', 2, 21),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '조깅 루틴 대단하세요.', 3, 22),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Kafka 글 잘 읽었습니다.', 4, 23),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', 'Spring MVC는 쉽지 않죠 ㅎㅎ', 5, 24),
  ( NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', '요리 글 보니 배고파지네요.', 1, 25);


SELECT * FROM posts;

COMMIT ;

