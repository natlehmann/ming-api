<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

		</div> <%-- cierra contenedor --%>
		
		
		
		<div id="dialog-eliminar" style="display:none;" title="Confirmación">
			<form action="delete" method="post">
				
				<input type="hidden" name="id" id="dialog-eliminar-id" value="" />
				
				<p>
					<span id="dialog-eliminar-mensaje">
						<spring:message code="are.you.sure.delete"/>
					</span>
				</p>
				
				<div class="ui-dialog-buttonpane">
					<input type="submit" value="Aceptar" />
					<button type="button" onclick="$('#dialog-eliminar').dialog('close');">
						<spring:message code="cancel"/>
					</button>
				</div>
			</form>
		</div>
		
		
		<div class="pie">
			@2015 MARCELO MINGRONE DERECHOS RESERVADOS
		</div>

	</body>

</html>