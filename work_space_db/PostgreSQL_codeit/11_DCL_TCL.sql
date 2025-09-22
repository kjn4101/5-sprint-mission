-------------------------------- DCL(권한, 트랜잭션) ---------------------------------------
-- DCL : Data Control Language
--  - 데이터의 제어 명령어, 보안, 무결성, 복원 등을 위해 사용되는 명령어 세트
--  1) 권한부여 : GRANT, REVOKE
--  2) 트랜잭션 관리 : COMMIT, ROLLBACK, SAVEPOINT
SET search_path TO codeit;

-- 사용자 생성 및 권한 부여 (슈퍼유저 계정에서 실행해야 함)
DROP ROLE IF EXISTS user1;
CREATE ROLE user1 WITH LOGIN PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE postgres TO user1;   -- 특정 DB에 전체 권한 부여
ALTER ROLE user1 WITH SUPERUSER LOGIN CREATEDB CREATEROLE;

-- 권한 회수
REVOKE ALL PRIVILEGES ON DATABASE postgres FROM user1;
DROP ROLE IF EXISTS user1;

------------------------------- 트랜잭션 ----------------------------------------
-- TCL ★★★★★
-- 트랜잭션이란?
--  - DB에서의 작업 단위. 여러 쿼리를 조합하여 업무 처리 단위로 만들 때 사용 (INSERT, UPDATE, DELETE, SELECT+@)
--  - ACID 원칙 준수가 중요 (원자성, 일관성, 독립성, 지속성)

-- COMMIT : 트랜잭션이 정상적으로 완료되었을 때 확정
-- ROLLBACK : 실패했을 경우 마지막 COMMIT 상태로 복구
-- SAVEPOINT <이름> : 트랜잭션 중간에 복원 지점 저장
-- ROLLBACK TO <이름> : 지정된 SAVEPOINT로 복원
-- RELEASE SAVEPOINT <이름> : SAVEPOINT 삭제
-- 주의 : DDL(CREATE, ALTER, DROP), DCL은 트랜잭션 자동 COMMIT 됨

DROP TABLE IF EXISTS tbl_transaction_test;
CREATE TABLE tbl_transaction_test(
     user_id   VARCHAR(20),
     user_name VARCHAR(20)
);

SELECT * FROM tbl_transaction_test;

-- INSERT 후 ROLLBACK 실습
BEGIN;
INSERT INTO tbl_transaction_test VALUES('TEST1','홍길동');
INSERT INTO tbl_transaction_test VALUES('TEST2','김길동');
INSERT INTO tbl_transaction_test VALUES('TEST3','박길동');
ROLLBACK;  -- 모든 작업 취소

BEGIN;
INSERT INTO tbl_transaction_test VALUES('TEST1','홍길동');
COMMIT;  -- 커밋된 데이터는 이후 ROLLBACK해도 유지

BEGIN;
INSERT INTO tbl_transaction_test VALUES('TEST1','홍길동');
COMMIT;

BEGIN;
INSERT INTO tbl_transaction_test VALUES('TEST2','김길동');
INSERT INTO tbl_transaction_test VALUES('TEST3','박길동');
ROLLBACK; -- 마지막 2개만 취소 → TEST1만 남음

-- SAVEPOINT 실습
DROP TABLE IF EXISTS tbl_transaction_test;
CREATE TABLE tbl_transaction_test(
     user_id   VARCHAR(20),
     user_name VARCHAR(20)
);

BEGIN;
INSERT INTO tbl_transaction_test VALUES('TEST1','홍길동');
SAVEPOINT sp1;

INSERT INTO tbl_transaction_test VALUES('TEST2','김길동');
SAVEPOINT sp2;

INSERT INTO tbl_transaction_test VALUES('TEST3','박길동');
SAVEPOINT sp3;

-- 특정 시점으로 복원 가능
ROLLBACK TO sp1; -- TEST1만 남음
-- 또는 ROLLBACK TO sp2; -- TEST1, TEST2 남음
-- 또는 ROLLBACK TO sp3; -- TEST1, TEST2, TEST3 남음

-- SAVEPOINT는 앞선 시점으로만 되돌릴 수 있음
-- 예: sp1 → sp3 복원은 불가
COMMIT;

SELECT * FROM tbl_transaction_test;

--------------------------------------------------------------------------------
