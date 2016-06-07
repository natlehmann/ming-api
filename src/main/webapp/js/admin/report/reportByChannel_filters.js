$(document).ready(function() {
	
	$("#dialog-report-exists").dialog({
		resizable : false,
		width : 350,
		modal : true,
		autoOpen : false
	});
	
} );

/*
function checkReportExistence(element) {
	
	$("#action").val( $(element).val() );
	
	$.get( $("#check_existence_url").val(), $("#form").serialize(), function(data) {
		if (data != "") {
			
			$("#dialog-report-exists .message").html(data);
			$("#dialog-report-exists").dialog("open");
			
		} else {
			saveReport();
		}
	});
}

function saveReport() {
	$("#form").attr("action", $("#form_action").val() );
	$("#form").submit();
}

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

