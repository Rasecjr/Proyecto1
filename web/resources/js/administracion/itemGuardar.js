/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oItemGuardar = null;

var ItemGuardar = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/item/",
        apis: {
            urlSingle: 'getSingle',
            urlGuardar: 'save'
        },
        init: function () {
            var me = this;

            $('#btnGuardar').on('click', function () {
                me.guardar();
            });
            
            me.getSingle();
        },
        valida: function () {
            var me = this;
            var result = true, mensaje = "";
            
            if ($("#sltClase").val() === "0") {
                mensaje += "Has omitido seleccionar una Clase de producto.";
                result = false;
            }

            if ($("#txtNombre").val().trim().length == 0) {
                mensaje += "Has omitido ingresar un Nombre de producto.";
                result = false;
            }
            else if ($("#txtNombre").val().length > 100) {
                mensaje += "El Nombre que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if ($("#txtNombreComercial").val().trim().length == 0) {
                mensaje += "Has omitido ingresar un Nombre Comercial de producto.";
                result = false;
            }
            else if ($("#txtNombreComercial").val().length > 100) {
                mensaje += "El Nombre Comercial que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if ($("#txtDescripcion").val().length > 100) {
                mensaje += "La Descripci\u00f3n que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if (!($("#sltEstado").val() === "1" || $("#sltEstado").val() === "2")) {
                mensaje += "Has omitido seleccionar un Estado v\u00e1lido.";
                result = false;
            }
            
            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;            
        },
        setEntity: function (rpta) {
            $("#txtCodigo").val(rpta.codigo);
            $("#txtNombre").val(rpta.nombre);
            $("#txtNombreComercial").val(rpta.nombreComercial);
            $("#txtDescripcion").val(rpta.descripcion);
            $("#sltClase").val(rpta.clase);
            $("#sltEstado").val(rpta.estado);
        },
        getEntity: function () {
            var oEntity = null;
            var id = $('#id').val().replaceAll("/","");
            var nombre = $('#txtNombre').val();
            var nombreComercial = $('#txtNombreComercial').val().trim();
            var descripcion = $('#txtDescripcion').val();
            var clase = $('#sltClase').val().trim();
            var estado = $('#sltEstado').val();

            oEntity = {
                id: id,
                nombre: nombre,
                nombreComercial: nombreComercial,
                descripcion: descripcion,
                clase: clase,
                estado: estado
            };

            return oEntity;
        },
        guardar: function () {
            var me = this;
            var data = me.getEntity();
            
            if (me.valida()){
                
                var exito = function(rpta){
                    if(rpta.apiEstado === "ok"){
                        BI.defaultSuccess(rpta);
                        
                        let id = "";
                        let codigo = "";
                        id = rpta.id;
                        codigo = String(rpta.codigo);
                        if (id.length !== 0) {
                            $("#titulo").text("Editar Producto");
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
                $("#titulo").text("Editar Producto");
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
        }
    };
};

$(function (e) {
    oItemGuardar = new ItemGuardar();
    oItemGuardar.Core.init();
});
