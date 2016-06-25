$(document).ready(function() {
	
	$("#dialog-report-exists").dialog({
		resizable : false,
		width : 350,
		modal : true,
		autoOpen : false
	});
	
} );

function checkExistence(element) {
	
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

/*
function clearSelection() {
	clearTimeout(timer);
	$("#archivar_reporte").hide();
	$("#crear_reporte").show();
}

function enableSaveButton() {

	$.get( $("#enable_save_url").val() , function(data) {
		if (data == true) {
			$("#save_button").removeAttr("disabled");
			
		} else {
			timer = setTimeout(enableSaveButton, 1000);
		}
	});
	
}


function confirmDeleteWeeklyReport(id) {
	
	$("#dialog-eliminar-id").val(id);
	$("#dialog-eliminar form").attr("action", $("#contexto").val() + "report/weekly/delete");
	$("#dialog-eliminar").dialog("open");
}

function confirmDeleteMonthlyReport(id) {
	
	$("#dialog-eliminar-id").val(id);
	$("#dialog-eliminar form").attr("action", $("#contexto").val() + "report/monthly/delete");
	$("#dialog-eliminar").dialog("open");
}
*/

