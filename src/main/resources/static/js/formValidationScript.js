/**
 * Created by olaskierbiszewska on 29.11.15.
 */
$(document).ready(function () {
    $('#myform').validate({ // initialize the plugin
        rules: {
            dostawaRadios: {
                required: true,
                minlength: 5
            },typeRadios: {
                required: true,
                minlength: 5
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
            messages: {
                name: "Podaj e-mail",
                email: {
                    required: "Potrzebujemy Twojego adresu e-mail w celu wysłania informacji o zamówieniu",
                    email: "E-mail powinien mieć format name@domain.com"
                }
            },
            inputRegulamin: {
                required: true,
                minlength: 5
            },

        },
        submitHandler: function (form) { // for demo
            alert('valid form submitted'); // for demo
            return false; // for demo
        }
    });

});