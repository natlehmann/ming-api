<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp" />

<div id="login">

	<div class="izq"><img src='<c:url value="/images/h1Izq.jpg"/>' width="14" height="34" /></div>
	<h1>LOGIN WITH USERNAME AND PASSWORD</h1>
	<div class="der"><img src='<c:url value="/images/h1Der.jpg"/>' width="31" height="34" /></div>
	
	<c:if test="${msg != null}">
		<div class="msg">${msg}</div>
	</c:if>
	
	<form action='<c:url value="j_spring_security_check" />' method='POST' id="loginsearch">
	
        
		<div class="user">
			username
			<input name="j_username" type="text" size="40" placeholder="" />
		</div>

		<div class="user">
			password
			<input name="j_password" type="password" size="40" placeholder="" />
		</div>


		<div id="buscaBot">
			<input name="submit" type="submit" value="LOGIN" />
		</div>
		
	</form>
	
</div>


	<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />