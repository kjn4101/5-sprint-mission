SET search_path TO codeit;

/*
01. SUBQUERY 관련 예제
*/

/* Q1)
사원명이 '노홍철'인 사원의 부서코드와 같은 부서에 속한 직원의 이름과 부서코드를 조회하시오.
(힌트: 서브쿼리, WHERE 절)
*/
SELECT emp_name, dept_code
FROM employee
WHERE dept_code = (SELECT dept_code FROM employee WHERE emp_name = '노홍철')
AND emp_name != '노홍철';


/* Q2)
전 직원의 평균 급여보다 많은 급여를 받고 있는 직원의 사번, 이름, 직급코드, 급여를 조회하시오.
(힌트: 서브쿼리, AVG, WHERE 절)
*/
SELECT emp_id, emp_name, dept_code, salary
FROM employee
WHERE salary >= (SELECT avg(salary) FROM employee);


/* Q3)
노홍철 사원의 급여보다 많은 급여를 받고 있는 사원의 사번, 이름, 부서코드, 직급코드, 급여를 조회하시오.
(힌트: 서브쿼리, WHERE 절)
*/
SELECT emp_no, emp_name, dept_code, job_code, salary
FROM employee
WHERE salary > (SELECT salary FROM employee WHERE emp_name = '노홍철');


/* Q4)
가장 적은 급여를 받는 직원의 사번, 이름, 직급코드, 부서코드, 급여, 입사일을 조회하시오.
(힌트: 서브쿼리, MIN, WHERE 절)
*/
SELECT emp_no, emp_name, job_code, dept_code, salary, hire_date
FROM employee
WHERE salary = (SELECT min(salary) FROM employee);


/* Q5)
부서별 급여 합계 중 가장 큰 부서의 부서명, 급여 합계를 조회하시오.
(힌트: 서브쿼리, GROUP BY, HAVING 절)
*/
SELECT
    d.dept_title AS 부서명,
    SUM(e.salary) AS 급여합계
FROM employee e
JOIN department d ON e.dept_code = d.dept_id
GROUP BY d.dept_title, d.dept_title
HAVING SUM(e.salary) = (
    SELECT MAX(dept_salary_sum)
    FROM (
        SELECT SUM(e2.salary) AS dept_salary_sum
        FROM employee e2
        GROUP BY e2.dept_code
    ) AS dept_totals
);

/* Q6)
부서별 최고 급여를 받는 직원의 이름, 직급, 부서, 급여를 조회하시오.
(힌트: 서브쿼리, IN 연산자)
*/
SELECT e.emp_name, j.job_name, d.dept_title, e.salary
FROM employee e
JOIN department d ON e.dept_code = d.dept_id
JOIN job j ON e.job_code = j.job_code
WHERE (e.dept_code, e.salary) IN (
    SELECT dept_code, max(salary)
    FROM employee
    GROUP BY dept_code)
ORDER BY d.dept_title;



/* Q7)
과장 직급의 최소 급여보다 많이 받는 대리 직급의 사번, 이름, 직급명, 급여를 조회하시오.
*/
SELECT e.emp_id, e.emp_name, j.job_name, e.salary
FROM employee e
JOIN job j ON e.job_code = j.job_code
WHERE j.job_name = '대리'
  AND e.salary >= (
    SELECT min(e2.salary)
    FROM employee e2
    JOIN job j2 ON e2.job_code = j2.job_code
    WHERE j2.job_name = '과장');


/* Q8)
직급별 급여 평균을 조회하시오.
(힌트: 서브쿼리, GROUP BY)
*/
SELECT j.job_name, floor(sq.avg_salary) AS 급여평균
FROM (
    SELECT job_code, AVG(salary) AS avg_salary
    FROM employee
    GROUP BY job_code
) sq
JOIN job j ON sq.job_code = j.job_code;

