<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<script type="text/javascript" src='<c:url value="/js/admin/report/reportByChannel_list.js" />' ></script>

<div id="adminInt">

	<h1><spring:message code="saved.reports"/></h1>
	
	<div class="Grid">

		<table class="datatable datatable-weekly">
			<thead>
				<tr>
					<th><spring:message code="id"/></th>
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

