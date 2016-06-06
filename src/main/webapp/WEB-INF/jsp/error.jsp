<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp" />

<div id="login">

	<h1>${titulo}</h1>
	
	<div class="user">${msg}</div>
    
	<div id="buscaBot">
		<a href='<c:url value="/home"/>' class="classname">
			VOLVER
		</a>
	</div>
	
</div>

<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />