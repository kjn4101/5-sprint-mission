------------------------------ 개론 -----------------------------------

-- 주석 다는 법 : 1) -- 한줄, 2) /* 내용 /
-- 주석입니다.

-- 현재 존재하는 데이터베이스를 확인
SELECT datname
FROM pg_database;

-- 현재 데이터베이스의 모든 스키마 확인
SELECT schema_name
FROM information_schema.schemata;

-- 현재 데이터베이스에서 모든 테이블 조회
SELECT
FROM information_schema.tables;

-- 현재 데이터베이스에서 모든 컬럼
SELECT *
FROM information_schema.columns;

-- 현재 데이터베이스에서 모든 제약사항 조회
SELECT *
FROM information_schema.constraint_table_usage;
SELECT *
FROM information_schema.constraint_column_usage;

-- 세션 단위로 스키마 우선순위 변경
SET search_path TO codeit;


---------------------------------- SELECT --------------------------------------
-- 실습전에 해야 할 일
-- SAMPLE.sql 명령어 스크립트로 실행할것! -> 깨졌으면 다시 실행하면 된다.
SET search_path TO codeit;
COMMIT;


-- 테이블의 컬럼정보를 출력하는 명령
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'codeit' -- 스키마명
  AND table_name = 'employee';
-- table 이름


-- 기본적인 SELECT문 - employee
SELECT *
FROM employee; -- SET search_path TO codeit을 사용해야 가능한 문법 ★★★★
SELECT *
FROM codeit.employee;
-- codeit 스키마에서 직접 조회하는 방법
-- * : 와일드카드, 모든 컬럼을 조회하는 키워드, 급할때만 활용 권장!

SELECT *
FROM codeit.employee;
SELECT *
FROM codeit.department;
SELECT *
FROM codeit.job;
SELECT *
FROM codeit.location;
SELECT *
FROM codeit.national;
SELECT *
FROM codeit.sal_grade;


-- 컬럼명을 지정하여 조회하는 방법
-- -> 일반적으로 DB 값을 조회하기 위해선 컬럼명을 명시하는 것이 정석
--    이유 : 컬럼의 순서 보장, 불필요한 데이터 조회시 메모리, 네트워크 시간 낭비


SELECT emp_id,
       emp_name,
       emp_no,
       email,
       phone,
       dept_code,
       job_code,
       sal_level,
       salary,
       bonus,
       manager_id,
       hire_date,
       ent_date,
       ent_yn
FROM employee;

-- 필요한 데이터만 조회
SELECT emp_id, emp_name, emp_no, email, phone
FROM employee;
SELECT employee.emp_id, employee.emp_name, employee.emp_no, employee.email, employee.phone
FROM employee;
-- 컬럼에 테이블 명 적어서 기술하는 법

-- 와일드 카드와 컬럼명을 결합하는 문법
SELECT emp_name, employee.*
FROM employee;
-- 컬럼에 테이블 명 적어서 기술하는 법

-- AS 문법 : table이나 컬럼명의 별칭을 만드는 방법, 향후 Java와 연동시 핵신적으로 사용될 문법 ★★★★★
-- 컬럼명 AS "별칭", AS 별칭, 별칭
SELECT emp_name
FROM employee;
SELECT emp_name AS "이름 제목"
FROM employee;
SELECT emp_name AS name
FROM employee; -- 권장 문법, 띄어쓰기는 _ 표시 (ex 이름_제목)
SELECT emp_name 이름
FROM employee;

-- Table 별칭 다는 방법
SELECT emp_name, e.*
FROM employee AS e;
SELECT emp_name, e.*
FROM employee e; -- 권장 문법
SELECT emp_name, "Employee Table".*
FROM employee AS "Employee Table";

-- DISTINCT : 중복을 제거하는 문법 ★★★★★
SELECT dept_code
FROM employee;
SELECT DISTINCT dept_code
FROM employee
ORDER BY 1;

-- DISTINCT를 두 컬럼 조합에 적용하는 방법
SELECT dept_code, job_code
FROM employee;
SELECT DISTINCT dept_code, job_code
FROM employee;
SELECT DISTINCT (dept_code, job_code)
FROM employee;

-- 중복되지 않은 컬럼 수 구하는 방법 (count)
SELECT COUNT(DISTINCT dept_code) AS 부서_전체_코드_갯수
FROM employee;
SELECT COUNT(DISTINCT (dept_code, job_code)) AS 부서_잡코드_전체_코드_갯수
FROM employee;

-- 비교연산자(Where절)
-- WHERE [컬럼명|리터럴] [비교연산자 = > < <= >=] [컬럼명|리터럴] {AND OR && ||} {다항식...}
-- ※주의 : 연산자 별로 우선순위가 존재함으로 괄호()사용을 적극 추천!
--  = : 동등연산자(값이 같은가?) (Java에서 ==)
-- !=, <>, ^= : 값이 같지 않음을 비교하는 연산자
-- > < <= >= : 값의 대소, 이상/초과를 표현하는 연산자

-- BETWEEN : 범위비교용 연산자
-- ex) WHERE between[범위1] and [범위2]
--     -> where A >= B && A <= C -> where between B and C

-- LIKE / NOT LIKE : 문자열 일부 또는 완전 일치하는지 확인하는 연산자 _나 %가 와일드 카드로 사용

-- IN / NOT IN : 특정 집합의 값이 포함되어 있는지 확인하는 연산자
--   ex) [VALUE] IN (특정 집합값 n1, n2 ...)
--   ex) 컬럼명 IN 10, 20, 30, 40 ...

-- IS NULL, IS NOT NULL : 값이 NULL인지 확인하는 연산자
-- ※주의 NULL은 '='로 비교 불가하다!!
-- AND, OR : 논리 연산자
-- NOT : 부정연산

-- PK(ID) 기준으로 조회하는 방법, WHERE절에서 가장 많이 쓰는 문법
SELECT *
FROM employee
WHERE emp_id = 200;
SELECT *
FROM employee
WHERE emp_id = '200'; -- DB에서는 Type이 느슨하게 연결되어 문자열 숫자 비교 가능!
SELECT *
FROM employee
WHERE emp_id = '200'::INT;
-- :: cast 문법


-- 직원중 월급이 300만원 이상인 사람
SELECT emp_name, salary, dept_code
FROM employee
WHERE salary >= 3000000;
SELECT emp_name, salary, dept_code
FROM employee
WHERE salary >= 3_000_000; -- 자리수 표현 가능
SELECT COUNT(*)
FROM employee
WHERE salary >= 3000000;
-- 인원수 구하는 방법


-- 직원 중 월급이 300만원 이상이 아닌 사람 표현1 - not 사용
SELECT emp_name, salary, dept_code
FROM employee
WHERE NOT salary >= 3000000;

-- 직원 중 월급이 300만원 이상이 아닌 사람 표현2 - 논리식 수정
SELECT emp_name, salary, dept_code
FROM employee
WHERE salary < 3000000;


-- 직원중 부서가 D1인 사람
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code = 'D1';
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code = 'd1'; -- 대소문자 민감함!
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code ILIKE 'd1';
-- 대소문자 구별 안함


-- 직원중에 부서가 D5 문자열 기준 높은 순의 사람 -> 문자열 대소비교 예제 ★★★★★
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code > 'D5';
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code <= 'D5';

-- 직원중 부서가 D1이면서 월급이 300만원 이상인 사람 -> AND절 연습예제
SELECT emp_name, salary, dept_code
FROM employee
WHERE dept_code = 'D1'
  AND salary >= 3000000;

-- 직원 중 월급이 200만원 이상이면서 300만원 이하인 사람 (AND정, BETWEEN 문법)
SELECT emp_name, salary, dept_code
FROM employee
WHERE salary >= 2000000
  AND salary <= 3000000;
SELECT emp_name, salary, dept_code
FROM employee
WHERE salary BETWEEN 2000000 AND 3000000;


-- 직원의 입사일 2015-01-01보다 빠르거나 느린사람 -> 날짜간의 대소비교도 가능!! ★★★★★
SELECT emp_name, hire_date
FROM employee
WHERE hire_date < '2015-01-01'; -- 2015년 이전
SELECT emp_name, hire_date
FROM employee
WHERE hire_date >= '2015-01-01'; -- 2015년 이후
SELECT emp_name, hire_date
FROM employee
WHERE hire_date < NOW();


-- LIKE : 문자열 패턴을 조회하는 키워드, 검색기능 만들때 자주 활용 ★★★★★
-- 장점 : 검색하기 가장 편하다. ex) 게시글 제목, 내용 검색 기능들
-- 단점 : 성능에는 좋지 않은 문장! (3차원 복잡도) -> 개선 방법은 있다.

-- 사용법
-- WHERE '컬럼명' LIKE '__문자%' : 앞의 _ 두글자로 시작로 시작하고 가운데 '문자'라는 키워드가 들어있는 아무 문자열 탐색
-- % : 와일드 카드로 문자열 0개 이상의 모든 문자 대처
-- '김%' : 김으로 시작하는 모든 문자열 ex) 김길동, 김길순, 김순 김
-- '%동' : 동으로 끝나는 모든 문자열 ex)홍길동, 박길동, 길동, 동
-- '%길%' : 중간에 길이 들어가는 모든 문자열 ex) 홍길동, 길동, 홍길, 길

-- (언더바) : 한개의 문자를 대처 가능하다.
-- '홍__' : 홍으로 시작하는 3글자
-- '_길_' : 길이 중간에 들어가는 3글자
-- '__동' : 동으로 끝나는 3글자

SELECT emp_name
FROM employee
WHERE emp_name LIKE '이%'; -- 이씨 성을 가진 사람
SELECT emp_name
FROM employee
WHERE emp_name LIKE '이__'; -- 이씨 성을 가진 3글자 이름인 사람
SELECT emp_name
FROM employee
WHERE emp_name LIKE '이_'; -- 이씨 성을 가진 2글자 이름인 사람
SELECT emp_name
FROM employee
WHERE emp_name LIKE '이___';
-- 이씨 성을 가진 4글자 이름인 사람

-- 휴대전화 번호가 011로 시작하는 사람
SELECT emp_name, phone
FROM employee
WHERE phone LIKE '011%';
SELECT emp_name, phone
FROM employee
WHERE phone LIKE '017%';

-- 휴대전화 번호가 10자리인 사람
SELECT emp_name, phone
FROM employee
WHERE phone LIKE '01________';
SELECT emp_name, phone
FROM employee
WHERE phone LIKE '__________';
SELECT emp_name, phone, LENGTH(phone)
FROM employee
WHERE LENGTH(phone) = 10;

-- 이름에 동이 들어가는 사람
SELECT emp_name
FROM employee
WHERE emp_name LIKE '%동%'; -- 가장 보편적인 검색어 처리
SELECT emp_name
FROM employee
WHERE emp_name LIKE '_동_';
SELECT emp_name
FROM employee
WHERE emp_name LIKE '_동%';
SELECT emp_name
FROM employee
WHERE emp_name LIKE '%동_';


-- email에서 '_' 앞에 글자가 3글자인 사람 -> '_' escape문으로 대체해서 활용가능
SELECT emp_name, email
FROM employee
WHERE email LIKE '____%'; -- 실패, 4글자 이상인 모든사람
SELECT emp_name, email
FROM employee
WHERE email LIKE '___#_%' ESCAPE '#';
-- 성공, 4글자 이상인 모든사람
-- escape로 정한 '#' 뒤에 1글자는 진짜 '_'로 인식하는 방법, %도 같은 방법으로 활용 가능


-- NULL : 데이터가 없는 상태, 0과는 다른 개념 -> 0이라는 값이 존재, 존재와 없음을 비교하는 개념
-- NULL 관련 명령어 ★★★★★
-- WHERE '컬럼명' IS NULL; -- 컬럼이 널인가? 체크하는 문법
-- WHERE '컬럼명' IS NOT NULL;
-- WHERE '컬럼명' = NULL (X); -- 안되는 문법

-- SELECT emp_name, bonus FROM employee WHERE bonus = NULL; -- 문법은 먹히나 의도와 다름!
SELECT emp_name, bonus
FROM employee
WHERE bonus IS NULL; -- 정상
SELECT emp_name, bonus
FROM employee
WHERE bonus IS NOT NULL; -- 정상
SELECT emp_name, bonus
FROM employee
WHERE NOT bonus IS NULL;
-- 정상, NOT 활용

-- 컬럼에 NULL이 있지만 특정 연상을 수행하고 싶은 경우 -> 지급된 월급과 보너스가 포함된 지급액
SELECT emp_name, salary AS 월급
FROM employee;
SELECT emp_name, salary AS 월급, salary + (salary * bonus) AS 지급금액
FROM employee;
-- NULL이 묻으면 NULL이 된다.
--> NULL 체크 및 변환을 수행하지 않아 생긴 문제, NVL 처리가 필요하다.

-- NULL 체크 문법 -> DB마다 방언이 존재하나 표준 함수도 존재
-- coalesce (ANSI 표준) : NULL을 특정값으로 변환하는 함수
-- coalesce(체크할 컬럼명, null일때 default값)
SELECT emp_name, salary AS 월급, bonus, salary + (salary * COALESCE(bonus, 0)) AS 지급금액
FROM employee;


-- 다중 값 비교하기
-- 다중 값(집합)을 비교하는 방법
-- in, not in : 다중값을 비교하는 키워드
-- where '컬럼명' in (값1, 값2, 값3, ... 값N)
--  -> 향후 괄호자리가 sub query로 대체되거나 프로그래밍의 반복문으로 대체되서 활용된다.

-- 부서 D5, D6, D7, D8인 사람을 검색하는 방법
-- -> 4개 정도 되면 or절로 가능하긴하나 문법이 너무 길어져서 회피가 필요
SELECT emp_name, dept_code
FROM employee
WHERE dept_code = 'D5'
   OR dept_code = 'D6'
   OR dept_code = 'D7'
   OR dept_code = 'D8';

-- IN을 통한 단축 표현 1. 리터럴 활용 문법
SELECT emp_name, dept_code
FROM employee
WHERE dept_code IN ('D5', 'D6', 'D7', 'D8')
ORDER BY 2;

-- IN을 통한 단축 표현 2. Sub Query 활용 문법
-- Sub Query는 주로 괄호안에서 완성
SELECT dept_id
FROM department
WHERE dept_id >= 'D5'
  AND dept_id <= 'D8';
SELECT dept_id
FROM department
WHERE dept_id BETWEEN 'D5' AND 'D8';
SELECT emp_name, dept_code
FROM employee
WHERE dept_code IN (SELECT dept_id FROM department WHERE dept_id BETWEEN 'D5' AND 'D8')
ORDER BY 2;

-- 연산자 우선순위 ★★★★★
-- ※ 연산자 우선순위 외우면 좋지만, 중요한건 (괄호) 사용!

-- 연산자 우선순위로 문제가 되는 예시
-- 부서가 D5, D6, D7, D8이면서 정이나 전씨로 시작하는 사람 찾기
SELECT emp_name, dept_code
FROM employee
WHERE dept_code IN ('D5', 'D6', 'D7', 'D8') AND emp_name LIKE '정%'
   OR emp_name LIKE '전%';
-- 틀린값!! and, or 우선순위로 잘못된 D1부서의 전지연 검색됨

SELECT emp_name, dept_code
FROM employee
WHERE dept_code IN ('D5', 'D6', 'D7', 'D8')
  AND (emp_name LIKE '정%' OR emp_name LIKE '전%');
-- 정답! or절의 괄호가 필요하다. -> 강사가 추천하는 and or 패턴중 하나.

-- ORDER BY절 ★★★★★ ★★★★★
-- - select문에서 나온 결과값을 정렬하는 기능, 명령어 우선순위가 두번째로 낮음. (LIMIT절이 제일 낮음)
-- ex) SELECT * FROM [Table 이름] ... ORDER BY [컬럼명] or 숫자(컬럼 순서), 별칭 [정렬순서 ASC or DESC] : default
-- 정렬 가능한 범위 : 숫자, 문자, 날짜 - 다된다!

-- 문자
SELECT emp_name
FROM employee
ORDER BY emp_name; -- 사전순 정렬, DEASULT는 ASC(오름차순)
SELECT emp_name
FROM employee
ORDER BY emp_name ASC; -- 명시적 표기 방법
SELECT emp_name
FROM employee
ORDER BY emp_name DESC;
-- 내림차순 정렬

-- 숫자
SELECT emp_name, salary
FROM employee
ORDER BY salary ASC;
SELECT emp_name, salary
FROM employee
ORDER BY salary DESC;

-- 날짜 기준 정렬
SELECT emp_name, hire_date
FROM employee
ORDER BY hire_date ASC;
SELECT emp_name, hire_date
FROM employee
ORDER BY hire_date DESC;

-- 컬럼명이 아닌 출력순서로 정하는 방법 (index), ※권장하지 않음
--        1       2
SELECT emp_id, emp_name
FROM employee
ORDER BY 1 DESC; -- emp_id 내린차순
SELECT emp_id, emp_name
FROM employee
ORDER BY 2 DESC;
-- emp_name 내린차순

-- 컬럼명이 아닌 별명으로 정렬하기 -> 함수나 복잡한 컬럼명을 별명으로 축약가능
SELECT emp_id, emp_name AS 이름
FROM employee
ORDER BY 이름 DESC;
-- 성별 조회하기
SELECT emp_name, emp_no, SUBSTRING(emp_no FROM 8 FOR 1)
FROM employee
ORDER BY SUBSTRING(emp_no FROM 8 FOR 1);

SELECT emp_name, emp_no, SUBSTRING(emp_no FROM 8 FOR 1) AS 성별
FROM employee
ORDER BY 성별;

-- 다중 정렬 방법, 순서가 우선순위가 된다. ★★★★★
SELECT * FROM employee ORDER BY dept_code, job_code, salary;
SELECT * FROM employee ORDER BY dept_code ASC, job_code DESC, salary DESC;

-- LIMIT절 : 조회 시 행의 LIMIT(제한)을 두고 값을 자르는 기법 ★★★★★
--           페이지를 구성하는 경우 특정 순서로 행을 자를 때 사용한다. ex) TOP 5, 최근 게시글 10개
-- ※ 주의점 : 정렬이 되지 않은 상태에서 LIMIT만 쓰면 의미 없음. 반드시 ORDER BY와 함께 사용 권장!

SELECT * FROM employee LIMIT 5; -- 현재 PK 기준으로 정렬된 상태로 limit가 먹힘, 권장 X
SELECT * FROM employee ORDER BY emp_id LIMIT 5; -- 명시적으로 정렬 사용 문법

-- 월급이 많은 5명
SELECT * FROM employee ORDER BY salary DESC LIMIT 5;

-- OFFSET절 : LIMIT절과 결합하여 일정 순서를 건너뛰고 조회 ★★★★★
-- -> 페이징 처리 : ORDER BY + LIMIT + OFFSET 조합 활용
SELECT * FROM employee ORDER BY emp_id OFFSET 5; -- OFFSET만 단독 사용도 가능

-- emp_id 순으로 0개를 건너뛰고, 5개를 가져오는 방법 = page 1
SELECT * FROM employee ORDER BY emp_id LIMIT 5 OFFSET 0;

-- emp_id 순으로 5개를 건너뛰고, 5개를 가져오는 방법 = page 2
SELECT * FROM employee ORDER BY emp_id LIMIT 5 OFFSET 5;

-- emp_id 순으로 10개를 건너뛰고, 5개를 가져오는 방법 = page 3
SELECT * FROM employee ORDER BY emp_id LIMIT 5 OFFSET 10;

-- 월급이 많은 5~10위
SELECT * FROM employee ORDER BY salary DESC LIMIT 5 OFFSET 0; -- 1 ~ 5
SELECT * FROM employee ORDER BY salary DESC LIMIT 5 OFFSET 5; -- 6 ~ 10

