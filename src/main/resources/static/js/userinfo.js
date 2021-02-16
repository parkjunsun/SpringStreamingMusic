$(document).ready(function(){
    $('#allClick').click(function () {
        if ($("input:checkbox[id='allClick']").prop("checked")){
            $("input[type=checkbox]").prop("checked", true);
        } else {
            $("input[type=checkbox]").prop("checked", false);
        }
    });
 });