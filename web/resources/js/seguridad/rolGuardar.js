/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var oRolGuardar = null;
var seleccionados = [];

var RolGuardar = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",
        apis: {
            urlSingle: 'rol/getSingle',
            urlGuardar: 'rol/save',
            urlMenu:'menu/search'
        },
        init: function () {
            var me = this;

            //$('#txtNombre').inputText();
            $('#btnGuardar').on('click', function () {
                me.guardar();
            });
            
            $('#selTod').click(function () {
                var isChecked = $(this).prop("checked");
                me.seleccionarTodos(isChecked);
            });
            
            me.getMenus();
        },
        valida: function () {
            var result = true, mensaje = "";

            if ($("#txtNombre").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un Nombre de Rol.";
                result = false;
            }
            if ($("#txtNombre").val().length > 100) {
                mensaje += "El Nombre que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if (seleccionados.length === 0) {
                mensaje += "Has omitido seleccionar al menos una opci\u00f3n del men\u00fa.";
                result = false;
            }

            if (!($("#sltEstado").val() === "1" || $("#sltEstado").val() === "2")) {
                mensaje += "Has omitido seleccionar un Estado.";
                result = false;
            }

            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;
        },
        setEntity: function (rpta) {
            var me = this;
            $("#txtCodigo").val(rpta.codigo);
            $("#txtNombre").val(rpta.nombre);
            $("#sltEstado").val(rpta.estado);
            console.log(rpta);
            for (var i = 0; i < rpta.menus.length; i++) {
                seleccionados.push(rpta.menus[i].id);
                $("#" + rpta.menus[i].id + " input:checkbox").prop("checked", true);
            }
        },
        getEntity: function () {
            var oEntity = null;
            var id = $('#id').val().replaceAll("/","");
            var nombre = $("#txtNombre").val();
            var estado = $("#sltEstado").val();
            var menus = seleccionados;

            oEntity = {
                id: id,
                nombre: nombre,
                estado: estado,
                menus: menus
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
                            $("#titulo").text("Editar Rol");
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
                $("#titulo").text("Editar Rol");
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
        getMenus: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlMenu;
            var url = `${apiUrl}?texto=&ordenamiento=&pagina=0&tamanio=0`;
            
            var exito = function(rpta){
                if(rpta.apiEstado === 'ok'){
                    let data = Array();
                    data = rpta.data;
                    $("#tablaSeleccioneMenus tbody").empty();
                    if (data.length !== 0) {
                        data.forEach(function (element) {
                            $("#tablaSeleccioneMenus tbody")
                                .append($("<tr id='" + element.id + "'>")                    
                                    .append($('<td>')
                                        .append($('<input type="checkbox" onClick="oRolGuardar.Core.seleccionar(\'' + element.id + '\')" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px"></input>')))
                                    .append($("<td>").text(element.codigo))
                                    .append($("<td>").text(element.nombre))
                                    .append($("<td>").text(element.modulo))
                                    .append($("<td>").text(element.fechaCreacion))
                                    .append($("<td>").text(element.estado))
                                );
                        });
                    } else {
                        $("#tablaSeleccioneMenus tbody")
                            .append($("<tr>")
                                .append($("<td>").attr("colspan", "6").append($("<div>").addClass("text-center").text(rpta.apiMensaje))));
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
            $('#tablaSeleccioneMenus tr:has(td)').find('input[type="checkbox"]').prop('checked', isChecked);
            $('#tablaSeleccioneMenus tr:has(td)').each(function (index, ele) {
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
    oRolGuardar = new RolGuardar();
    oRolGuardar.Core.init();
});

