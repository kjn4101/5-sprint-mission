<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.codeit.servlet.UserDto" %>
<html>
<head>
    <title>로그인 성공</title>
</head>
<body>
<h2>로그인 성공</h2>
<p>환영합니다, ${user.name}님!</p>
<hr>
<h3>회원 정보</h3>
<ul>
    <li>아이디: ${user.username}</li>
    <li>비밀번호: ${user.password}</li>
    <li>이름: ${user.name}</li>
    <li>나이: ${user.age}</li>
</ul>
</body>
</html>