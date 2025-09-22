--------------------------------- SUB QUERY ----------------------------------
SET search_path TO codeit;

-- Sub Query란? ★
-- 하나의 Query안에 또 다른 Query이 존재하는 문장
-- 형식 : Query1 WHERE (Query2)
-- ( )로 문장을 구분하고 WHERE 절 오른쪽에 위치

-- 전지현 사원의 관리자의 이름을 출력
SELECT manager_id FROM employee WHERE emp_name = '전지현';
SELECT * FROM employee WHERE emp_id = 214;

-- Join문을 통해 한번에 출력하는 방법, self 조인 성능적으로 우위!
SELECT e1.emp_name, e2.emp_name AS 매니저
FROM employee e1
         JOIN employee e2 ON e1.manager_id::INT = e2.emp_id
WHERE e1.emp_name = '전지현';

-- Sub Query로 활용하는 방법
SELECT * FROM employee WHERE emp_id = (SELECT manager_id::INT FROM employee WHERE emp_name = '전지현');

-- 평균 급여보다 많이 받는 사람 찾기 (조인으로 해결 불가)
SELECT avg(salary) FROM employee;
SELECT emp_id, emp_name, salary FROM employee WHERE salary > 2792656;
SELECT emp_id, emp_name, salary FROM employee WHERE salary > (SELECT avg(salary) FROM employee);

-- 평균 급여보다 많이 받는 사람들 이름과 부서 이름까지 출력해라 (subquery + join)
SELECT e.emp_name, d.dept_title, e.salary, (SELECT avg(salary) FROM employee)
FROM employee e
    JOIN department d ON e.dept_code = d.dept_id
WHERE salary > (SELECT avg(salary) FROM employee)
ORDER BY e.salary DESC;

-- 1. 단일행 서브쿼리 : 서브쿼리의 결과가 한개 열에 한개 행을 매칭시킬 때
--   ex) 윤은혜 사원과 동일한 급여를 받고 있는 사원 찾기

SELECT emp_name, salary FROM employee
WHERE salary = (SELECT salary FROM employee WHERE emp_name = '윤은혜')
AND emp_name != '윤은혜';

-- ex) 최대 급여와 최소 급여를 받는 사람을 동시에 찾는 방법
SELECT emp_name, salary FROM employee
WHERE salary = (SELECT max(salary) FROM employee)
OR salary = (SELECT min(salary) FROM employee);

-- 2. 다중행 서브쿼리 : 한개의 컬럼에 다수의 행을 조회하는 문구
-- ※ 주의 : 행이 다수이므로 '=' 비교 불가! IN(), NOT IN()으로 문제를 해결
--          ANY=SOME, ALL, EXIST()문 활용 가능

-- 송중기, 박나래와 같은 부서 사람
SELECT dept_code FROM employee WHERE emp_name = '송중기' OR emp_name = '박나래';
SELECT dept_code FROM employee WHERE emp_name IN ('송중기', '박나래');

-- 죽는 쿼리, 안된다!
SELECT emp_name, dept_code FROM employee
WHERE dept_code = (SELECT dept_code FROM employee WHERE emp_name IN ('송중기', '박나래'));

-- IN을 적용한 올바른 쿼리!
SELECT emp_name, dept_code FROM employee
WHERE dept_code IN (SELECT dept_code FROM employee WHERE emp_name IN ('송중기', '박나래'));

-- 송중기, 박나래와 같지 않은 부서 사람 = NOT IN
SELECT emp_name, dept_code FROM employee
WHERE dept_code NOT IN (SELECT dept_code FROM employee WHERE emp_name IN ('송중기', '박나래'));

-- 직급이 '대표', '부사장'이 아닌 사람
SELECT emp_name, job_code FROM employee
WHERE job_code IN (SELECT job_code FROM job WHERE job_name NOT IN ('대표', '부사장'));

-- 직급이 '대표', '부사장'이 아닌 사람들 직급의 이름
SELECT e.emp_name, e.job_code, j.job_name
FROM employee e JOIN job j ON j.job_code = e.job_code
WHERE j.job_code IN (SELECT job_code FROM job WHERE job_name NOT IN ('대표', '부사장'));


-- 일반 JOIN문으로도 가능하다.
SELECT
    e.emp_name, e.job_code, j.job_name
FROM employee e
    JOIN job j ON e.job_code = j.job_code
WHERE j.job_name NOT IN ('대표', '부사장');


-- ANY = SOME
-- ANY 대소비교, 동등비교, ANY에 있는 값을 OR로 연결 비교

-- 직원중에 J3 직급 직원들의 최소 급여보다 많이 받는 사람
SELECT salary FROM employee WHERE job_code = 'J3';
SELECT emp_name, salary FROM employee
WHERE salary > ANY (SELECT salary FROM employee WHERE job_code = 'J3');

SELECT salary FROM employee WHERE job_code = 'J3';
SELECT emp_name, salary FROM employee
WHERE salary > SOME (SELECT salary FROM employee WHERE job_code = 'J3');

-- 부서 별 최고 급여 값 보다 많이 받는 사람을 찾을 때
SELECT emp_name, salary FROM employee
WHERE salary >= ALL (SELECT MAX(salary) FROM employee GROUP BY dept_code);

-- 다중열 쿼리 : 행이 한개이면서 컬럼이 여러개인 서브쿼리
-- ex) 가장 먼저 퇴사한 사원과 부서와 직급이 같은 사람

SELECT dept_code FROM employee WHERE ent_yn = 'Y' LIMIT 1;
SELECT job_code FROM employee WHERE ent_yn = 'Y' LIMIT 1;

-- 단일행으로 하는 경우 WHERE 절에 AND로 연결
SELECT
    *
FROM employee
WHERE dept_code = (SELECT dept_code FROM employee WHERE ent_yn = 'Y' LIMIT 1)
AND job_code = (SELECT job_code FROM employee WHERE ent_yn = 'Y' LIMIT 1);

-- 다중열로 변경하는 방법
SELECT
    emp_name, dept_code, job_code
FROM employee
WHERE (dept_code, job_code) = (SELECT dept_code, job_code FROM employee WHERE ent_yn = 'Y' LIMIT 1);

-- 다중행/다중열 서브쿼리 = IN ★★★★★★
SELECT
    emp_name, dept_code, job_code
FROM employee
WHERE (dept_code, job_code) IN (SELECT dept_code, job_code FROM employee WHERE ent_yn = 'Y' LIMIT 1);

-- 다중행 다중열 서브쿼리 : 행도 여러개, 열도 여러개
-- ex) 부서별 최소 급여인 사람 구하는 법, 이름을 출력하는 쿼리

-- 부서별 최소급여를 받는 사람 찾는 법
SELECT dept_code, MIN(salary) FROM employee GROUP BY dept_code;

SELECT emp_name, dept_code, salary FROM employee
WHERE (dept_code, salary) IN (SELECT dept_code, MIN(salary) FROM employee GROUP BY dept_code);


-- 상관 서브쿼리 (상호 연관)
-- 서브쿼리를 구성할때 메인쿼리에 있는 값을 가지고와서 작성할때 사용
-- 일반적으로 성능이 좋지 않으므로 개선이 필요한 문장! -> JOIN 서브쿼리로 해결 가능!
SELECT
    emp_id, emp_name, salary, dept_code
FROM employee e1
WHERE salary <
      ( SELECT AVG(salary)  FROM employee e2  WHERE e1.dept_code = e2.dept_code);


-- EXISTS : ROW 1개 이상이 있다면 무조건 TRUE, ROW가 0이면 FALSE;
SELECT * FROM employee WHERE EXISTS (SELECT dept_code FROM employee WHERE dept_code = 'D9');
SELECT * FROM employee WHERE EXISTS (SELECT dept_code FROM employee WHERE dept_code = 'D11');




-- JOIN 서브쿼리
-- 상관 서브쿼리를 그나마 개선할 수 있는 방법 중 하나, JOIN 문에 서브쿼리가 들어가는 문장
-- 부서별 평균 월급평균 TOP 3 부서를 찾고 EMPLOYEE 정보와 팀 평균 월급을 같이 출력하는 예시

-- GROUP BY 절 -> 부서별 월급평균 TOP 3 부서
SELECT
    rank() OVER (ORDER BY avg(salary) DESC) AS 순위,
    dept_code, avg(salary) AS 팀평균월급
FROM employee
WHERE dept_code IS NOT NULL
GROUP BY dept_code
ORDER BY 순위
LIMIT 3;

-- JOIN을 통한 서브쿼리 활용
SELECT
    e1.emp_name, job_code, e1.dept_code, top3.팀평균월급, top3.순위
FROM employee e1
JOIN (SELECT
            rank() OVER (ORDER BY avg(salary) DESC) AS 순위,
            dept_code, avg(salary) AS 팀평균월급
        FROM employee
        WHERE dept_code IS NOT NULL
        GROUP BY dept_code
        ORDER BY 순위
        LIMIT 3) top3 ON e1.dept_code = top3.dept_code
ORDER BY top3.순위, e1.salary DESC;


-- 상관 서브쿼리를 JOIN 서브쿼리로 개선하는 방법

SELECT
    emp_id, emp_name, salary, dept_code
FROM employee e1
WHERE salary <
      ( SELECT AVG(salary)  FROM employee e2  WHERE e1.dept_code = e2.dept_code);

-- SELECT AVG(salary)  FROM employee e2  WHERE e1.dept_code = e2.dept_code -> 그룹함수로 전환

-- 부서별 평균 그룹바이절
SELECT dept_code, avg(salary) FROM employee GROUP BY dept_code;

-- JOIN 서브쿼리화
SELECT
    e.emp_name, e.salary, d.dept_code, floor(d.부서별평균)
FROM employee e
    JOIN (SELECT dept_code, avg(salary) AS 부서별평균 FROM employee GROUP BY dept_code) d
        ON d.dept_code = e.dept_code
WHERE e.salary > d.부서별평균;


-- 스칼라 서브쿼리
-- SELECT문안에 서브쿼리가 구성되는 서브쿼리
-- 부서별 인원 조회하기
SELECT dept_id, dept_title,
       (SELECT COUNT(*) FROM employee e WHERE e.dept_code = d.dept_id) as 부서인원
FROM department d;


-- OVER() 문 : 윈도우 함수로 특정 창(그룹) 내에서 집계 용도로 활용
--             ex) 반내 등수, 누적 매출, 현재 재고량 등

-- 월급이 낮은 순으로 정렬하고 누적된 월급의 SUM 값을 구하는 방법
SELECT emp_name, salary, SUM(salary) OVER (ORDER BY salary ASC) AS "누적 SUM"
FROM employee
ORDER BY salary ASC;

-- 월급을 낮은 순으로 정렬하고 팀별 누적된 월급의 SUM 값을 구하는 방법
SELECT emp_name, salary, dept_code,
       SUM(salary) OVER (PARTITION BY dept_code ORDER BY salary ASC) AS "팀 누적 SUM"
FROM employee
ORDER BY dept_code, salary ASC;

-- TON-N 분석 = RANKING : ROW에 순위를 매기는 문법 ★★★
-- ex) 급여를 많이 받는 3명, 댓글이 제일 많은 글, 추천수 or 좋아요가 많은 게시물
--     최근 글 중에 10개를 달라 -> 페이징 처리 ★★

-- 급여를 많이 받는 사람의 랭크
-- 1. RANK() OVER(정렬기준) : 정렬시 행의 숫자대로 랭크 부여
-- 동일 순위가 있을때 다음 순위는 동일 순위 인원 만큼 카운팅
SELECT emp_name, salary, RANK() OVER (ORDER BY salary DESC) AS 순위 FROM employee;

-- 2. DENSE_RANK() OVER(정렬기준) : 중복된 순위가 있는 경우, 사람수에서 제외하고 Count
-- 동일 순위가 있을때 다음 순위는 1명으로만 카운팅
SELECT emp_name, salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS 순위 FROM employee;

-- 3. ROW_NUMBER() OVER(정렬기준) : 중복이 있어도 순서대로 카운팅하는 기능
SELECT emp_name, salary, ROW_NUMBER() OVER (ORDER BY salary DESC) AS 순위 FROM employee;

-- 팀별로 월급을 많이 받는 순서를 구해라
SELECT emp_name, salary, dept_code,
       RANK() OVER (PARTITION BY dept_code ORDER BY salary DESC) AS 팀내월급순위
FROM employee
WHERE dept_code IS NOT NULL
ORDER BY dept_code, 팀내월급순위 ASC;


-- with절 : 임시 테이블 또는 가상 테이블 별칭으로 임시값을 저장하고, 활용할수 있는 sub쿼리(함수화)

WITH sum_sal AS (SELECT sum(employee.salary) FROM employee),
    avg_sal AS (SELECT avg(employee.salary) FROM employee),
    count AS (SELECT count(*) FROM employee)
SELECT * FROM sum_sal, avg_sal, count;

-- 수직으로 출력하기
WITH sum_sal AS (SELECT sum(employee.salary) FROM employee),
    avg_sal AS (SELECT avg(employee.salary) FROM employee),
    count AS (SELECT count(*) FROM employee)
SELECT '월급 총합', * FROM sum_sal
UNION
SELECT '월급 평균', * FROM avg_sal
UNION
SELECT '카운트', * FROM count;
