-- h2 DB 활용시 자동으로 추가될 영역

-- DROP DATABASE IF EXISTS my_blog;
-- CREATE DATABASE my_blog ENCODING = 'UTF8';

DROP TABLE IF EXISTS users CASCADE;

-- users 테이블
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    nickname   VARCHAR(50)  NOT NULL,
    has_avatar  BOOL NOT NULL DEFAULT false,
    birthday    DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);


