<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
    
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="title" name="회원정보 보기" />
</jsp:include>
    
<section id="content" class="container text-center" style="width: 40%">
	<h3 class="text-center">회원 정보 목록으로 보기</h3>
	<table class="table text-center">
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>나이</th>
			<th>성별</th>
			<th>좋아하는 언어</th>
		</tr>
		<jsp:useBean id="list" scope="request" type="java.util.List"/>
		<c:forEach var="member" items="${list}">
			<tr>
				<td>${member.id}</td>
				<td>${member.name}</td>
				<td>${member.age}</td>
				<td>${member.gender}</td>
				<td>
					<c:forEach var="item" items="${member.devLang}" varStatus="status">
						${item}<c:if test="${not status.last}">,</c:if>
					</c:forEach>
				</td>
			</tr>		
		</c:forEach>
	</table>
</section>
    

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>







