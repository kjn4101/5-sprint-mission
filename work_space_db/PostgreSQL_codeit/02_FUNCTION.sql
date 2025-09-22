
------------------------------- FUNCTION ---------------------------------------
SET search_path TO codeit;

-- from 없는 select문 = 함수 연습용, DB 연산이 필요시 활용
SELECT 'test';
SELECT length('test');


-- 문자열 함수
-- length=char_length : 문자열의 길이를 반환, 국문 영문 같은 크기로 취급
-- octet_length : 문자열의 바이트 수를 반환(국문 = 3byte, 영문 1byte)


SELECT length('test'); -- 4글자
SELECT char_length('test'); -- 4글자
SELECT octet_length('test'); -- 4 byte

SELECT length('홍길동'); -- 3글자
SELECT char_length('홍길동'); -- 3글자
SELECT octet_length('홍길동'); -- 9byte


-- email 길이가 17자리 이상인 사람
SELECT * FROM employee WHERE length(email) >= 17;
SELECT COUNT(*) FROM employee WHERE length(email) >= 17;


-- locate : 문자열을 찾는 함수, 인자와 index를 지정하여 탐색 가능
-- position('찾을 문자열' in '컬럼이나 text값')
-- DB index의 시작은 java와 다르게 1이다.
SELECT position('최길동' in '안녕하세요? 저는 홍길동입니다. 홍길동은 19살입니다.'); -- 0대신 없으면 0이 아님 → 못찾으면 0 결과 없음
SELECT position('홍길동' in '안녕하세요? 저는 홍길동입니다. 홍길동은 19살입니다.'); -- 11



-- 이메일에서 id만 추출하는 방법
SELECT email, position('@' in email), substring(email from 1 for position('@' in email) - 1) FROM employee;


-- LPAD / RPAD : 문자열을 지정한 크기만큼 특수문자로 채워놓는 함수 ★★★
-- 활용 : 가명처리, 마스킹처리할때 자주 사용, 900101-1******
SELECT LPAD('홍길동', 8, '@'); -- @@@@@홍길동, 최대 8글자, 왼쪽 남은문자열을 '@'으로 채워짐
SELECT LPAD('홍길동', 5, '@'); -- @@홍길동, 최대 5글자, 왼쪽 남은문자열을 '@'으로 채워짐
SELECT RPAD('홍길동', 8, '@'); -- 홍길동@@홍길동, 최대 8글자, 왼쪽 남은문자열을 '@'으로 채워짐
SELECT RPAD('홍길동', 5, '@'); -- 홍길동@@, 최대 5글자, 오른쪽 남은문자열을 '@'으로 채워짐

SELECT LPAD('홍길동', 3, '@'); -- '홍길동', 최대크기와 글자수가 같아서 의미 없다.
SELECT LPAD('홍길동', 2, '@'); -- '홍길동', 최대크기와 글자수가 같아서 의미 없다.


-- trim : 문자열의 공백이나 지정한 문자열을 제거하는 함수
-- L/RTRIM('문자') -- 공백 제거
-- FROM : 특정문자 제거하는 방법
-- LEADING FROM : 앞에 존재하는 문자열만 제거
-- TRAILING FROM : 뒤에 존재하는 문자열만 제거

SELECT trim('   안녕하세요   '); -- 안녕하세요
SELECT ltrim('   안녕하세요   '); -- 안녕하세요'  ' -> 뒤에 공백이 남음
SELECT rtrim('   안녕하세요   '); -- '   '안녕하세요 -> 앞에 공백이 남음

-- 특정 문자만 제거하는 방법
SELECT trim('@@!안녕하세요!@@');
SELECT trim('@' from '@@!안녕하세요!@@'); -- from 문법으로 '@'만 삭제하는 방법

-- replace : 특정 문자열을 바꿔주는 함수
-- trim에서 특정 문자만 바꾸는 기능을 replace로도 구현 가능!
SELECT replace('@@!안녕하세요!@@', '@',  ''); -- 없애는 문법
SELECT replace('@@!안녕하세요!@@', '@',  '!'); -- !로 바꾸는 문법



-- substr : 문자열을 자르는 함수 ★★★, 긴 컬럼값을 가지고 올때 일부만 가지고 올수도 있다.
-- substr('문자열|컬럼', '시작위치', [길이]) []=option
SELECT SUBSTR('1234567890abcde', 1, 5);  -- 12345
SELECT SUBSTR('1234567890abcde', 5);  -- 567890abcde
SELECT SUBSTR('1234567890abcde', 5, 3);  -- 567
SELECT SUBSTR('1234567890abcde', -5, 5);  -- abcde

-- 주민번호 마스킹 처리하는 방법
SELECT emp_name, emp_no, SUBSTR(emp_no, 1, 8),
       RPAD(SUBSTR(emp_no, 1, 8), 14, '*') AS 주민번호 FROM employee;



-- substr 파생문법
-- left : 왼쪽부터 자르는 함수
-- right : 오른쪽부터 자르는 함수
SELECT LEFT('1234567890abcde', 5);
SELECT RIGHT('1234567890abcde', 5);


-- 대소문자 변경하기
-- lower, upper
-- 아이디나 영문을 강제로 소문자로 매칭시킬때
SELECT LOWER('Hello DataBase World!!');
SELECT UPPER('Hello DataBase World!!');


-- CONCAT : 문자열 합치기 ★★★★★, 인자를 여러개로 활용할수 있다.
SELECT CONCAT('Hello', 'World');
SELECT CONCAT(emp_name, '님', ' 환영합니다.') FROM employee;

-- 이메일에 y가 포함된 사람
SELECT * FROM employee WHERE email LIKE '%y%';
SELECT * FROM employee WHERE email LIKE CONCAT('%', 'y', '%'); -- Mybatis에서 동적쿼리 만들때 활용

-- reverse : 문자열을 역순으로 바꾸는 함수
SELECT REVERSE('ABCDE');
SELECT REVERSE('가나다');


-- format : 숫자의 포멧팅 출력(금액, 소수점), 소수점자리까지 반올림
-- 향후 front단에서 처리하는 것을 권장함으로 DB format하여 값을 가져오지 말 것
SELECT FORMAT(123456789.123456, 4); -- 소수점 4번째 자리까지 반올림하는 포멧팅,
-- 결과 -> 123,456,789.1235 = 문자열형식

-- 공백으로 채우는 방법
SELECT repeat(' ', 10);

-- 문자열 함수 끝


-- ■ MATH 관련 함수
-- ABS : 절대값
-- MOD : 모듈러 연산 %
-- ROUND : 반올림
-- FLOOR : 버림
-- CEIL : 올림
-- TRUNC : 사용자가 지정한 소수점 자르기
-- RAND : 랜덤값 발생

SELECT ABS(+10);
SELECT ABS(-10);

SELECT MOD(10, 3); -- 1

SELECT ROUND(10.44, 1); -- 10.4, 1을 출력할 소수점 자리수
SELECT ROUND(10.46, 1); -- 10.5, 1을 출력할 소수점 자리수
SELECT ROUND(15.46, -1); -- 20, -1=양수자리


SELECT FLOOR(10.9); -- 10, 버림
SELECT FLOOR(10.2); -- 10, 버림


SELECT CEIL(10.9); -- 11, 올림
SELECT CEIL(10.2); -- 11, 올림

SELECT TRUNC(10.5); -- error 발생, 반드시 2개인자 활용
SELECT TRUNC(10.5, 0); -- 소수점 자르기 -> 문자열 기준으로 잘린다.
SELECT TRUNC(10.12345, 3); -- 3번째 자리까지 자르기
SELECT TRUNC(12345.12345, -2); -- 100자리 미만 절삭


-- RAND : 랜덤한 값, 1.0 ~ 0 무작위 난수 발생
SELECT random();
SELECT setseed(0.5); -- seed 설정
SELECT random();
SELECT floor(random() * 100);

-- 숫자 관련 함수 끝!

-- 날짜 함수 시작!! ★★★★★
SELECT NOW();
SELECT current_date;
SELECT current_time;
SELECT current_timestamp;
SELECT localtimestamp;


-- 날짜 차이
SELECT now()::date - DATE '2024-05-07'; -- 일수 차이
SELECT now()::date - DATE '2024-10-24';
SELECT DATE '2024-10-24' - now()::date;

-- ADDDATE : 인자로 전달받은 날짜와 지정한 일수를 더해주는 함수 (+- 가능) ★★★
-- DATE_ADD, DATE_SUB 거의 비슷한 기능
-- 날짜 더하기
SELECT now() + interval '7 day';
SELECT now() - interval '7 day';

SELECT now() + interval '5 month';
SELECT now() - interval '5 month';

SELECT now() + interval '5 year';
SELECT now() - interval '5 year';

-- 직원 테이블에서 사원의 이름, 입사일, 입사후 6개월이 된 날짜 조회
SELECT emp_name, hire_date, hire_date + interval '6 month' AS "입사 6개월" FROM employee;


-- EXTRACT : 날짜의 연, 월, 일, 시, 분, 초를 추출 할 수 있는 함수, 표준적인 함수
-- 모든 TIME 속성들 추출 가능하다.
SELECT EXTRACT(YEAR FROM NOW());
SELECT EXTRACT(MONTH FROM NOW());
SELECT EXTRACT(DAY FROM NOW());

-- '2024년 07월 15일 extract를 통해 조합하는 방법'
SELECT CONCAT (EXTRACT(YEAR FROM NOW()), '년 ',
               EXTRACT(MONTH FROM NOW()), '월 ',
               EXTRACT(DAY FROM NOW()), '일') AS 날짜 ;


SELECT EXTRACT(hour from now());
SELECT EXTRACTute from now());
SELECT EXTRACT(second from now());
-- 날짜 함수 끝!!

-- 형변환 함수 ★★★★★
-- 문자 : char, varchar 들 호환해서 사용 가능
-- 숫자 : 정수, 실수 호환 사용 가능
-- Date : 날짜 관련 모든 Type 호환

-- cast : 모든 type을 원하는 type으로 변경 가능

-- 날짜 -> 문자, 숫자 ★★★★★
SELECT NOW();
SELECT CAST(NOW() AS CHAR); -- 2024-07-15 15:06:20
SELECT CAST(NOW() AS INT); -- 20240715150633, long
SELECT CAST(NOW() AS JSON); -- "2024-07-15 15:06:45.000000"

-- 문자열을 숫자로 변환 할 때
SELECT CAST('12345' AS DEC) + 1; -- 문자 -> 숫자로 변환 뒤 +1을 함
SELECT CAST('12345.1234' AS DEC) + 1; -- 정수형
SELECT CAST('12345.1234' AS DOUBLE) + 1; -- 실수형
SELECT CAST('12345.1234' AS DECIMAL(10,2)) + 1; -- 총 문자열 10개에서 소수점 2자리 허용

-- 숫자를 문자로 바꾸는 것은 의미가 없다!! -> 문자는 숫자든 숫자형태면 사칙연산 및 산술 연산 지원
SELECT '123' + '456'; -- 579, 숫자는 숫자로 받아들인다.
SELECT CAST(123 AS CHAR) + '456';
SELECT CONCAT(CAST(123 AS CHAR), '456'); -- 123456

-- to_char : 날짜를 특정 포멧의 문자열로 바꾸는 함수
SELECT to_char(NOW(), 'YYYY-MM-DD Day HH24:MI:SS');
SELECT to_char(NOW(), 'DD Month, YYYY Day HH24:MI:SS');
SELECT to_char(NOW(), 'YY/MM/DD HH24:MI:SS');
SELECT to_char(NOW(), 'YY/MM/DD');


-- 1) TO_CHAR: 날짜·숫자 → 문자 포맷팅
SELECT to_char(NOW(), 'YYYY-MM-DD HH24:MI:SS');          -- 날짜를 문자열로
SELECT to_char(1234567.89, 'FM999,999,990.00');          -- 숫자 천단위 구분·소수 고정
SELECT to_char(interval '3 days 4 hours', 'DD "d" HH24"h"'); -- interval 포맷

-- 2) TO_DATE / TO_TIMESTAMP: 문자열 → 날짜/타임스탬프
SELECT to_date('2024-07-15', 'YYYY-MM-DD');              -- date
SELECT to_timestamp('2024-07-15 13:45:20', 'YYYY-MM-DD HH24:MI:SS'); -- timestamp

-- 3) TO_NUMBER: 문자열 → 숫자 (포맷 지정)
SELECT to_number('1,234,567.89', '999G999G990D99');      -- 1234567.89
SELECT to_number('010-1234-5678', '999" -"9999" -"9999'); -- 숫자만 추출 형태

-- 4) CAST / 타입 캐스트 연산자(::)
SELECT CAST('123.45' AS numeric(10,2));                  -- 표준 CAST
SELECT '123.45'::numeric(10,2);                          -- PostgreSQL 축약 표기
SELECT '2025-08-25'::date;                               -- 문자열 → date
SELECT '2025-08-25 09:10:11'::timestamp;                 -- 문자열 → timestamp
SELECT 123::text || '456';                               -- 숫자 → 문자 후 연결
SELECT (extract(epoch from now())::bigint);              -- timestamp → epoch seconds


-- 논리함수
-- IF : 논리절을 구성하는 기본적인 함수, 삼항연산자와 같은 구성
SELECT CASE WHEN 10 > 5 THEN '참' ELSE '거짓' END;

-- 성별 출력하기
SELECT
    emp_name, emp_no, substring(emp_no from 8 for 1) AS 성별,
    CASE WHEN substring(emp_no from 8 for 1) = '1' THEN '남' ELSE '여' END AS 성별문자
FROM employee;

-- 성별 출력하기, 2000대생이 포함된 경우 1,3=남자 / 2,4=여자
SELECT
    emp_name, emp_no, substring(emp_no from 8 for 1) AS 성별,
    CASE WHEN substring(emp_no from 8 for 1) in ('1','3') THEN '남' ELSE '여' END AS 성별문자
FROM employee;


-- 여성만 축력하기
SELECT
    *
FROM
    employee
WHERE
    SUBSTRING(emp_no, 8, 1) IN ('2', '4');


-- COALESCE() : 값이 null일 경우 default값을 설정할수 있응 함수 (NVL) ★★★
SELECT COALESCE(NULL, 0);
SELECT COALESCE(NULL, '-');
SELECT COALESCE(NULL, '');
SELECT emp_name, bonus FROM employee;
SELECT emp_name, COALESCE(bonus,0) FROM employee;


-- 최대값을 찾아오는 방법
select greatest(1,2,3,4,5);

-- 최소값을 찾아오는 방법
select least(1,2,3,4,5);


-- CASE 문 : IF와 활용 비슷함
-- CASE
--    WHEN 조건1 THEN 결과1
--    WHEN 조건2 THEN 결과2
--    WHEN 조건n THEN 결과n
--    ELSE 결과
--  END

-- 주민번호로 남여구별 case 문으로 구성
SELECT
    emp_name, emp_no,
    CASE
        WHEN substring(emp_no from 8 for 1) = '1' THEN '남'
        WHEN substring(emp_no from 8 for 1) = '2' THEN '여'
        WHEN substring(emp_no from 8 for 1) = '3' THEN '남'
        WHEN substring(emp_no from 8 for 1) = '4' THEN '여'
        ELSE '-'
        END AS 성별
FROM employee;


SELECT
    emp_name, emp_no,
    CASE
        WHEN substring(emp_no from 8 for 1) IN ('1','3') THEN '남'
        ELSE '여'
        END AS 성별
FROM employee;

SELECT emp_name, salary,
       CASE
           WHEN salary > 5000000 THEN '1등급'
           WHEN salary > 3500000 THEN '2등급'
           WHEN salary > 2000000 THEN '3등급'
           ELSE '4등급'
           END 등급
FROM employee
ORDER BY 등급;

-- 그룹함수는 그룹함수에서 실습!!

------------------------------ FUNCTION END ------------------------------------