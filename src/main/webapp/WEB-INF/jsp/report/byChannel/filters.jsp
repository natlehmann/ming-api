<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:include page="/WEB-INF/jsp/includes/header.jsp">
	<jsp:param value="reportByChannel" name="itemMenuSeleccionado"/>
</jsp:include>

<script type="text/javascript" src='<c:url value="/js/admin/report/reportByChannel_filters.js" />' ></script>

	<div class="msg" id="main-msg">${msg}</div>
	
	<input type="hidden" id="form_action" value='<c:url value="/report/byChannel/create"/>' />
	<input type="hidden" id="check_existence_url" value='<c:url value="/report/byChannel/exists"/>' />
		
	
	<form method="post" id="reportByChannelForm">
	
		<input type="hidden" id="action" name="action" />
		
		<div id="crear_reporte">
		
			<div id="busca">         	
            	<div class="izq"><img src='<c:url value="/images/h1Izq.jpg"/>' width="14" height="34" /></div>
 				<h1><spring:message code="build.report"/></h1> 
            	<div class="der"><img src='<c:url value="/images/h1Der.jpg"/>' width="31" height="34" /></div>
	
				<div class="inline width-40">
					<h2><spring:message code="period"/></h2>
					
					<div class="dropdown">
						<select name="timePeriod" class="dropdown-select">
							<c:forEach items="${timePeriods}" var="period">
								<option value="${period}" ${period eq selectedPeriod ? "selected='selected'" : "" }>
									<spring:message code="${period.code}"/>
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
		
				<div class="inline width-40">
					<h2><spring:message code="end.date"/></h2>
					
					<div class="dropdown">
						<input name="endDate" value='<fmt:formatDate value="${selectedEndDate}" pattern="dd/MM/yyyy"/>' 
							class="datepicker dropdown-select"/>
					</div>
				</div>
	
            
				<div id="buscaBot">
					<input type="button" value='<spring:message code="build"/>' onclick="checkExistence(this)"/>
				</div>
				
			</div>
			

		</div>		
		
	</form>
	
	
	<div id="msg-report-in-progress" style="display: none;">
		
		<div class="msg">
			<span id="progress-msg">
				<spring:message code="report.in.progress"/>
			</span>
			
			<div id="downloaded">
				<spring:message code="report.ready"/>
				<br/>
				
				<div class="width-20">
					<input type="button" value='<spring:message code="download.report"/>' />
				</div>
				
				<br/>
				<br/>
				
				<spring:message code="would.you.like.to.save.this.report"/>
				<br/>
				
				<div class="width-20">
					<input type="button" value='<spring:message code="save"/>' />
				</div>
			</div>
		</div>
	</div>
	
	
	<div id="dialog-report-exists" style="display:none;" title='<spring:message code="confirm"/>'>
			<p>
				<span class="message">
				</span>
			</p>
			
			<div class="ui-dialog-buttonpane">
				<input type="button" value='<spring:message code="accept"/>' onclick="buildReport()" />
				<button type="button" onclick="$('#dialog-report-exists').dialog('close');">
					<spring:message code="cancel"/>
				</button>
			</div>
	</div>


<jsp:include page="/WEB-INF/jsp/includes/footer.jsp" />
