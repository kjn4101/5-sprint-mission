-- h2 DB 활용시 자동으로 추가될 영역

-- DROP DATABASE IF EXISTS my_blog;
-- CREATE DATABASE my_blog ENCODING = 'UTF8';

DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS binary_content CASCADE;
DROP TABLE IF EXISTS post_attachments CASCADE;

-- users 테이블
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    nickname   VARCHAR(50)  NOT NULL,
    has_avatar  BOOL NOT NULL DEFAULT false,
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

-- file storage
CREATE TABLE binary_content
(
    id         BIGSERIAL PRIMARY KEY,
    origin_file_name    VARCHAR(255) NOT NULL,
    renamed_file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- file와 유첨파일 관계
CREATE TABLE post_attachments
(
    post_id       BIGINT,
    attachment_id BIGINT,
    PRIMARY KEY (post_id, attachment_id),

    CONSTRAINT fk_post_att_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,

    CONSTRAINT fk_post_att_file
        FOREIGN KEY (attachment_id) REFERENCES binary_content (id) ON DELETE RESTRICT,

    CONSTRAINT uq_post_att_one_to_many UNIQUE (attachment_id)
);


