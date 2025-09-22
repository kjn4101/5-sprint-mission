---------------------------------- CREATE 시작 ------------------------------------

-- 문자형 : CHAR, VARCHAR★★★★★, TEXT
-- CHAR(길이) : 길이 고정형 문자열, 지정 길이만큼 공간 확보
-- VARCHAR(길이) : 가변 길이 문자열, 선언한 최대 길이 제한 내에서 저장, 실제 사용 길이만큼 공간 사용
-- TEXT : 매우 긴 문자열 저장 Type, 길이 지정 불가
--  ※ 주의 : PostgreSQL에는 MEDIUMTEXT/LONGTEXT 타입이 없으며 TEXT 하나로 통일됨
--  ※ 권장 : 길이 제어와 인덱싱을 고려하면 VARCHAR를 우선 검토하고, 매우 긴 본문 등은 TEXT 사용

-- 숫자형 : INTEGER, BIGINT★, DOUBLE PRECISION, DECIMAL(NUMERIC), BOOLEAN
-- INTEGER(INT) : 정수형 (4byte, 약 ±21억)
-- BIGINT : 8byte 정수형 (9경)
-- REAL : 실수 (4byte)
-- DOUBLE PRECISION : 실수(8byte)
-- DECIMAL(precision, scale) 또는 NUMERIC : 고정 소수점 정밀 제어
-- BOOLEAN : true / false

-- 날짜형 : DATE, TIME, TIMESTAMP [WITHOUT|WITH] TIME ZONE
-- DATE : 날짜만 저장
-- TIME : 시간만 저장
-- TIMESTAMP : 날짜+시간, WITHOUT TIME ZONE(기본) 또는 WITH TIME ZONE(=TIMESTAMPTZ)
--  ※ 주의 : PostgreSQL의 TIMESTAMP는 내부 정수 저장/2038 제한 개념이 아니며 매우 넓은 범위 지원

-- 이진형 : BYTEA
-- 이진 데이터 저장 가능(이미지, 동영상 등)
-- ※ 일반적으로 파일은 DB에 저장하지 않고 경로만 저장하는 것을 권장

-- 스키마 생성 및 전환
CREATE SCHEMA codeit;
COMMIT ;
DROP SCHEMA codeit;
SET search_path TO codeit;

SELECT * FROM employee;

-- 딕셔너리 조회하기

-- 예시: 스키마 내 테이블 목록
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema = 'codeit';

-- 예시: 권한 확인
SELECT rolname, rolsuper, rolcreaterole, rolcreatedb
FROM pg_roles;

-- Table 생성하는 방법

DROP TABLE IF EXISTS tbl_member;
CREATE TABLE tbl_member(
    member_no BIGINT,
    member_id VARCHAR(20),
    member_pwd VARCHAR(20),
    member_name BIGINT
);

SELECT * FROM tbl_member;

-- PostgreSQL 확인: \d tbl_member (psql) 또는 information_schema.columns 조회
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'codeit' AND table_name = 'tbl_member';


-- IF EXISTS, IF NOT EXISTS
-- -> DDL 스크립트를 구성할때 중간에 에러가 발생해도 스크립트가 실행되게끔 보호하는 문장
DROP TABLE IF EXISTS tbl_member2;
CREATE TABLE IF NOT EXISTS tbl_member2(
    member_no BIGINT,
    member_id VARCHAR(20),
    member_pwd VARCHAR(20),
    member_name BIGINT
);


DROP TABLE IF EXISTS tbl_member3;
CREATE TABLE IF NOT EXISTS tbl_member3(
    member_no BIGINT,
    member_id VARCHAR(20),
    member_pwd VARCHAR(20),
    member_name BIGINT
);

-- 식별자에 대문자 구별 : 이중따옴표(") 사용 -> 절대 사용하지 말것!! 테이블 이름은 소문자로만 구성 권장
DROP TABLE IF EXISTS "Member_test";
CREATE TABLE "Member_test"(
   "Member_id"   VARCHAR(20),
   "Member_pwd"  VARCHAR(20),
   "Member_name" VARCHAR(20)
);

-- 문자열 tpye을 사용하는 테이블

DROP TABLE IF EXISTS tbl_str_data;
CREATE TABLE tbl_str_data(
    a CHAR(3),      -- 고정길이 문자
    b VARCHAR(3),   -- 가변길이 문자 ★★★★★
    c TEXT          -- 긴문자열, 길이 지정이 불가
);

SELECT * FROM tbl_str_data;

INSERT INTO tbl_str_data (a, b, c) VALUES ('ABC', 'ABC', 'ABC'); -- 영문 3글자
INSERT INTO tbl_str_data (a, b, c) VALUES ('가나다', '가나다', '가나다'); -- 한글 3글자
INSERT INTO tbl_str_data (a, b, c) VALUES ('ABCDEF', 'ABCDEF', 'ABCDEF'); -- 영문 6글자
INSERT INTO tbl_str_data (a, b, c) VALUES ('가나다라마바', '가나다라마바', '가나다라마바'); -- 한글 6글자
INSERT INTO tbl_str_data (a, b, c) VALUES ('가나다', '가나다', '가나다라마바'); -- 3, 3, 제한없음

-- 데이터 길이 검사하는 방법
-- 1) char_length(length) : 글자 수
-- 2) octet_length : byte 길이
SELECT length(a), length(b), length(c) FROM tbl_str_data;
SELECT octet_length(a), octet_length(b), octet_length(c) FROM tbl_str_data;

COMMIT;   -- 트랜잭션 확정
ROLLBACK; -- 트랜잭션 취소

-- 숫자 Type Table 생성
DROP TABLE IF EXISTS tbl_number_data;
CREATE TABLE tbl_number_data(
    a INTEGER,               -- 정수, 약 ±21억 (INT로 생략 가능)
    b BIGINT,                -- 8바이트 정수
    c DOUBLE PRECISION,      -- 배정밀도 실수 (생략 불가능)
    d DECIMAL(5, 2)          -- 총 5자리, 소수 2자리
);

INSERT INTO tbl_number_data VALUES(123.45, 123.45, 123.45, 123.45); -- 정수 a는 123으로 저장

-- PostgreSQL은 precision/scale 위반 시 잘려서 INSERT 가능하다.
INSERT INTO tbl_number_data VALUES(-123, -123, 123.456789, 123.456789);

-- 정수/정밀도 범위 초과 예시
INSERT INTO tbl_number_data VALUES(12345678901234567890.456,12345678901234567890.456,
                                   12345678901234567890.456,12345678901234567890.456);

INSERT INTO tbl_number_data VALUES(210000, 123456782000, 1234567890.456, 12.46);
SELECT * FROM tbl_number_data;

-- date/time type table 생성
DROP TABLE IF EXISTS tbl_date_data;
CREATE TABLE tbl_date_data(
      date1 DATE,                  -- 날짜만
      time1 TIME,                  -- 시간만
      datetime1 TIMESTAMP,         -- 날짜시간 (권장 X)
      timestamp1 TIMESTAMPTZ       -- 타임존 포함 Timestamp
);

SELECT * FROM tbl_date_data;
SELECT now(), statement_timestamp();  -- 현재시간 statement_timestamp() 또는 now()
INSERT INTO tbl_date_data VALUES(now()::date, now()::time, now(), now());
INSERT INTO tbl_date_data VALUES(CURRENT_DATE, CURRENT_TIME, now(), now());
INSERT INTO tbl_date_data VALUES(DATE '2025-07-09', TIME '14:37', TIMESTAMP '2025-07-09 15:59:07', now());
INSERT INTO tbl_date_data VALUES(DATE '2025-07-09', TIME '14:37', TIMESTAMP '2025-07-09 15:59:07',
                                 TIMESTAMPTZ '2025-07-09 15:59:07+09');


-- ENUM 예제
-- ENUM 타입 생성 (스키마 내 타입)
DROP TYPE IF EXISTS status CASCADE;
CREATE TYPE status AS ENUM ('ACTIVE', 'INACTIVE', 'PENDING');

-- 테이블 생성
DROP TABLE IF EXISTS tbl_enum_demo;
CREATE TABLE tbl_enum_demo (
   id         BIGINT,
   user_name  VARCHAR(40) ,
   user_status status  DEFAULT 'PENDING'
);

-- 입력 예시
INSERT INTO tbl_enum_demo (user_name, user_status) VALUES
   ('홍길동', 'ACTIVE'),
   ('이몽룡', 'INACTIVE'),
   ('성춘향', DEFAULT);  -- PENDING

-- 조회 예시
SELECT id, user_name, user_status FROM tbl_enum_demo ORDER BY id;

-- 값 추가 예시 (필요 시)
-- ALTER TYPE status ADD VALUE 'SUSPENDED';  -- 기존 ENUM에 값 추가


-- BOOLEAN 예제
DROP TABLE IF EXISTS tbl_bool_demo;
CREATE TABLE tbl_bool_demo (
       id        BIGINT,
       title     TEXT,
       is_active BOOLEAN DEFAULT FALSE,
       is_admin  BOOLEAN DEFAULT FALSE
);

-- 입력 예시
INSERT INTO tbl_bool_demo (title, is_active, is_admin) VALUES
   ('일반 사용자', TRUE, FALSE),
   ('관리자 사용자', TRUE, TRUE),
   ('비활성 사용자', FALSE, FALSE);

-- 조회 예시 (BOOLEAN은 IS TRUE / IS FALSE 권장)
SELECT id, title FROM tbl_bool_demo WHERE is_active IS TRUE;
SELECT id, title FROM tbl_bool_demo WHERE is_admin = TRUE;   -- 동일 의미


-- UUID 예제
-- UUID 생성 함수 사용을 위해 확장 설치 (pgcrypto 권장)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS tbl_uuid_demo;
CREATE TABLE tbl_uuid_demo (
   id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- pgcrypto
   memo       TEXT,
   created_at TIMESTAMPTZ DEFAULT now()
);

-- 입력 예시 (기본값 생성 + 수동 지정 혼합)
INSERT INTO tbl_uuid_demo (memo) VALUES ('기본 UUID 생성');
INSERT INTO tbl_uuid_demo (id, memo) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', '수동 지정 UUID');

-- 조회
SELECT id, memo, created_at FROM tbl_uuid_demo;


---------------------------------- 배열(Array) 예제 ----------------------------------
DROP TABLE IF EXISTS tbl_array_demo;
CREATE TABLE tbl_array_demo (
    id        BIGINT,
    tags      TEXT[]     DEFAULT '{}',     -- 문자열 배열
    scores    INTEGER[]    DEFAULT '{}',     -- 정수 배열
    created_at TIMESTAMPTZ  DEFAULT now()
);

-- 입력 예시 (ARRAY 리터럴)
INSERT INTO tbl_array_demo (tags, scores) VALUES
  (ARRAY['db','postgres','lecture'], ARRAY[90,85,95]),
  (ARRAY['sql','index'],             ARRAY[70,88]),
  ('{}',                              '{}');          -- 빈 배열

-- 조회: 배열 길이/크기
SELECT id,
       array_length(tags, 1)    AS tag_len,
       cardinality(scores)      AS score_count
FROM tbl_array_demo;

-- 배열 포함/교집합 연산
-- @> : 좌변이 우변을 포함
SELECT * FROM tbl_array_demo WHERE tags @> ARRAY['postgres'];     -- 'postgres' 포함 행
-- && : 교집합 존재
SELECT * FROM tbl_array_demo WHERE tags && ARRAY['index','plan']; -- 교집합이 있으면 선택

-- ANY/ALL 예시
SELECT * FROM tbl_array_demo WHERE 95 = ANY (scores);    -- scores 중 95가 있으면
SELECT * FROM tbl_array_demo WHERE scores @> ARRAY[90];  -- 동일 의미

-- 배열 원소 펼치기
SELECT id, unnest(tags) AS tag FROM tbl_array_demo;

-- 배열 원소 추가/병합 ( 연산자 또는 array_cat)
UPDATE tbl_array_demo
SET tags = tags  ARRAY['tip']    -- 뒤에 추가
WHERE id = 1;

-- 배열 조회 확인
SELECT id, tags, scores FROM tbl_array_demo ORDER BY id;


---------------------------------- CREATE 끝 ------------------------------------