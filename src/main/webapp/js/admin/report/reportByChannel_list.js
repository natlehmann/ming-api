$(document).ready(function() {
    $('.datatable-weekly').dataTable( {
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": $("#contexto").val() + "report/byChannel/list_ajax",
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "oLanguage": {
            "sUrl": $("#contexto").val() + "js/datatables_ES.txt"
        },
        "columnDefs": [ { "targets": 4, "orderable": false } ],
        "bAutoWidth" : false,
        "aoColumns": [
                      {"sWidth" : "9%"},
                      {"sWidth" : "27%"},
                      {"sWidth" : "27%"},
                      {"sWidth" : "27%"},
                      {"sWidth" : "10%" }
                    ]
    } );
} );