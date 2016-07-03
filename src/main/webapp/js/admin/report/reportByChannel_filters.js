$(document).ready(function() {
	
	$("#dialog-report-exists").dialog({
		resizable : false,
		width : 350,
		modal : true,
		autoOpen : false
	});
	
} );

function checkExistence(element) {
	
	$("#main-msg").html("");
	
	if ( $("input[name='endDate']").val() == '' || $("select[name='timePeriod']").val() == '') {
		$("#validation-msg").show();
		
	} else {
	
		$("#validation-msg").hide();
		
		$.get( $("#check_existence_url").val(), $("#reportByChannelForm").serialize(), function(data) {
			if (data != "") {
				
				$("#dialog-report-exists .message").html(data);
				$("#dialog-report-exists").dialog("open");
				
			} else {
				buildReport();
			}
		});
	}
	
}

function buildReport() {
	
	$('#dialog-report-exists').dialog('close');
	
	if ( $("input[name='endDate']").val() == '' || $("select[name='timePeriod']").val() == '') {
		$("#validation-msg").show();
		
	} else {
	
		$("#validation-msg").hide();
		$("#process-msg").show();
		
		$.get( $("#form_action").val(), $("#reportByChannelForm").serialize(), function(data) {
			$("#process-msg").hide();
			$("#main-msg").html(data);
		});
	}
	
}

function confirmarEliminarReporte(id, username) {
	
	$("#dialog-eliminar-id").val(id);
	$("#dialog-eliminar-username").val(username);
	$("#dialog-eliminar").dialog("open");
}

