--------------------------- DML - INSERT UPDATE DELETE -------------------------------

-- INSERT 문
-- - 테이블에 한 행(ROW)의 데이터 셋을 추가하는 명령어
-- - 한 번에 한 행을 삽입하는게 일반적

-- INSERT 생성 방법 2가지
-- 1. INTO - VALUES set 활용
--  INSERT INTO 테이블명(컬럼명1,컬럼명2,컬럼명3 ... ) VALUES(값1,값2,값3 ...);
-- 2. 컬럼명을 생략하는 방법
--  INSERT INTO 테이블명 VALUES(값1,값2,값3 ... 컬럼의 끝값 까지);
--  -> ※ 주의점 : Table 생성 순서대로 넣어야 하고, 제약사항 검토 필요

SET search_path TO codeit;

SELECT * FROM department;

-- dept_id, dept_title, location_id
INSERT INTO department(dept_id, dept_title, location_id) VALUES ('D11', '개발팀', 'L7'); -- 가장 표준적인 방식
INSERT INTO department VALUES ('F0', '운영팀', 'L7'); -- 컬럼명을 생략하는 경우
INSERT INTO department VALUES ('F1', 'Devops팀'); -- (컬럼 수 불일치해도 삽입 가능)
INSERT INTO department VALUES ('F2', 'Devops팀', DEFAULT); -- default 지정
INSERT INTO department(dept_id, dept_title) VALUES ('E0', '개발팀'); -- 일부 컬럼만 → 반드시 컬럼명 명시

-- RETURNING : 영향을 받은 행의 값을 바로 반환
INSERT INTO department VALUES ('F0', 'Devops팀')
RETURNING dept_id;


-- COMMIT 필요
COMMIT;

-- 직원정보 insert 해보기
SELECT * FROM employee;

INSERT INTO employee
(emp_id, emp_name, emp_no, email, phone,
 dept_code, job_code, sal_level, salary, bonus,
 manager_id, hire_date, ent_date, ent_yn)
VALUES
    (240, '민지','041212-4121222', 'minji@codeit.com', '01012345678',
     'D5', 'J5', 'S5', 3000000, 0.1,
     200, '2020-07-01', NULL, 'N');

-- 다중 행 INSERT
INSERT INTO employee (emp_id, emp_name, emp_no, email)
VALUES (900, '홍길동', '901123-1231237', 'test@email.com'),
       (901, '박길동', '901124-1231237', 'test@email.com'),
       (902, '최길동', '901125-1231237', 'test@email.com');


-- ON CONFLICT (key) DO NOTHING : 중간에 에러가 발생해도 수행
INSERT INTO employee (emp_id, emp_name, emp_no, email)
VALUES (903, '임길동', '901122-1231237', 'test@email.com'),
       (901, '박길동', '901124-1231237', 'test@email.com'), -- emp_id 중복
       (904, '혼길동', '901128-1231237', 'test@email.com')
    ON CONFLICT (emp_id) DO NOTHING;

-- 1. 서브쿼리를 통해 TABLE 복사
DROP TABLE IF EXISTS tbl_insert_test;

CREATE TABLE tbl_insert_test AS
SELECT emp_id, emp_name, dept_title
FROM employee JOIN department ON dept_code = dept_id;

SELECT * FROM tbl_insert_test;
INSERT INTO tbl_insert_test VALUES (245, '홍길동', '개발부');

-- 2. INSERT ... SELECT
DROP TABLE IF EXISTS tbl_insert_test;
CREATE TABLE tbl_insert_test AS
SELECT emp_id, emp_name, dept_title
FROM employee JOIN department ON dept_code = dept_id
WHERE 1=0; -- 구조만 복사

INSERT INTO tbl_insert_test
SELECT emp_id, emp_name, dept_title
FROM employee JOIN department ON dept_code = dept_id;

-- WHERE 절 활용
INSERT INTO tbl_insert_test
SELECT emp_id, emp_name, dept_title
FROM employee JOIN department ON dept_code = dept_id
WHERE dept_code = 'D5';

-- UPDATE
--  - 행의 한 개 또는 다수 데이터를 수정
--  - UPDATE 테이블명 SET 컬럼=값 WHERE 조건;
--  - WHERE 없으면 모든 행이 수정됨

DROP TABLE IF EXISTS tbl_dept_test;
CREATE TABLE tbl_dept_test AS SELECT * FROM department;

SELECT * FROM tbl_dept_test;
-- DESC tbl_dept_test;

-- PostgreSQL에는 sql_safe_updates 없음
-- WHERE 없는 UPDATE = 모든 행 수정
UPDATE tbl_dept_test SET location_id = 'L7';

-- WHERE 절 사용 + RETURNING 사용
UPDATE tbl_dept_test SET location_id = 'L7' WHERE dept_id = 'D6'; -- PK 기반 권장
UPDATE tbl_dept_test SET location_id = 'L7' WHERE dept_title = '총무부'
RETURNING location_id;

-- 다중행 업데이트
UPDATE tbl_dept_test SET dept_title = '국내영업부' WHERE dept_title LIKE '%영업%';


-- UPDATE 서브쿼리 예시
-- 국내영업부 location_id를 총무부 location_id로 변경
UPDATE tbl_dept_test
SET location_id = (
    SELECT location_id FROM tbl_dept_test WHERE dept_title = '총무부' LIMIT 1
    )
WHERE dept_title LIKE '%국내영업부%';


-- → CTE + 조건으로 처리
WITH rows AS (
    SELECT dept_id FROM tbl_dept_test ORDER BY dept_id LIMIT 3
)
UPDATE tbl_dept_test SET dept_title = '전략기획팀'
WHERE dept_id IN (SELECT dept_id FROM rows);


-- DELETE
-- DELETE FROM 테이블명 WHERE 조건;
-- WHERE 없으면 모든 행 삭제됨

DROP TABLE IF EXISTS tbl_dept_test;
CREATE TABLE tbl_dept_test AS SELECT * FROM department;
SELECT * FROM tbl_dept_test;

-- 전체 삭제
DELETE FROM tbl_dept_test;

-- 단일행 삭제
DELETE FROM tbl_dept_test WHERE dept_id = 'D1';
DELETE FROM tbl_dept_test WHERE dept_id = 'D2'
RETURNING dept_id;


-- 다중행 삭제
DELETE FROM tbl_dept_test WHERE dept_title LIKE '%영업%';

-- MySQL: SET foreign_key_checks = 0; (PostgreSQL 없음)
-- PostgreSQL에서는 트리거/제약을 DROP CONSTRAINT 하거나 DEFERRABLE로 조정해야 함
-- 여기서는 일반 DELETE만 가능
DELETE FROM tbl_dept_test WHERE dept_id LIKE 'D%';

-- MySQL: DELETE ... ORDER BY ... LIMIT → PostgreSQL 미지원
-- CTE + IN 절 활용
WITH rows AS (
    SELECT dept_id FROM tbl_dept_test ORDER BY dept_id LIMIT 5
    )
DELETE FROM tbl_dept_test WHERE dept_id IN (SELECT dept_id FROM rows);

-- TRUNCATE : DELETE 보다 빠른 전체 삭제 (ROLLBACK 불가)
DROP TABLE IF EXISTS tbl_dept_test;
CREATE TABLE tbl_dept_test AS SELECT * FROM department;
SELECT * FROM tbl_dept_test;

TRUNCATE TABLE tbl_dept_test;


