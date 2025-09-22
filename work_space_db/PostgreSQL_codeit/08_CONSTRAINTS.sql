--------------------------------- 제약조건 시작 ------------------------------------

-- DB의 제약조건(Constraints) ★★★★★
-- -> 제약을 쓰지 않으면 무결성을 보장할 수 없다.

-- NOT NULL : 특정 컬럼에 NULL을 허용하지 않는 제약
-- UNIQUE (U) : 특정 컬럼에 중복값을 허용하지 않는 제약 (NULL 처리 정책은 DB마다 다르지만, PostgreSQL은 NULL 여러 개 허용)
-- PRIMARY KEY (PK) : 유일성+NOT NULL을 만족하는 주키(인덱스 자동 부여)
-- FOREIGN KEY (FK) : 외래키. 다른 테이블의 키를 참조하여 관계를 강제
-- CHECK : 특정 컬럼의 입력값을 제한
-- DEFAULT : 입력값이 없을 때 기본값으로 채움
-- SERIAL / IDENTITY로 대체 키가 자동으로 증감하는 제약

-- 제약사항 활용
-- 1) 컬럼 레벨 제약
-- 2) 테이블 레벨 제약


SET search_path TO codeit;

-- 사용자가 가진 제약 확인 (information_schema)
SELECT * FROM information_schema.table_constraints;
SELECT * FROM information_schema.table_constraints WHERE table_schema = 'codeit';

-- 테이블 구조 확인
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema='codeit' AND table_name='employee';

-- 제약이 없는 기본 테이블
DROP TABLE IF EXISTS tbl_user;
CREATE TABLE tbl_user(
     user_no BIGINT, -- 컬럼 레벨 제약을 거는 곳 : NOT NULL, UNIQUE, AUTO_INCREMENT, PRIMARY KEY
     user_id VARCHAR(20),
     user_pw VARCHAR(20),
     user_name VARCHAR(20),
     user_age INT,
     user_recv_email BOOLEAN
    -- 테이블 레벨로 제약을 거는 곳
    -- unique, primary key, 외래키 .. 2개 이상의 컬럼이 필요한 제약
);

INSERT INTO tbl_user VALUES (NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO tbl_user VALUES (1,'test1','1234','홍길동',31,TRUE);
INSERT INTO tbl_user VALUES (1,'test1','1234','홍길동',31,FALSE);
INSERT INTO tbl_user VALUES (2,'test1','4321','박길동',31,TRUE);

SELECT * FROM tbl_user;

-- NOT NULL 제약 : 데이터 값의 null을 허용하지 않는 제약
DROP TABLE IF EXISTS tbl_cons_not_null;
CREATE TABLE tbl_cons_not_null(
      user_no BIGINT NOT NULL, -- not null은 컬럼레벨 제약만 가능
      user_id VARCHAR(20) NOT NULL,
      user_pw VARCHAR(20),
      user_name VARCHAR(20),
      user_age INT
    -- 테이블 레벨에서 NOT NULL 선언은 불가
);

-- 다음 두 문장은 에러( NOT NULL 위반 )
INSERT INTO tbl_cons_not_null VALUES (NULL, NULL, NULL, NULL, NULL);
INSERT INTO tbl_cons_not_null VALUES (1, NULL,'1234','홍길동',31);

INSERT INTO tbl_cons_not_null VALUES (1,'test1','1234','홍길동',31);
INSERT INTO tbl_cons_not_null VALUES (2,'test1','4321','박길동',31);

SELECT * FROM tbl_cons_not_null;

-- UNIQUE : 컬럼값 중에서 중복을 허용하지 않는 제약
--          컬럼, 테이블 레벨에서 제약 둘다 가능!
DROP TABLE IF EXISTS tbl_cons_unique;
CREATE TABLE tbl_cons_unique(
    user_no BIGINT NOT NULL UNIQUE, -- not null + unique 제약을 한번에 거는 방법
    user_id VARCHAR(20) UNIQUE,
    user_pw VARCHAR(20),
    user_name VARCHAR(20),
    user_age INT,
    -- table 레벨에서도 UNIQUE 걸린다!
    UNIQUE (user_pw),
    UNIQUE (user_name, user_age) -- 이름+나이 조합 유일
);

INSERT INTO tbl_cons_unique VALUES(1, 'test', '1234', '홍길동', 31);
INSERT INTO tbl_cons_unique VALUES(1, 'test', '1234', '홍길동', 31); -- user_no가 중복
INSERT INTO tbl_cons_unique VALUES(2, 'test2', '4321', '홍길동', 32); -- OK
INSERT INTO tbl_cons_unique VALUES(3, 'test3', '43212', '김길동', 32);
INSERT INTO tbl_cons_unique VALUES(4, 'test4', '432123', '김길동', 32); -- NG, UNIQUE(user_name, user_age)
INSERT INTO tbl_cons_unique VALUES(4, 'test4', '432123', '김길동', 33); -- OK

SELECT * FROM tbl_cons_unique;

-- primary key(주키, 기본키, PK) ★★★★★★★
-- - table의 유일성, 최소성 원칙이 지켜진 id(식별자)
-- - 중복값이 없고, NULL이 없는 제약이 발생 (unique + not null)
-- - 테이블당 1개만 설정이 가능하고, 테이블, 컬럼 레벨 둘다 제약 가능
-- - MySQL에서는 가급적 PK를 통해 검색 및 삭제, 수정을 진행해야한다. -> 가장 빠르다!
DROP TABLE IF EXISTS tbl_cons_primary_key;
CREATE TABLE tbl_cons_primary_key(
     user_no BIGINT PRIMARY KEY, -- 컬럼 레벨에서 PK 제약
     user_id VARCHAR(20),
     user_pw VARCHAR(20),
     user_name VARCHAR(20),
     user_age INT
    -- table 레벨에서도 primary_key 걸린다
    -- PRIMARY KEY(user_no)  -- 테이블 레벨로도 가능 (중복 선언 금지)
);
INSERT INTO tbl_cons_primary_key VALUES(null,null,null,null,null); -- NG
INSERT INTO tbl_cons_primary_key VALUES(1, null,'1234','홍길동',31);
INSERT INTO tbl_cons_primary_key VALUES(1,'test1','1234','홍길동',31); -- NG
INSERT INTO tbl_cons_primary_key VALUES(2,'test1','4321','박길동',31);

SELECT * FROM tbl_cons_primary_key;

-- PK 조회 및 실행계획
SELECT * FROM tbl_cons_primary_key WHERE user_no = 1;
EXPLAIN SELECT * FROM tbl_cons_primary_key WHERE user_no = 1; -- index 탄다.
EXPLAIN SELECT * FROM tbl_cons_primary_key WHERE user_name = '홍길동'; -- index 안탄다.

-- 복합 PRIMARY KEY (FK 조합 등에서 사용)
-- 주키를 2개 이상으로 적용해보기 -> 일반적인 상황은 아니나 필요할때가 있다!!
-- 주키 2개 이상으로 설계하는 패턴 : FK 2개로 table을 구성할때, ex) 즐겨찾기
DROP TABLE IF EXISTS tbl_cons_primary_key2;
CREATE TABLE tbl_cons_primary_key2(
      user_no BIGINT,
      user_id VARCHAR(20),
      user_pw VARCHAR(20),
      user_name VARCHAR(20),
      user_age INT,
      PRIMARY KEY (user_no, user_id) -- 반드시 table 레벨에서만 2개의 PK 걸린다.
    -- PRIMARY KEY(user_no, user_id, user_pw) 도 가능
);

INSERT INTO tbl_cons_primary_key2 VALUES(null,null,null,null,null);
INSERT INTO tbl_cons_primary_key2 VALUES(1, null,'1234','홍길동',31);
INSERT INTO tbl_cons_primary_key2 VALUES(1,'test1','1234','홍길동',31);
INSERT INTO tbl_cons_primary_key2 VALUES(2,'test1','4321','박길동',31);

select * from tbl_cons_primary_key2;



-- FOREIGN KEY (외래키) ★★★★★
-- 다른 테이블을 참조(join)할때 다른 테이블의 참조값(PK)를 자신의 컬럼값으로 활용할때 사용한다.
-- 참조무결성을 지킬수 있다.

DROP TABLE IF EXISTS tbl_cons_foreign_key_order;
DROP TABLE IF EXISTS tbl_cons_foreign_key_user;

-- 참조될 테이블 → 참조하는 테이블 순서로 생성
-- 사용자
CREATE TABLE tbl_cons_foreign_key_user(
  user_no  BIGINT UNIQUE,
  user_id  VARCHAR(20) PRIMARY KEY,
  user_pw  VARCHAR(20),
  user_name VARCHAR(20)
);

-- 주문정보
CREATE TABLE tbl_cons_foreign_key_order(
   order_no      BIGINT PRIMARY KEY,
   product_name  VARCHAR(20) NOT NULL,
   product_price INT NOT NULL,
   user_id       VARCHAR(20), -- 보통의 외래키는 참조될 테이블의 컬럼명과 일치하는게 정석, 달라도 생성은 된다!
   FOREIGN KEY (user_id) REFERENCES tbl_cons_foreign_key_user(user_id)
);

INSERT INTO tbl_cons_foreign_key_user VALUES(1,'test1','1234','홍길동1');
INSERT INTO tbl_cons_foreign_key_user VALUES(2,'test2','1234','박길동1');
SELECT * FROM tbl_cons_foreign_key_user;

INSERT INTO tbl_cons_foreign_key_order VALUES
    (100, '아이폰16', 99, 'test1'); -- 성공, test1 아이디 존재함
INSERT INTO tbl_cons_foreign_key_order VALUES
    (101, '갤럭시z 플립7', 102, 'test3'); -- 실패, 외래키 제약으로 실패, 참조 할 테이블 test3 없어서!
SELECT * FROM tbl_cons_foreign_key_order;
SELECT * FROM tbl_cons_foreign_key_order
    JOIN tbl_cons_foreign_key_user using(user_id);

-- 삭제 테스트
DELETE FROM tbl_cons_foreign_key_user WHERE user_id = 'test1'; -- 외래키 제약으로 인해 삭제 실패! 참조무결성 위배!

-- 외래키가 포함 컬럼 삭제 시 옵션 ★
-- ON DELETE RESTRICTED (DEFAULT) : 외래키로 참조된 행 삭제 불가능 -- 제일 안전한 방법!!
-- ON DELETE SET NULL : 외래키가 삭제된 경우 해당 행에 데이터를 NULL 갱신 - 비교적 안전!!
-- ON DELETE CASCADE  : 외래키가 주키로 있는 행이 삭제 되면 참조 된 행도 자동으로 삭제 됨

-- DROP 옵션
-- CASCADE : A개체를 변경/삭제할때, A개체를 참조하고 있는 모든 개체들이 변경/삭제된다.
-- RESTRICT : A개체를 변경/삭제할때, A개체를 참조하고 있는 개체가 존재하면 A개체에 대한 명령(변경/삭제)이 취소된다.
DROP TABLE IF EXISTS tbl_cons_foreign_key_order CASCADE;
DROP TABLE IF EXISTS tbl_cons_foreign_key_user  CASCADE;

-- ON DELETE 옵션 실험
CREATE TABLE tbl_cons_foreign_key_user(
      user_no    INT UNIQUE NOT NULL,
      user_id    VARCHAR(20) PRIMARY KEY,
      user_pw    VARCHAR(20) NOT NULL,
      user_name  VARCHAR(20),
      user_age   VARCHAR(6),
      user_phone VARCHAR(20)
);

CREATE TABLE tbl_cons_foreign_key_order(
   order_no       INT PRIMARY KEY,
   product_name   VARCHAR(20) NOT NULL,
   product_price  INT NOT NULL,
   user_no        INT,
    -- FOREIGN KEY(user_no) REFERENCES tbl_cons_foreign_key_user(user_no)
    -- FOREIGN KEY(user_no) REFERENCES tbl_cons_foreign_key_user(user_no) ON DELETE SET NULL
   FOREIGN KEY(user_no) REFERENCES tbl_cons_foreign_key_user(user_no) ON DELETE CASCADE
);

INSERT INTO tbl_cons_foreign_key_user VALUES (1,'test11','1234','김길동','23','010-5633-3121');
INSERT INTO tbl_cons_foreign_key_user VALUES (2,'test22','1234','박길동','33','010-2233-3121');

INSERT INTO tbl_cons_foreign_key_order VALUES (100, '아이폰16',      999, 1);
INSERT INTO tbl_cons_foreign_key_order VALUES (101, '아이폰16 프로', 1330, 2);

SELECT * FROM tbl_cons_foreign_key_user;
SELECT * FROM tbl_cons_foreign_key_order;

DELETE FROM tbl_cons_foreign_key_user WHERE user_no = 1;
-- ON DELETE RESTRICTED(없을 때) : 제약으로 인해 child record가 삭제되지 않음
-- ON DELETE SET NULL : user_no가 null로 채워진다.
-- ON DELETE CASCADE : 참조된 id가 있었던 주문정보가 같이 삭제된다.


-- check 제약 : 정해진 범위의 값을 확인하는 제약  ★★★★
DROP TABLE IF EXISTS tbl_user_check;
CREATE TABLE tbl_user_check(
       user_name VARCHAR(30),
       age INT CHECK (age > 19 AND age < 40),     -- 범위 체크
       gender VARCHAR(2),
       CHECK (gender IN ('남','여')),             -- 값 제한
       CHECK (user_name NOT IN ('홍길동'))        -- 금지 키워드
);

INSERT INTO tbl_user_check values('홍길동', 30, '남'); -- 안됨! 이름이 홍길동이라서
INSERT INTO tbl_user_check values('박길동', 8, '남'); -- 안됨! 나이제한으로 안된다.
INSERT INTO tbl_user_check values('박길동', 30, '남자'); -- 안됨! 남,여가 아니어서
INSERT INTO tbl_user_check values('박길동', 30, '남'); -- 된다
INSERT INTO tbl_user_check values('박길순', 31, '여'); -- 된다.
SELECT * FROM tbl_user_check;


-- DEFAULT : 해당 컬럼에 입력되는 값이 없는 경우 설정된 초기 값으로 값을 저장하는 제약 ★★★★★
--			 NULL로 초기화하는 경우는 NULL로 입력됨으로 주의
DROP TABLE IF EXISTS user_default;
CREATE TABLE user_default(
     user_no INT PRIMARY KEY,
     user_id VARCHAR(20) DEFAULT 'TEST',
     user_pwd VARCHAR(20),
     user_name VARCHAR(20) DEFAULT '',   -- 공백과 NULL은 다르다
     user_age INT DEFAULT 0,
     create_date TIMESTAMP DEFAULT now()
);

INSERT INTO user_default (user_no) values(0); -- insert문에서 명시한 컬럼 외의 값들은 default로 초기화됨
INSERT INTO user_default values(1, null, null, null, null, null); -- null로 입력하면 default가 활용되지 않는다!
INSERT INTO user_default values(2, default, default, default, default, default); -- default로 입력하는 경우 default 제약이 있으면 default 값으로 초기화
SELECT * FROM user_default;

-- IDENTITY/SEQUENCE ★★★★★
-- 특정 ID 기준으로 자동으로 증감하는 값으로 시퀀스 개념
DROP TABLE IF EXISTS user_auto_increment;
CREATE TABLE user_auto_increment(
--     user_no BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, -- 긴 표현
    user_no BIGSERIAL PRIMARY KEY, -- 짧은 표현
    user_id VARCHAR(20) DEFAULT 'TEST',
    user_pwd VARCHAR(30),
    user_name VARCHAR(30) DEFAULT '-'
);

INSERT INTO user_auto_increment DEFAULT VALUES;
INSERT INTO user_auto_increment VALUES (0, NULL, NULL, NULL);
INSERT INTO user_auto_increment VALUES (100, NULL, NULL, NULL);
INSERT INTO user_auto_increment DEFAULT VALUES;                  -- 다음 값(101)

--  IDENTITY 300으로 재시작
ALTER TABLE user_auto_increment ALTER COLUMN user_no RESTART WITH 300;
ALTER SEQUENCE codeit.user_auto_increment_user_no_seq RESTART WITH 300; -- BIGSERIAL로 추가하는 문법

INSERT INTO user_auto_increment DEFAULT VALUES;  -- 300
INSERT INTO user_auto_increment DEFAULT VALUES;  -- 301
INSERT INTO user_auto_increment DEFAULT VALUES;  -- 302
SELECT * FROM user_auto_increment ORDER BY user_no;


-- 테이블 생성 시 초기화 (CREATE TABLE AS SELECT)
SELECT * FROM employee;

CREATE TABLE employee_copy AS
SELECT e.emp_id, e.emp_name, e.salary, d.dept_title, j.job_name
FROM employee e
         LEFT JOIN department d ON e.dept_code = d.dept_id
         LEFT JOIN job j USING (job_code);

SELECT e.emp_id, e.emp_name, e.salary, d.dept_title, j.job_name
FROM employee e
         LEFT JOIN department d ON e.dept_code = d.dept_id
         LEFT JOIN job j USING (job_code)
        WHERE 1=0;

SELECT * FROM employee_copy;

--------------------------------- 제약조건 끝! -----------------------------------
