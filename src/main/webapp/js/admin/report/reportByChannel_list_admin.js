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
        "columnDefs": [ { "targets": 5, "orderable": false } ],
        "bAutoWidth" : false,
        "aoColumns": [
                      {"sWidth" : "8%"},
                      {"sWidth" : "21%"},
                      {"sWidth" : "15%"},
                      {"sWidth" : "15%"},
                      {"sWidth" : "33%"},
                      {"sWidth" : "8%" }
                    ]
    } );
} );