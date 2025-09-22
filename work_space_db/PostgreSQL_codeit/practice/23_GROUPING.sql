SET search_path TO codeit;

/*
01. GROUP BY 및 HAVING 및 ORDER BY 관련 예제
*/


/* Q1)
사원들의 급여의 총 합을 조회하시오.
*/
SELECT sum(salary)
FROM employee;


/* Q2)
사원들의 급여의 평균을 구하시오.
*/
SELECT avg(salary)
FROM employee;


/* Q3)
employee 테이블에서 부서 코드별 그룹을 지정하여
부서코드, 그룹별 급여의 합계, 그룹별 급여의 평균(정수처리), 인원수를 조회하고,
부서코드 순으로 오름차순 정렬하시오.
*/
SELECT dept_code, sum(salary) AS 급여합계, floor(avg(salary)) AS 급여평균, count(*) AS 인원수
FROM employee
GROUP BY dept_code
ORDER BY dept_code;


/* Q2)
employee 테이블에서 직급별 직급코드, 보너스를 받는 사원수를 조회하여
직급코드 순으로 오름차순 정렬하시오.
*/
SELECT job_code, count(*)
FROM employee
WHERE bonus IS NOT NULL
GROUP BY job_code
ORDER BY job_code;


/* Q3) HARD!!
employee 테이블에서 주민번호의 8번 째 자리를 조회하여 1이면 남, 2면 여로 결과를 조회하고
성별별 급여 평균(정수처리), 급여 합계, 인원수를 조회한 뒤
인원수로 내림차순 정렬하시오.
*/
SELECT
    CASE substring(emp_no FROM 8 FOR 1)
        WHEN '1' THEN '남'
        WHEN '3' THEN '남'
        WHEN '2' THEN '여'
        WHEN '4' THEN '여'
        END AS 성별,
    floor(avg(salary)) AS 급여_평균,
    sum(salary) AS 급여_합계,
    count(*) AS 인원수
FROM employee
GROUP BY 성별
ORDER BY 인원수 DESC;


/* Q4)
300만원 이상을 받는 사원들의 부서별 평균 급여를 조회하시오.
(힌트: 평균 급여는 FLOOR()를 사용해 적절히 잘라내야 할 수도 있다)
*/
SELECT dept_code, floor(avg(salary)) AS 평균급여
FROM employee
WHERE salary >= 3000000
GROUP BY dept_code;



/* Q5)
평균이 300만원 이상인 부서 사원들의 평균 급여를 조회하시오.
(힌트: 평균 급여는 FLOOR()를 사용해 적절히 잘라내야 할 수도 있다)
*/
SELECT dept_code, floor(avg(salary)) AS 평균급여
FROM employee
GROUP BY dept_code
HAVING avg(salary) >= 3000000;



/* Q6)
급여 합계가 가장 많은 부서의 부서 코드와 급여 합계를 구하시오.
*/
SELECT dept_code, sum(salary) AS 급여합계
FROM employee
GROUP BY dept_code
ORDER BY 급여합계 DESC
LIMIT 1;
