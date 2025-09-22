---------------------------------  JOIN  ---------------------------------------
SET search_path TO codeit;

-- JOIN문 ★★★★★
-- - 두 테이블 간에 Null이 없는 값만 테이블로 통합하는 방법 (Inner join)
-- - 두 테이블 간에 Null이 있어도 통합이 가능한 방법 (Outer join)
-- - 자기 자신과도 Join이 가능하다. (Self join)

-- ANSI식 (American National Standards Institute) 권장(수업) ★★★★★
SELECT
    emp_name, dept_title
FROM employee
    JOIN department ON dept_id = dept_code;

-- ON 이후로 괄호로 묶어도 됩니다.
SELECT
    emp_name, dept_title
FROM employee
    JOIN department ON (dept_id = dept_code);

-- 강사가 추천하는 JOIN문 쓰는 문장 ★★★★★
-- -> 별칭사용 문법, 별칭을 사용할 일도 앖고, USING 문법을 못 외울 때 한 패턴으로 정의 가능
SELECT
    e.emp_name, e.dept_code, d.dept_title
FROM employee e
    JOIN department d ON (e.dept_code = d.dept_id)


-- USING 문법 : JOIN 되는 키값의 이름이 같을때 사용 가능, 무조건 괄호 필요!
SELECT
    emp_name, job_code, job_name
FROM employee
    JOIN job USING (job_code);

-- 강사가 추천하는 문장으로 다시 USING문을 풀어 써보기 ★★★★★
SELECT
    e.emp_name, e.job_code, j.job_name
FROM employee e
    JOIN job j ON e.job_code = j.job_code;

-- 오라클식 JOIN문(수업에서는 권장하진 않음!)
SELECT
    emp_name, dept_code, dept_title
FROM employee, department
WHERE dept_id = dept_code;

-- JOIN 중 컬러명이 겹칠때 해결방법 = 별칭 사용(AS)
-- 1. table 이름으로 컬럼명 분리
SELECT
    employee.emp_name, employee.dept_code, department.dept_title
FROM employee
    JOIN department ON dept_code = dept_id;

-- 2. 별칭 사용(권장)
-- - 별칭 네이밍 방법 : 보통 테이블의 앞글자로 별칭을 만들고, 겹치는 경우는 e1, e2로 분리해서 사용.
SELECT
    e.emp_name, e.dept_code, d.dept_title
FROM employee e
    JOIN department d ON e.dept_code = d.dept_id;

-- 별칭 사용시의 와일드카드 활용법
SELECT
    e.emp_name, d.*
FROM employee e
        JOIN department d ON e.dept_code = d.dept_id;

-- 연습하기
-- employee + sal_grade
SELECT
    e.emp_name, s.sal_level, s.max_sal, s.min_sal
FROM employee e
    JOIN sal_grade s ON e.sal_level = s.sal_level;

-- location + national
SELECT
    l.local_code, l.local_name, n.national_name
FROM location l
    JOIN national n ON l.national_code = n.national_code;


-- Inner JOIN : INNER가 있거나 일반적인 Join문 특별한 키워드가 없는 Join 문장
--              외래키를 사용하는데 null을 허용하지 않고 외래키와 주키가 완벽하게 일치하면 사용하면 된다.
--              -> 반대로 null이 허용되는 경우에 null이 포함된 값을 조회하는 경우는 Outer JOIN을 사용해야한다.
--              ex) 게시글의 글쓴 사람, 결제 시의 물품 번호

-- Outer JOIN : LEFT, RIGHT 키워드가 포함된 JOIN 문으로 키가 서로 일치 않는 경우에도 조회 가능하다.
--              한쪽 테이블 기준으로 결측 값(null)이 있어도 join이 가능함
--                 ex) 주문테이블과 카드결제 테이블을 같이 조회할때, 카드결제가 아니어도 같이 조회할 때
--                     게시글과 댓글을 같이 조회할때 댓글이 없어도 같이 조회할때
--              ※ 주로 잘못된 설계나 결측(null)값을 비정상적으로 조회할때 사용된다.
--              -> Inner join 대비 성능 저하가 크게 발생하나 어쩔수 없이 사용해야한다.

-- left join(ANSI) - 왼쪽 기준으로 오른쪽 null 값이 조회되는 경우
-- 키워드 : LEFT OUTER JOIN OR LEFT JOIN
SELECT
    e.emp_name, e.dept_code, d.dept_title
FROM employee e
--     LEFT OUTER JOIN department d ON e.dept_code = d.dept_id;
    LEFT JOIN department d ON e.dept_code = d.dept_id ;

-- right join(ANSI) - 오른쪽 기준으로 왼쪽 null 값이 조회되는 경우
-- 키워드 : right outer join or right join
SELECT
    e.emp_name, e.dept_code, d.dept_title
FROM employee e
--     RIGHT OUTER JOIN department d ON e.dept_code = d.dept_id;
    RIGHT JOIN department d ON e.dept_code = d.dept_id ;

-- JOIN문 WHERE절 조합

-- ORACLE 문법 -> 기본적으로 WHERE절이 있으므로 AND, OR를 결합해서 사용해야 한다. <- 불편!
-- 총무부이거나 기술지원부인 사람을 찾아라!
SELECT
    e.emp_name, d.dept_title
FROM employee e, department d
WHERE e.dept_code = d.dept_id
    AND (d.dept_title = '총무부' OR d.dept_title = '기술지원부');

-- ANSI 문법
SELECT
    e.emp_name, d.dept_title
FROM employee e
    JOIN department d ON e.dept_code = d.dept_id
    WHERE d.dept_title = '총무부' OR d.dept_title = '기술지원부';

-- ANSI 문법의 금기 (사용하지 말것)
-- ON이 사실상 WHERE절이므로 ON에서 해도 된다.
SELECT
    e.emp_name, d.dept_title
FROM employee e
    JOIN department d ON e.dept_code = d.dept_id
    AND (d.dept_title = '총무부' OR d.dept_title = '기술지원부');

-- CROSS JOIN : Cartesian(카테시안) 곱, ROW간 결합될수 있는 전체 경우 수를 출력
--              -> 사용하는 경우는 드물다.
SELECT
    emp_name, dept_title
FROM employee
    CROSS JOIN department;

-- self join : 테이블 하나로 join을 활용하는 경우, 자기 자신을 참조할 일이 생길 때 사용.(자신이 트리구조로 구성 될 때)
-- 사례: 대댓글, Q&A 글처럼 한 테이블 안에 자식-부모 관계가 성립되는 트리구조
-- 직원의 관리자를 출력해라!

SELECT
    e.emp_id, e.emp_name, e.manager_id, m.emp_name AS 관리자
FROM employee e
    JOIN employee m ON e.manager_id::INT = m.emp_id;

-- 관리자가 없어도 출력이 필요할 땐 LEFT JOIN 활용!
SELECT
    e.emp_id, e.emp_name, e.manager_id, m.emp_name AS 관리자
FROM employee e
    LEFT JOIN employee m ON e.manager_id::INT = m.emp_id;

-- 다중 join : 3개 이상의 테이블을 결합할때 사용 ★★★★★
-- employee, job, department, location
-- 직원 id, 이름, 직위이름, 부서이름, 부서의 국가 위치, 국가 이름, Local 이름
-- 주의점 : join 순서가 존재한다!

SELECT * FROM employee;
SELECT * FROM job;
SELECT * FROM department;
SELECT * FROM location;

SELECT
    e.emp_id, e.emp_name, j.job_name, d.dept_title, l.local_code, l.local_name
FROM employee e
    JOIN job j ON e.job_code = j.job_code
    JOIN department d ON e.dept_code = d.dept_id
    JOIN location l ON d.location_id = l.local_code;

SELECT
    e.*, d.*, j.*, l.*
FROM employee e
    LEFT JOIN job j ON e.job_code = j.job_code
    LEFT JOIN department d ON e.dept_code = d.dept_id
    LEFT JOIN location l ON d.location_id = l.local_code;

-- oracle문법 다중 조인
SELECT e.emp_id,
       e.emp_name,
       j.job_code,
       d.dept_title,
       l.local_name,
       l.national_code
FROM employee e,
     job j,
     department d,
     location l
WHERE e.job_code = j.job_code
  AND e.dept_code = d.dept_id
  AND d.location_id = l.local_code;


-- NON_EQUI JOIN : 비등가 조인, 일치하는 범위에 값을 가져오는 기능
SELECT
    e.emp_name, e.salary, s.sal_level
FROM employee e
    jOIN sal_grade s ON (e.salary BETWEEN 3000000 AND 3500000);

SELECT * FROM sal_grade;
SELECT * FROM employee;

-- DB에서 성능적으로 고려 할 순서 (연산이 오래 걸리는 시간)
-- 1. 네트워크 전송시간 -> 여러 네트워크를 통해 DB 사용함으로써 전송시간에 딜레이 생긴다.
-- 2. HDD(하드디스크, SSD)에서 읽어오는 시간 -> 캐시를 통해 빠르게 접근하기는 하지만 느리다!
-- 3. 쿼리 처리 시간 (복잡한 함수나 로직으로 인해 처리가 지연되는 시간)


-- JOIN문을 사용할때 주의 해야할 점 = 이유? 잘못 사용하면 과도한 JOIN으로 인해 성능저하 발생!
-- 1. index 생성된 값을 키값으로 사용 할 것 (PK는 index를 자동 생성함으로 PK로 join 권장)
-- 2. 결합하는 값은 주로 외래키 = 주키로 사용할 것 ★★★★★
--    -> DB 설계할때 sequence number 통해 주키를 할당하고 해당 키로 Join을 하면 속도 측면에서 문제가 거의 없다.
-- 3. Outer Join은 Inner Join 성능저하 유발한다. 다중으로 중복해서 사용하지 말길 권장, 6개 정도가 권장
-- ※ Join을 통해 성능 저하가 발생하는 경우 꼬인 코드를 풀어 성능 개선을 하거나 적절한 반정규화를 실행해야한다.
---------------------------------- JOIN 끝 ------------------------------------




