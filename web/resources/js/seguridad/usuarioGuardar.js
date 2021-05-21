/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var oUsuarioGuardar = null;
var seleccionados = [];

var UsuarioGuardar = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",
        apis: {
            urlSingle: 'usuario/getSingle',
            urlGuardar: 'usuario/save',
            urlRol:'rol/search'
        },
        init: function () {
            var me = this;

            $('#btnGuardar').on('click', function () {
                me.guardar();
            });
            
            $('#selTod').click(function () {
                var isChecked = $(this).prop("checked");
                me.seleccionarTodos(isChecked);
            });
            
            $("#txtNumeroDocumento").on("keypress", function (e) {
                var evento = e || window.event;
                var codigoCaracter = evento.charCode || evento.keyCode;
                var caracter = String.fromCharCode(codigoCaracter);
                if (!["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"].includes(caracter)) {
                    return false;
                }
            });  
            
            $('#sltTipoDocumento').change(function () {
                if ($('#sltTipoDocumento').val() === '1') {
                    $("#txtNumeroDocumento").attr('maxlength', '8');
                } else if ($('#sltTipoDocumento').val() === '2'){
                    $("#txtNumeroDocumento").attr('maxlength', '11');
                }
                else{
                    $("#txtNumeroDocumento").attr('maxlength', '20');
                }
            });
            
            $("#chkCambiar").click(function (e) {
                var isChecked = $(this).prop("checked");
                if(isChecked){
                    $("#txtContrasenia").attr("disabled",false);
                    $("#divCambio").attr("hidden");
                }
                else{
                    $("#txtContrasenia").attr("disabled",true);
                    $("#divCambio").removeAttr("hidden");
                    $("#txtContrasenia").val("");
                }
            });
            
            me.getRoles();
        },
        valida: function () {
            var me = this;
            var result = true, mensaje = "";

            if ($("#txtNombre").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un Nombre de Usuario.";
                result = false;
            }
            else if ($("#txtNombre").val().length > 100) {
                mensaje += "El Nombre que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if (!($("#sltTipoDocumento").val() === "1" || $("#sltTipoDocumento").val() === "2")) {
                mensaje += "Has omitido seleccionar un tipo de documento.";
                result = false;
            }
            else if ($("#sltTipoDocumento").val() === "1" && !$("#txtNumeroDocumento").val().trim().length === 8) {
                mensaje += "Debe ingresar un n\u00famero de dni de 8 dig\u00edtos.";
                result = false;
            }
            else if ($("#sltTipoDocumento").val() === "2" && !$("#txtNumeroDocumento").val().trim().length === 11) {
                mensaje += "Debe ingresar un n\u00famero de ruc de 11 dig\u00edtos.";
                result = false;
            }

            if ($("#txtNumeroDocumento").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un n\u00famero de documento.";
                result = false;
            }

            if ($("#txtCorreo").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un correo electr\u00f3nico.";
                result = false;
            }
            else if ($("#txtCorreo").val().length > 100) {
                mensaje += "El Correo electr\u00f3nico que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }
            else if (!me.validateEmail($("#txtCorreo").val().trim()) ) {
                mensaje += "Debe ingresar un correo electr\u00f3nico v\u00e1llido.";
                result = false;
            }

            if ($("#id").val().trim().length === 0) {
                if ($("#txtContrasenia").val().trim().length === 0) {
                    mensaje += "Has omitido ingresar una contrase\u00f1a.";
                    result = false;
                }
                else if ($("#txtContrasenia").val().length > 100) {
                    mensaje += "La Contrase\u00f1a que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                    result = false;
                }
            }

            if ($("#txtTelefono").val().trim().length !== 0 &&
                (!($("#txtTelefono").val().trim().length === 7 || $("#txtTelefono").val().trim().length === 9) || !me.validateNumber($("#txtTelefono").val().trim()))) {
                mensaje += "Debe ingresar un n\u00famero de tel\u00e9fono v\u00e1lido.";
                result = false;
            }

            if (!($("#sltEstado").val() === "1" || $("#sltEstado").val() === "2")) {
                mensaje += "Has omitido seleccionar un estado.";
                result = false;
            }

            if (seleccionados.length === 0) {
                mensaje += "Has omitido seleccionar al menos un Rol para el nuevo Usuario.";
                result = false;
            }
            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;
        },
        validateNumber: function(num){
            var re = /^\d+$/;
            return re.test(num);
        },
        validateEmail: function(email){
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(email).toLowerCase());
        },
        setEntity: function (rpta) {
            var me = this;
            console.log(rpta);
            $("#txtCodigo").val(rpta.codigo);
            $("#txtNombre").val(rpta.nombre);
            $("#sltTipoDocumento").val(rpta.tipoDocumento);
            $("#txtNumeroDocumento").val(rpta.numeroDocumento);
            $("#txtCorreo").val(rpta.contenido);
            $("#txtContrasenia").val(rpta.clave);
            $("#txtTelefono").val(rpta.telefono);
            $("#sltEstado").val(rpta.estado);
            $("#lblEditarContrasenia").text("Editar Contraseña");

            for (var i = 0; i < rpta.roles.length; i++) {
                seleccionados.push(rpta.roles[i].id);
                $("#" + rpta.roles[i].id + " input:checkbox").prop("checked", true);
            }
            
            $("#txtContrasenia").attr("disabled",true);
            $("#divCambio").removeAttr("hidden");
            
            if ($('#sltTipoDocumento').val() === '1') {
                $("#txtNumeroDocumento").attr('maxlength', '8');
            } else if ($('#sltTipoDocumento').val() === '2'){
                $("#txtNumeroDocumento").attr('maxlength', '11');
            }
            else{
                $("#txtNumeroDocumento").attr('maxlength', '20');
            }
        },
        getEntity: function () {
            var oEntity = null;
            var id = $('#id').val().replaceAll("/","");
            var nombre = $("#txtNombre").val();
            var tipoDocumento = parseInt($("#sltTipoDocumento").val(), 10);
            var numeroDocumento = $('#txtNumeroDocumento').val();
            var correo = $('#txtCorreo').val();
            var contrasenia = $('#txtContrasenia').val();
            var telefono = $('#txtTelefono').val();
            var roles = seleccionados;
            var estado = $("#sltEstado").val();
            var cambiarContrasenia = $("#chkCambiar").is(":checked");
            
            oEntity = {
                id: id,
                nombre: nombre,
                tipoDocumento: tipoDocumento,
                numeroDocumento: numeroDocumento,
                correo: correo,
                contrasenia: contrasenia,
                telefono: telefono,
                estado: estado,
                roles: roles,
                cambiarContrasenia: cambiarContrasenia
            };
            
            return oEntity;
        },
        guardar: function () {
            var me = this;
            var data = me.getEntity();
            console.log(data);
            if (me.valida()){
                var exito = function(rpta){
                    if(rpta.apiEstado === "ok"){
                        BI.defaultSuccess(rpta);
                        
                        let id = "";
                        let codigo = "";
                        id = rpta.id;
                        codigo = String(rpta.codigo);
                        if (id.length !== 0) {
                            $("#titulo").text("Editar Usuario");
                            $('#id').val(id);
                        }

                        if (codigo.length !== 0) {
                            $('#txtCodigo').val(codigo);
                        }
                    }else{
                        BI.MostrarPopupError(rpta.apiMensaje);
                    }
                };
                
                $.ajax({
                    url: me.contextUrl + me.apis.urlGuardar,
                    type: 'post',
                    data: {data: JSON.stringify(data)},
                    dataType: 'json',
                    beforeSend: function(){

                    },
                    success: exito,
                    error: function(error){
                        console.log(error);
                    }
                });
            }
        },
        getSingle: function () {
            var me = this;
            let id = '';
            id = $("#id").val().replaceAll("/","");
            
            if (id !== '') {
                $("#titulo").text("Editar Usuario");
                var apiUrl = me.contextUrl + me.apis.urlSingle;
                var url = `${apiUrl}?id=${id}`;
                
                $.ajax({
                    url: url,
                    type: 'get',
                    dataType: 'json',
                    beforeSend: function(){

                    },
                    success: function (rpta) {
                        me.setEntity(rpta);
                    },
                    error: function(error){
                        console.log(error);
                        BI.MostrarMensajeError(error.apiMensaje);
                    }
                  });
            } else {
                $("#divLoading").css('display', 'none');
            }
        },
        getRoles: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlRol;
            var url = `${apiUrl}?texto=&ordenamiento=&pagina=0&tamanio=0`;
            
            var exito = function(rpta){
                if(rpta.apiEstado === 'ok'){
                    let data = Array();
                    data = rpta.data;
                    $("#tablaSeleccioneMenus tbody").empty();
                    if (data.length !== 0) {
                        data.forEach(function (element) {
                            $("#tablaSeleccioneRoles tbody")
                                .append($("<tr id='" + element.id + "'>")                    
                                    .append($('<td>')
                                        .append($('<input type="checkbox" onClick="oUsuarioGuardar.Core.seleccionar(\'' + element.id + '\')" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px"></input>')))                    
                                    .append($("<td>").text(element.nombre))
                                );
                        });
                    } else {
                        $("#tablaSeleccioneRoles tbody")
                            .append($("<tr>")
                                .append($("<td>").attr("colspan", "2").append($("<div>").addClass("text-center").text(rpta.apiMensaje))));
                    }

                   me.getSingle();
                } 
            };

            $.ajax({
                url: url,
                type: 'get',
                dataType: 'json',
                beforeSend: function(){

                },
                success: exito,
                error: function(error){
                    console.log(error);
                }
            });
        },
        seleccionarTodos: function(isChecked){
            var me = this;
            $('#tablaSeleccioneRoles tr:has(td)').find('input[type="checkbox"]').prop('checked', isChecked);
            $('#tablaSeleccioneRoles tr:has(td)').each(function (index, ele) {
                me.seleccionar($(ele).attr("id"));

            });
        },
        seleccionar: function(id){
            if (!seleccionados.includes(id)) {
                seleccionados.push(id);
            } else {
                for (var i = 0; i < seleccionados.length; i++) {
                    if (seleccionados[i] === id) {
                        seleccionados.splice(i, 1);
                        break;
                    }
                }
            }
        }
    };
};

$(function (e) {
    oUsuarioGuardar = new UsuarioGuardar();
    oUsuarioGuardar.Core.init();
});
