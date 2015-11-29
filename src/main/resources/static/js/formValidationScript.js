/**
 * Created by olaskierbiszewska on 29.11.15.
 */
$(document).ready(function () {
    $('#myform').validate({ // initialize the plugin
        rules: {
            dostawaRadios: {
                required: true
            },typeRadios: {
                required: true
            },
            inputName: {
                required: true,
                minlength: 5
            },
            inputAddress: {
                required: true,
                minlength: 5
            },
            inputPostalCode: {
                required: true,
                minlength: 5
            },
            inputCity: {
                required: true,
                minlength: 5
            },
            inputMail: {
                required: true,
                email: true
            },
            inputRegulamin: {
                required: true
            },

        },
        //submitHandler: function (form) {
        //    alert('Zamówienie złorzone poprawnie');
        //    return false;
        //}
    });

});