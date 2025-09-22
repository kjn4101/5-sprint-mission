SET search_path TO codeit;

/*
01. SELECT 관련 실습
: SELECT, ORDER BY, WHERE, DISTINCT, LIMIT
*/

/* Q1)
employee 테이블의 사번(사원번호), 이름을 조회하시오.
*/
SELECT
	emp_id,
	emp_name
FROM
	employee;


/* Q2)
employee 테이블에서 부서코드가 D9인 사원을 조회하시오.
*/
SELECT
	*
FROM
	employee
WHERE
	dept_code = 'D9';


/* Q3)
employee 테이블에서 직급코드가 J1인 사원을 조회하시오.
*/
SELECT *
FROM employee
WHERE job_code = 'J1';


/* Q4)
employee 테이블에서 급여가 300만원 이상인 사원의 사번, 이름, 부서코드, 급여를 조회하시오.
*/
SELECT emp_id, emp_name, dept_code, salary
FROM employee
WHERE salary >= 3000000;


/* Q5)
부서코드가 D6이고 급여를 200만원보다 많이 받는 직원의 이름, 부서코드, 급여를 조회하시오.
*/
SELECT emp_name, dept_code, salary
FROM employee
WHERE salary >= 2000000;


/* Q6)
보너스를 받지 않는 사원에 대해 사번, 직원명, 급여, 보너스를 조회하시오.
*/
SELECT emp_id, emp_name, salary, bonus
FROM employee
WHERE bonus is NULL;


/* Q7)
D9부서에서 근무하지 않는 사원의 사번, 이름, 부서코드를 조회하시오.
*/
SELECT emp_id, emp_name, dept_code
FROM employee
WHERE dept_code != 'D9';


/* Q8)
employee 테이블에서 퇴사여부가 N인 직원을 조회하고 사번, 이름, 입사일을 별칭을 사용해 조회하시오.
(퇴사여부 컬럼은 'ent_yn'이고 N은 퇴사 안 한사람, Y는 퇴사 한 사람)
*/
SELECT emp_id 사번, emp_name 이름, hire_date 입사일
FROM employee
WHERE ent_yn = 'N';


/* Q9)
employee 테이블에서 급여를 350만원 이상, 550만원 이하를 받는 직원의
사번, 이름, 급여, 부서코드, 직급코드를 조회하시오.
*/
SELECT emp_id, emp_name, salary, dept_code, job_code
FROM employee
WHERE salary >= 3500000 AND salary <= 5500000;


/* Q10)
employee 테이블에서 350만원 미만, 또는 550만원을 초과하는 직원의 사번, 이름, 부서코드, 급여를 조회하시오.
*/
SELECT emp_id, emp_name, dept_code, salary
FROM employee
WHERE salary < 3500000 OR salary > 5500000;


/* Q11)
employee 테이블에서 성이 김씨인 직원의 사번, 이름, 입사일을 조회하시오.
*/
SELECT emp_id, emp_name, hire_date
FROM employee
WHERE emp_name LIKE '김%';


/* Q12)
employee 테이블에서 성이 김씨 성이 아닌 직원의 사번, 이름, 입사일을 조회하시오.
*/
SELECT emp_id, emp_name, hire_date
FROM employee
WHERE emp_name NOT LIKE '김%';


/* Q13)
employee 테이블에서 '하'가 이름에 포함된 직원의 이름, 주민번호, 부서코드를 조회하시오.
*/
SELECT emp_name, emp_no, dept_code
FROM employee
WHERE emp_name LIKE '%하%';


/* Q14)
employee 테이블에서 전화번호 국번이 3자리 이면서 9로 시작하는 직원의 사번, 이름, 전화번호를 조회하시오.
(힌트: '_')
*/
SELECT emp_id, emp_name, phone
FROM employee
WHERE phone LIKE '___9%';


/* Q15)
employee 테이블에서 9뒤에 7개의 번호가 있는 전화번호를 가진 직원의 사번, 이름, 전화번호를 조회하시오.
(힌트: '_')
*/
SELECT emp_id, emp_name, phone
FROM employee
WHERE phone LIKE '___9_______';


/* Q16)
employee 테이블에서 영어로 표현한 성씨가 3자리인 이메일 주소를 가진 사원의 사번, 이름, 이메일 주소를 조회하시오.
예) (O) 선동일 - sun_di@codeit.com (세 글자라서 해당 됨)
예) (X) 송중기 - song_jk@codeit.com (네 글자라서 해당 안됨)
예) (X) 노홍철 - no_oc@codeit.com (두 글자라서 해당 안됨)
(힌트: SUBSTR, INSTR, REGEXP 중 사용하세요.)
*/
SELECT emp_id, emp_name, email
FROM employee
WHERE length(substring(email FROM 1 FOR position('_' IN email) - 1)) = 3;

/* Q17)
employee 테이블에서 이씨 성이 아닌 직원의 사번, 이름, 이메일 주소 조회
*/
SELECT emp_id, emp_name, email
FROM employee
WHERE emp_name NOT LIKE '이%';


/* Q18)
employee 테이블에서 D6부서 이거나 D8 부서인 직원들의 이름, 부서코드, 급여를 조회하시오
*/
SELECT emp_name, dept_code, salary
FROM employee
WHERE dept_code = 'D6' OR dept_code = 'D8';


/* Q19)
employee 테이블에서 D6부서나 D8부서가 아닌 사원을 조회하시오.
*/
SELECT *
FROM employee
WHERE dept_code != 'D6' AND dept_code != 'D8';


/* Q20)
J2직급이면서 급여 200만원 이상 받는 직원이거나 J7직급인 직원의 이름, 급여, 직급코드를 조회하시오.
*/
SELECT emp_name, salary, job_code
FROM employee
WHERE (job_code = 'J2' AND salary >= 2000000) OR (job_code = 'J7');


/* Q21)
J2직급이거나 J7직급인 직원들 중에 급여가 200만원 이상인 직원의 이름, 급여, 직급코드를 조회하시오.
*/
SELECT emp_name, salary, job_code
FROM employee
WHERE (job_code = 'J2' OR job_code = 'J7') AND salary >= 2000000;