-------------------------------- INDEX -----------------------------------------

-- INDEX란? ★★
-- DB에서 데이터 검색 성능 향상을 위해 별도의 INDEX를 생성하여 빠르게 검색할 수 있도록 돕는 기능
-- 사전에 색인처럼 Key 값과 실제 데이터 위치를 매핑하여 빠른 탐색 가능
-- PK, UNIQUE 제약 시 자동으로 INDEX가 생성됨 ★★★★★

-- INDEX 종류
-- 1. 고유 인덱스 (UNIQUE INDEX): 고유한 값만 허용, PK 선언 시 자동 생성 ★★★★★
-- 2. 비고유 인덱스 (NON-UNIQUE INDEX): 중복값 허용, 일반적으로 많이 사용 ★★★
-- 3. 단일 인덱스 (SINGLE INDEX): 하나의 컬럼만 인덱스에 사용
-- 4. 결합 인덱스 (COMPOSITE INDEX): 두 개 이상의 컬럼을 결합하여 인덱스 생성
SET search_path TO codeit;

-- 인덱스 조회 방법
SELECT * FROM employee;
SELECT * FROM pg_indexes WHERE tablename = 'employee';

-- 인덱스 생성 문법
-- CREATE [UNIQUE] INDEX 인덱스명 ON 테이블명 (컬럼명|표현식);

-- 고유 인덱스 생성 (유니크 값만 가능, PK는 자동 생성됨)
CREATE UNIQUE INDEX idx_emp_no ON employee(emp_no);
-- 유니크하지 않으면 생성 실패

SELECT * FROM employee WHERE emp_no = '860508-1342154';
EXPLAIN SELECT * FROM employee WHERE emp_no = '860508-1342154'; -- 인덱스 사용 여부 확인

SET enable_seqscan = off; -- 테이블 길이가 짧으면 index 조회 안함...

EXPLAIN SELECT * FROM employee WHERE emp_no = '860508-1342154'; -- 인덱스 사용 여부 확인
EXPLAIN SELECT * FROM employee WHERE phone = '01099546325';
-- 인덱스 없으면 Seq Scan, 있으면 Index Scan

-- 비고유 인덱스 생성 (일반 인덱스)
CREATE INDEX idx_dept_code ON employee(dept_code);
EXPLAIN SELECT * FROM employee WHERE dept_code = 'D5';
EXPLAIN SELECT * FROM employee WHERE dept_code LIKE 'D5%'; -- LIKE절은 index 안탐

-- 결합 인덱스 (두 개 이상 컬럼)
CREATE INDEX idx_name_phone ON employee(emp_name, phone);

EXPLAIN SELECT * FROM employee WHERE emp_name = '하이유' AND phone = '01036654488'; -- idx_name_phone 사용
EXPLAIN SELECT * FROM employee WHERE emp_name = '하이유';   -- 선두 컬럼 emp_name 인덱스 사용
EXPLAIN SELECT * FROM employee WHERE phone = '01036654488'; -- 단독 phone 조건 인덱스 사용
EXPLAIN SELECT * FROM employee WHERE emp_name LIKE '%하이%'; -- LIKE절은 와일드카드 → 인덱스 적용 안됨

-- 인덱스 삭제
DROP INDEX idx_dept_code;

-- PostgreSQL에서는 인덱스 최적화를 위해 VACUUM 명령 사용 가능
VACUUM (ANALYZE) idx_dept_code;


-- Index 성능 참고: https://www.postgresql.org/docs/current/indexes.html

-- Index 성능 시험

DROP TABLE IF EXISTS index_test;
CREATE TABLE index_test (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(50),
    email VARCHAR(100),
    reg_date TIMESTAMP
);

-- 대량 데이터 삽입 함수 (PostgreSQL)
CREATE OR REPLACE FUNCTION insert_bulk_data(loop_count INT)
    RETURNS void
    LANGUAGE plpgsql
AS $$
DECLARE
    i INT;
BEGIN
    FOR i IN 1..loop_count LOOP
            INSERT INTO index_test (user_name, email, reg_date)
            VALUES (
                       'user_' || i,
                       'user_' || i || '@example.com',
                       NOW() - ((FLOOR(RANDOM() * 1000))::INT * INTERVAL '1 day')
                   );
        END LOOP;
END;
$$;

SELECT insert_bulk_data(100000);

SELECT * FROM index_test;
COMMIT;


-- 인덱스 생성 이전에 where 절로 검색하는 문장
SELECT * FROM index_test WHERE email = 'user_99999@example.com';
EXPLAIN ANALYSE SELECT * FROM index_test WHERE email = 'user_99999@example.com';
-- Planning Time: 0.154 ms
-- Execution Time: 0.073 ms

-- 인덱스 생성 및 Index로 검색하는 문장
DROP INDEX IF EXISTS idx_email;
CREATE INDEX idx_email ON index_test(email);
EXPLAIN ANALYSE SELECT * FROM index_test WHERE email = 'user_99999@example.com';
-- Planning Time: 0.885 ms
-- Execution Time: 0.046 ms

-- LIKE 절 INDEX 적용해보기

-- 확장 설치 (DB마다 최초 1회만 실행)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 트라이그램 인덱스 생성
DROP INDEX IF EXISTS idx_email_trgm_gin;
CREATE INDEX idx_email_trgm_gin ON index_test USING gin (email gin_trgm_ops);

-- 인덱스 비활용 (양쪽 와일드카드만 있을 경우)
EXPLAIN ANALYSE SELECT * FROM index_test WHERE email LIKE '%user_123%';



----------------------------------------------------------------------------------------------
