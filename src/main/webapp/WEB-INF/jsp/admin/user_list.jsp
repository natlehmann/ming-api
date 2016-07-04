<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
	<jsp:param value="users" name="itemMenuSeleccionado"/>
</jsp:include>

<script type="text/javascript" src='<c:url value="/js/admin/user_list.js" />' ></script>

<c:if test="${msg != null}">
	<div class="msg">${msg}</div>
</c:if>

<div id="crear">
	<button type="button" onclick="ir('create')"><spring:message code="users.create.new"/></button>
</div>

<div id="adminInt">

	<h1><spring:message code="users"/></h1>
	
	<div class="Grid">

		<table class="datatable">
			<thead>
				<tr>
					<th><spring:message code="id"/></th>
					<th><spring:message code="username"/></th>
					<th><spring:message code="user.email"/></th>
					<th><spring:message code="language"/></th>
					<th><spring:message code="header.is.admin"/></th>
					<th><spring:message code="actions"/></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		
	</div>
</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />