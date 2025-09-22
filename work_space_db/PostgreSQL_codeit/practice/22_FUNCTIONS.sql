SET search_path TO codeit;

/*
02. FUNCTION 관련 실습
*/


/* Q1)
모든 사원들의 아이디를 '@' 이후를 제외하여 조회하시오.
(힌트: SUBSTRING(), POSITION())
*/
SELECT substring(email from 1 FOR position('@' IN email) - 1) 아이디
FROM employee;

/* Q2)
employee 테이블에서 직원들의 주민번호를 조회하여 사원명, 생년, 생월, 생일을 각각 분리하여 조회할 것.
(단, 컬럼의 별칭은 사원명, 생년, 생월, 생일로 한다.)
(힌트: SUBSTRING())
*/
SELECT emp_name 사원명,
       substring(emp_no from 1 for 2) AS 생년,
       substring(emp_no from 3 for 2) AS 생월,
       substring(emp_no from 5 for 2) AS 생일
FROM employee;


/* Q3)
여직원들을 조회하시오.
(단, WHERE 조건절에는 emp_no 컬럼만 사용할 수 있음)
(힌트: 대한민국에서 여자의 주민등록번호 뒷자리는 2 또는 4로 시작한다.)
*/
SELECT *
FROM employee
WHERE emp_no LIKE '_______2%' OR emp_no LIKE '_______4%';


/* Q4)
employee 테이블에서 모든 직원들이 입사일로부터 현재까지 몇 개월 간 근무했는지를 계산하여 이름과 개월 수를 조회하시오.
(힌트: AGE(), EXTRACT(), CURRENT_DATE)
*/
SELECT emp_name,
       (extract(YEAR FROM age(current_date, hire_date)) * 12) + (extract(MONTH FROM age(current_date, hire_date)))
FROM employee;


/* Q5)
employee 테이블에서 사원의 이름, 입사일, 입사 후 6개월이 되는 날짜를 계산하여 조회하시오.
(힌트: + INTERVAL)
*/
SELECT emp_name, hire_date, hire_date + INTERVAL '6 month' AS 입사_후_6개월
FROM employee;


/* Q6)
employee 테이블에서 근속 년수가 20년 이상인 직원을 조회하시오.
(힌트: EXTRACT(), AGE(), CURRENT_DATE)
(여러가지 답이 나올 수 있음.)
*/
SELECT *
FROM employee
WHERE extract(YEAR FROM age(current_date, hire_date)) >= 20;


/* Q7)
employee 테이블에서 사원 이름, 입사년, 입사월, 입사일 조회하시오.
단, EXTRACT()를 반드시 사용할 것.
*/
SELECT emp_name,
       extract(YEAR FROM hire_date) 입사년,
       extract(MONTH FROM hire_date) 입사월,
       extract(DAY FROM hire_date) 입사일
FROM employee;


/* Q8)
employee 테이블에서 직원의 이름, 입사일, 근무 년수를 조회할 것.
단, 근무 년수는 '현재년도 - 입사년도'로 계산하며, EXTRACT()를 반드시 사용할 것.
*/
SELECT emp_name, extract(DAY FROM hire_date) 입사일, extract(YEAR FROM now()) - extract(YEAR FROM hire_date) 근무년수
FROM employee;

SELECT emp_name, hire_date
FROM employee;

/* Q9)
employee 테이블에서 이름, 입사일을 조회하시오.
단, 입사일에 포맷을 적용하여 '2018년 6월 10일 (Tue)' 형식으로 출력할 것.
(힌트: TO_CHAR())
*/
SELECT emp_name, to_char(hire_date, 'YYYY년 MM월 DD일 (dy)')
FROM employee;


/* Q10)
employee 테이블에서 2000년도 이후에 입사한 사원의 사번, 이름, 입사일을 조회하시오.
(힌트: EXTRACT()을 사용)
*/
SELECT emp_id, emp_name, hire_date
FROM employee
WHERE extract(YEAR FROM hire_date) >= 2000;


/* Q11)
EMPLOYEE 테이블에서 사번이 홀수인 직원들의 모든 정보를 조회하시오.
(힌트: MOD())
*/
SELECT *
FROM employee
WHERE mod(emp_id, 2) = 1;


/* Q12)
EMPLOYEE 테이블에서 보너스 포인트가 NULL인 직원은 0.5로, 보너스 포인트가 NULL이 아닌 경우 0.7로 변경하여 조회하시오.
(힌트: COALESCE(), CASE 문)
*/
SELECT emp_name,
       CASE
           WHEN bonus IS NULL THEN 0.5 ELSE 0.7
           END AS 보너스_포인트
FROM employee;

/* Q13) HARD!!
직원의 급여를 아래와 같이 인상하고자 한다.
- 직급 코드가 J7인 직원은 급여의 10%를 인상하고,
- 직급 코드가 J6인 직원은 급여의 15%를 인상하고,
- 직급 코드가 J5인 직원은 급여의 20%를 인상하고,
- 그 외 직급의 직원은 5%만 인상한다.
employee 테이블에서 직원명, 직급코드, 급여, 인상급여(위 조건)을 조회하시오.
단, 인상된 급여는 '인상급여'라는 별칭을 붙여 조회할 것.
(힌트: CASE문 사용)
*/
SELECT emp_name, job_code, salary,
       CASE
           WHEN job_code = 'J7' THEN salary * 1.1
           WHEN job_code = 'J6' THEN salary * 1.15
           WHEN job_code = 'J5' THEN salary * 1.2
           ELSE salary * 1.05
           END AS 인상급여
FROM employee;


/* Q14) HARD!!
사번, 사원명, 급여를 EMPLOYEE 테이블에서 조회하고
급여가 500만원 초과이면 '고급',
급여가 300만원 초과~ 500만원 이하이면 '중급',
그 이하는 '출력'으로 출력하여 처리하고 별명은 '구분'으로 한다.
(힌트: CASE문 사용)
*/
SELECT emp_id, emp_name, salary,
       CASE
           WHEN salary > 5000000 THEN '고급'
           WHEN salary > 3000000 AND salary <= 5000000 THEN '중급'
           ELSE '출력'
           END AS 구분
FROM employee;
