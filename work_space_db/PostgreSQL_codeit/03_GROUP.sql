SET search_path TO codeit;

-- 그룹 함수 : 결과 값이 N:1로 조합되는 함수
--  ex) AVG(평균), SUM(총합), COUNT(갯수 세기), MIN(최소값), MAX(최대값), STDDEV(표준편차), VARIANCE(분산)  ★★★★★
--  그룹함수는 DB에서 무거운 기능, 성능적 고려 필요, 느려지는 이유 : 모든 행을 조회해서 해당 열만 가져오기 때문에 full scan 된다.

-- SUM : 컬럼의 총합을 구하는 함수
SELECT sum(salary) FROM employee;

-- AVG : 컬럼의 평균을 구하는 함수
SELECT avg(salary) FROM employee;

-- STD : 컬럼의 표준편차를 구하는 함수
SELECT stddev(salary) FROM employee;

-- VARIANCE : 분산을 구하는 함수
SELECT variance(salary) FROM employee;



-- COUNT : 컬럼의 갯수를 구하는 함수 ★★★★★
SELECT count(*) FROM employee;
SELECT count(emp_id) FROM employee;
SELECT count(*) FROM employee WHERE dept_code = 'D5'; -- D5 부서의 인원

-- MAX : 컬럼의 최대값 구하는 함수
SELECT max(salary) FROM employee;

-- MIN : 컬럼의 최소값 구하는 함수
SELECT min(salary) FROM employee;


-- GROUP BY ★★
-- - 그룹함수를 사용할때 그룹핑하여 그룹별로 계산이 가능한 명령어
-- - ex) 직급별 월급 평균, 합, 부서별 월급 평균 합, 가장 많이 받는 사람

-- 부서별, 직위별 월급 평균 계산
SELECT dept_code, avg(salary) FROM employee; -- GROUP BY가 없어서 에러 발생!
SELECT dept_code, avg(salary) FROM employee GROUP BY dept_code; -- 정렬되진 않는다.
SELECT dept_code, avg(salary) FROM employee GROUP BY dept_code ORDER BY dept_code;
SELECT dept_code, floor(avg(salary)) FROM employee GROUP BY dept_code ORDER BY dept_code;
SELECT dept_code, floor(avg(salary)) as 부서별월급평균 FROM employee GROUP BY dept_code ORDER BY dept_code;
SELECT job_code, floor(avg(salary)) as 직급별월급평균 FROM employee GROUP BY job_code ORDER BY job_code;

-- 부서별 인원
SELECT dept_code, count(*) FROM employee GROUP BY dept_code ORDER BY dept_code;

-- WHERE절 결합 : 그룹함수가 실행되기 전에 먼저 시행되어 사전 필터링을 수행하는 문장
-- 부서코드가 없는 사람을 제외한 월급의 평균
SELECT avg(salary) FROM employee
WHERE dept_code IS NOT NULL;

-- 부서코드가 없는 사람을 제외한 부서별 월급 평균
SELECT dept_code, floor(avg(salary)) FROM employee
WHERE dept_code IS NOT NULL
GROUP BY dept_code ORDER BY dept_code;

-- 부서코드가 없는 사람들의 월급 평균
SELECT dept_code, floor(avg(salary)) FROM employee
WHERE dept_code IS NULL
GROUP BY dept_code ORDER BY dept_code;

-- GROUP BY 절 다중으로 활용하기 (2개 이상 결합)
-- 부서별 - 부서내 직급별 인원 세기
SELECT dept_code, job_code, count(*)
FROM employee
WHERE dept_code IS NOT NULL
GROUP BY dept_code, job_code
ORDER BY dept_code, job_code;

-- 인원수로 정렬하고 싶을때
SELECT dept_code, job_code, count(*) AS 인원수
FROM employee
WHERE dept_code IS NOT NULL
GROUP BY dept_code, job_code
-- ORDER BY count(*) DESC;
ORDER BY 인원수 DESC; -- 권장 : 별칭사용

-- HAVING 절 ★★
-- - 그룹함수의 결과가 나오고 그 결과에서 조건절을 찾을때 사용하는 방법 (그룹연산시 후행 연산)
-- - WHERE절은 그룹함수가 계산되기 전에 반영됨으로 선행연산이 되고 HAVING절은 그룹함수 이후에 실행
-- - 실행 순서 : where(값 필터링) - 그룹 함수 - having(결과를 다시 조건 반영)

-- 부서별 월급 평균
SELECT dept_code, floor(AVG(salary)) AS 평균 FROM employee GROUP BY dept_code ORDER BY 평균 DESC;

-- 부서별 월급 평균이 300만원 이상인 부서만 출력
-- 1. WHERE절 시도 -> 틀린답
-- -> 월급이 300만원 이상인 직원을 찾고, 부서별 평균을 구한 쿼리
SELECT dept_code, floor(AVG(salary)) AS 평균 FROM employee
WHERE salary >= 3_000_000
GROUP BY dept_code ORDER BY 평균 DESC;

-- 2. HAVING절 시도 -> 정답
SELECT dept_code, floor(AVG(salary)) AS 평균 FROM employee
GROUP BY dept_code
HAVING floor(AVG(salary)) >= 3_000_000
ORDER BY 평균 DESC;

-- PostgraSQL은 HAVING절 별칭 사용 불가!
SELECT dept_code, floor(AVG(salary)) AS 평균 FROM employee
GROUP BY dept_code
HAVING 평균 >= 3_000_000
ORDER BY 평균 DESC;

-- 부서원이 3명 이상인 부서만 출력
SELECT dept_code, count(*) FROM employee
GROUP BY dept_code HAVING count(*) >= 3;

SELECT dept_code, count(*) AS 인원수 FROM employee
GROUP BY dept_code HAVING count(*) >= 3
ORDER BY 인원수 DESC;

-- ROLLUP : PostgreSQL에서는 GROUP BY ROLLUP(...) 사용
--          ROLLUP에 선언된 왼쪽 순으로 소계나 총계를 구해오는 기능

-- 부서, 직급별 인원을 세오는 쿼리
SELECT dept_code, job_code, count(*) AS 인원수
FROM employee GROUP BY dept_code, job_code
ORDER BY dept_code, job_code;

-- 부서, 직급별 인원 소계 세오는 쿼리
SELECT dept_code, job_code, count(*) AS 인원수
FROM employee
GROUP BY ROLLUP (dept_code, job_code)
ORDER BY dept_code, job_code;

-- GROUPING
--  - GROUP BY에 산출된 ROW인 경우에는 0을 반환
--  - ROLLUP를 이용해서 산출되는 ROW 1 이상으로 반환

SELECT
        dept_code,
        job_code,
        count(*) AS 인원수,
        CASE
            WHEN GROUPING(dept_code) = 0 AND GROUPING(job_code) = 1
                THEN '부서별 합계'
            WHEN GROUPING(dept_code) = 1 AND GROUPING(job_code) = 1
                THEN '총 합계'
            ELSE '그룹별 합계'
        END AS 구분
FROM employee
GROUP BY ROLLUP (dept_code, job_code)
ORDER BY dept_code, job_code;



