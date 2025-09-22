------------------------------ ALTER / DROP ------------------------------------
-- ALTER : 테이블이나 각종 제약사항들 등의 Object를 수정하는 명령어 ★★
-- 테이블 수정(컬럼명 수정, 컬럼 추가, 컬럼 삭제), 제약사항 추가/수정/삭제, 기타 객체 변경 가능

SET search_path TO codeit;

-- 사용자가 가진 제약을 확인하는 방법
SELECT * FROM information_schema.table_constraints;
SELECT * FROM information_schema.table_constraints WHERE table_schema = 'codeit';

DROP TABLE IF EXISTS tbl_alter_test;

CREATE TABLE tbl_alter_test(
   user_no INT PRIMARY KEY,
   user_id VARCHAR(20),
   user_name VARCHAR(20)
);

SELECT * FROM tbl_alter_test;
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'tbl_alter_test';

INSERT INTO tbl_alter_test VALUES (1,'test_id1','홍길동');
INSERT INTO tbl_alter_test VALUES (2,'test_id2','김길동');
INSERT INTO tbl_alter_test VALUES (3,'test_id3','최길동');

-- 컬럼 추가하기 (주소 정보)
ALTER TABLE tbl_alter_test ADD COLUMN user_addr VARCHAR(100);
-- ALTER는 COMMIT 없어도 바로 반영됨 → ROLLBACK 불가

INSERT INTO tbl_alter_test VALUES (4,'test_id4','최길동','서울시 강남구');

-- 컬럼 추가하기 (제약사항 + default 값)
ALTER TABLE tbl_alter_test ADD COLUMN user_pw VARCHAR(50) DEFAULT '1234' NOT NULL;

-- 제약사항 추가하기
ALTER TABLE tbl_alter_test ADD CONSTRAINT uq_user_id UNIQUE(user_id);
-- 만일 이미 unique하지 않은 경우 실패!

-- 제약사항 삭제하기
ALTER TABLE tbl_alter_test DROP CONSTRAINT uq_user_id;

-- 제약 확인하는 방법 2가지
-- 1. \d tbl_alter_test (psql)
-- 2. table_constraints에서 찾기 → 이름 확인 가능
SELECT * FROM information_schema.table_constraints WHERE table_name = 'tbl_alter_test';

-- 제약 삭제 추가 - PK
-- MySQL: ALTER TABLE ... DROP PRIMARY KEY;
-- PostgreSQL: PK도 CONSTRAINT 이름으로 삭제해야 함
ALTER TABLE tbl_alter_test DROP CONSTRAINT tbl_alter_test_pkey;

-- 컬럼명 수정하기
ALTER TABLE tbl_alter_test RENAME COLUMN user_addr TO user_address;

-- 컬럼 타입 수정하기
ALTER TABLE tbl_alter_test ALTER COLUMN user_name TYPE VARCHAR(100);

-- 컬럼 속성 한번에 수정 (이름 변경 + 타입 + 기본값 + NOT NULL)
ALTER TABLE tbl_alter_test
    RENAME COLUMN user_name TO user_name2;

ALTER TABLE tbl_alter_test
ALTER COLUMN user_name2 TYPE VARCHAR(1000),
    ALTER COLUMN user_name2 SET DEFAULT '홍길동',
    ALTER COLUMN user_name2 SET NOT NULL;

-- 테이블 이름 변경하기
ALTER TABLE tbl_alter_test RENAME TO tbl_alter_test222;
ALTER TABLE tbl_alter_test222 RENAME TO tbl_alter_test;

-- DROP 명령어
-- - 테이블과 제약사항 등 모든 객체를 제거하는 명령
DROP TABLE IF EXISTS tbl_alter_test; -- 에러 방지용
DROP TABLE tbl_alter_test;

--------------------------------- ALTER / DROP 끝 ---------------------------------------
