/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oPrecioGuardar = null;

var PrecioGuardar = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",
        listParams: {
            ordenamiento: 'fecha desc',
            paginacion: {
                pagina: 1,
                totalPaginas: 1,
                limite: 8
            },
            primero : "btnPrimero",
            anterior : "btnAnterior",
            siguiente : "btnSiguiente",
            ultimo : "btnUltimo"
        },  
        apis: {
            urlSingle: 'precio/getSingle',
            urlGuardar: 'precio/save',
            urlCombo:'item/search',
            urlListar: 'precio/search'
        },
        init: function () {
            var me = this;

            $('#btnGuardar').on('click', function () {
                me.guardar();
            });
            
            me.getItem();
        },
        valida: function () {
            var me = this;
            var result = true, mensaje = "";
            
            if ($("#sltProducto").val() === "") {
                mensaje += "Has omitido seleccionar un producto.";
                result = false;
            }
            if ($("#txtPrecioBase").val().trim().length === 0 || $("#txtPrecioBase").val()=== "0") {
                mensaje += "Has omitido ingresar un precio base.";
                result = false;
            }
            if ($("#txtVigenciaDesde").val().trim().length === 0) {
                mensaje += "Has omitido elegir una Fecha válida.";
                result = false;
            }
            else{
                var d = new Date();
                var fechaActual = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
                
                if($("#txtVigenciaDesde").val()<fechaActual){
                    mensaje += "Fecha de Vigencia menor a la actual.";
                    result = false;
                }
            }
            
            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;     
        },
        setEntity: function (rpta) {
            var me = this;
            
            $("#sltProducto").val(rpta.idItem);
            $("#txtPrecioBase").val(rpta.precio);
            $("#txtVigenciaDesde").val(BI.convertFecha(rpta.fechaVigencia));
            
            me.buscarHistorial();
        },
        getEntity: function () {
            var oEntity = null;
            var id = $('#id').val().replaceAll("/","");
            var idItem = $("#sltProducto").val();
            var precio = $("#txtPrecioBase").val();
            var fechaVigencia = BI.convertFecha($("#txtVigenciaDesde").val(), 1);

            oEntity = {
                id: id,
                idItem: idItem,
                precio: precio,
                fechaVigencia: fechaVigencia
            };

            return oEntity;
        },
        guardar: function () {
            var me = this;
            var data = me.getEntity();
            
            if (me.valida()){
                
                var exito = function(rpta){
                    if(rpta.apiEstado === "ok"){
                        console.log(rpta.apiEstado);
                        BI.defaultSuccess(rpta);
                        
                        let id = "";
                        let codigo = "";
                        id = rpta.id;
                        codigo = String(rpta.codigo);

                        if (id.length !== 0) {
                            $("#titulo").text("Editar Precio Vigencia");
                            $('#id').val(id);
                        }

                        if (codigo.length !== 0) {
                            $('#txtCodigo').val(codigo);
                        }
                        me.buscarHistorial();
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
                $("#titulo").text("Editar Precio Vigencia");
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
        getItem: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlCombo;
            var url = `${apiUrl}?texto=&ordenamiento=&pagina=0&tamanio=0`;
         
            var exito = function(rpta){
               if(rpta.apiEstado === 'ok'){
                  let data = Array();
                  data = rpta.data;
                  var mySelect = $('#sltProducto');
                  data.forEach(function (element, index) {
                      mySelect.append(
                          $('<option></option>').val(element.id).html(element.nombre)
                      );
                  });
                  
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
        getParams: function () {
            var oEntity = null;
            var idItem = $("#sltProducto").val();
            var clase = "";
            var texto = "";
            
            oEntity = {
                idItem : idItem,
                clase : clase,
                texto: texto
            };

            return oEntity;
        },
        buscarHistorial: function(){
            var me = this;
            var oParam = me.getParams();
            var apiUrl = me.contextUrl + me.apis.urlListar;
            var url = `${apiUrl}?idItem=${oParam.idItem}&clase=${oParam.clase}&texto=${oParam.texto}&ordenamiento=${me.listParams.ordenamiento}&pagina=${me.listParams.paginacion.pagina}&tamanio=${me.listParams.paginacion.limite}`;
            
            var exito = function(rpta){
                $("#formulario").hide();
                $('#totalRegistros').text(rpta.total);
                me.listParams.paginacion.totalPaginas = Math.ceil((rpta.total / me.listParams.paginacion.limite));
                $('#totalPagina').text(me.listParams.paginacion.totalPaginas);

                var mySelect = $('#numeropagina');
                mySelect.empty();

                for (let index = 1; index <= me.listParams.paginacion.totalPaginas; index++) {
                    mySelect.append(
                        $('<option></option>').val(index).html(index)
                    );
                }

                mySelect.val(me.listParams.paginacion.pagina);

                var data = rpta.data;
                $("#totalRegistros").text(rpta.total);
                $("#tablaResultado tbody").empty();
                console.log(data);
                if (data.length !== 0) {
                    data.forEach(function (element) { 
                        var precio = (Math.round(element.precioBase * 100) / 100).toFixed(2);
                        $("#tablaResultado tbody")
                        .append($("<tr>")
                            .append($("<td>").text(element.codigo))
                            .append($("<td>").text(element.nombre))
                            .append($("<td>").text(element.clase))
                            .append($('<td style="text-align: right;">').text(precio))
                            .append($("<td>").html("&nbsp;&nbsp;&nbsp;" + element.vigenciaDesde))
                        );
                    });
                }
                else {
                    BI.defaultTable("tablaResultado",rpta);
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
        }
    };
};

$(function (e) {
    oPrecioGuardar = new PrecioGuardar();
    oPrecioGuardar.Core.init();
});

