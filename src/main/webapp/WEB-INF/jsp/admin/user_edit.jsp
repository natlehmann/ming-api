<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
	<jsp:param value="users" name="itemMenuSeleccionado"/>
</jsp:include>


<div id="adminInt" class="email">

	<c:choose>
		<c:when test="${user.id == null}">
			<h1><spring:message code="users.create.new"/></h1>
		</c:when>
		
		<c:otherwise>
			<h1><spring:message code="user.update"/></h1>
		</c:otherwise>
	</c:choose>
	

	<form:form commandName="user" action="accept" method="POST">
	
		<form:hidden path="id"/>
		
		<div class="campo">
			<form:label path="username"><spring:message code="username"/></form:label>
			<form:errors path="username" cssClass="error"/>
			<form:input path="username" cssErrorClass="error"/>
		</div>
	
		<div class="campo">
			<form:label path="email"><spring:message code="user.email"/></form:label>
			<form:errors path="email" cssClass="error"/>
			<form:input path="email" cssErrorClass="error"/>
		</div>
		
		<div class="campo">
			<form:label path="password"><spring:message code="password"/></form:label>
			<form:errors path="password" cssClass="error"/>
			<form:password path="password" cssErrorClass="error"/>
		</div>
		
		<div class="campo">
			<form:label path="apiKey"><spring:message code="user.apiKey"/></form:label>
			<form:errors path="apiKey" cssClass="error"/>
			<form:input path="apiKey" cssErrorClass="error"/>
		</div>
		
		
		<div class="campo">
			<form:label path="language"><spring:message code="language"/></form:label>
			
			<div class="dropdown">
				<select name="language" class="dropdown-select">
					<option value="es" ${selectedLanguage eq "es" ? "selected='selected'" : "" }>
						<spring:message code="spanish"/>
					</option>
					
					<option value="en" ${selectedLanguage eq "en" ? "selected='selected'" : "" }>
						<spring:message code="english"/>
					</option>
				</select>
			</div>
		</div>
		
		<div class="campo">
			<label><spring:message code="is.admin"/></label>
			<input type="checkbox" value="${userIsAdmin}" name="isAdmin"/>
		</div>

		
		
		<div class="acciones">
			<form:button value="Aceptar"><spring:message code="accept"/></form:button>
			<button type="button" onclick="ir('list')"><spring:message code="cancel"/></button>
		</div>
	
	</form:form>
	
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />