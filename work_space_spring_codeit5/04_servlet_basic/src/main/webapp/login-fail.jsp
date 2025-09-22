<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>로그인 실패</title>
</head>
<body>
<h2>로그인 실패</h2>
<p>${error}</p>
<a href="${pageContext.request.contextPath}/login.jsp">다시 시도하기</a>
</body>
</html>
