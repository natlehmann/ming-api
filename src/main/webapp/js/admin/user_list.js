$(document).ready(function() {
    $('.datatable').dataTable( {
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": $("#contexto").val() + "admin/user/list_ajax",
        "bJQueryUI": true,
        "sPaginationType": "full_numbers",
        "oLanguage": {
            "sUrl": $("#contexto").val() + "js/datatables_ES.txt"
        },
        "columnDefs": [ { "targets": 4, "orderable": false }, { "targets": 5, "orderable": false } ],
        "bAutoWidth" : false,
        "aoColumns": [
                      {"sWidth" : "10%"},
                      {"sWidth" : "30%" },
                      {"sWidth" : "30%" },
                      {"sWidth" : "10%" },
                      {"sWidth" : "10%" },
                      {"sWidth" : "10%" }
                    ]
    } );
} );