$(function() {
	$("#dialog-eliminar").dialog({
		resizable : false,
		width : 350,
		modal : true,
		autoOpen : false
	});
	
	$(".datepicker").datepicker({
		dateFormat: 'dd/mm/yy'
	});
});

function ir(url) {
	window.location.assign(url);
}

function irAbsoluto(url) {
	window.location.assign($("#contexto").val() + url);
}

function confirmarEliminar(id) {
	
	$("#dialog-eliminar-id").val(id);
	$("#dialog-eliminar").dialog("open");
}