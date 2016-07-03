<%@page import="ar.com.marcelomingrone.vericast.reports.model.RoleNames"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize ifAllGranted="<%= RoleNames.ADMINISTRATOR.toString() %>">
	<script type="text/javascript" src='<c:url value="/js/admin/report/reportByChannel_list_admin.js" />' ></script>
</sec:authorize>

<sec:authorize ifNotGranted="<%= RoleNames.ADMINISTRATOR.toString() %>">
	<script type="text/javascript" src='<c:url value="/js/admin/report/reportByChannel_list.js" />' ></script>
</sec:authorize>

<div id="adminInt">

	<h1><spring:message code="saved.reports"/></h1>
	
	<div class="Grid">

		<table class="datatable datatable-weekly">
			<thead>
				<tr>
					<th><spring:message code="id"/></th>
					
					<sec:authorize ifAllGranted="<%= RoleNames.ADMINISTRATOR.toString() %>">
						<th><spring:message code="owner"/></th>
					</sec:authorize>
					
					<th><spring:message code="timePeriod"/></th>
					<th><spring:message code="endDate"/></th>
					<th><spring:message code="state"/></th>
					<th><spring:message code="actions"/></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		
	</div>
</div>

