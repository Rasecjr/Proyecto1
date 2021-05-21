/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var oPrecio= null;

var Precio = function(){
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
            urlListar: 'precio/search',
            urlExportar: 'precio/export',
            urlDelete: 'precio/delete',
            urlCombo:'item/search'
        },
        init: function () {
            var me = this;

            $('thead tr th a').on('click', function (ev) {
                me.ordenar(ev);
                me.buscar();
            });

            $('ul li button').on('click', function (ev) {
                me.mostrarPagina(ev);
                me.buscar();
            });

            $('#numeropagina').on('change', function () {
                me.listParams.paginacion.pagina = parseInt(this.value);
                me.buscar();
            });

            $("#btnExportar").click(function () {
                me.exportarExcel();
            });

            $('#btnBuscar').click(function () {
                me.buscar();
            });

            $("#tablaResultado").on('click', '.eliminar', function (ev) {
                var currentRow = $(this).closest("tr");
                var col2 = currentRow.find("td:eq(1)").children("span");
                var data = col2.attr('data-id');
                me.eliminar(data);
            });
            
            me.listarProducto();
            me.buscar();
        },
        getParams: function () {
            var oEntity = null;
            var idItem = $("#producto").val();
            var clase =  $("#sltClase").val();
            var texto = "";

            oEntity = {
                idItem : idItem,
                clase : clase,
                texto: texto
            };

            return oEntity;
        },
        buscar: function () {
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
                            .append($('<td>')
                                .append($('<a href="createPrecio?id=' + element.id + '" class="btn btn-outline-primary editar" style="margin: 2px 2px 2px 2px; padding: 2px 2px 2px 2px" data-id="' + element.id + '" ><span class="material-icons">edit</span></span>')))
                            .append($('<td>')
                                .append($('<span class="btn btn-outline-danger eliminar" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px" data-id="' + element.id + '"><span class="material-icons">remove</span></span>')))
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
        },
        ordenar: function (ev) {
            this.listParams.ordenamiento = BI.ordenar(ev, this.listParams.ordenamiento, this);
        },
        mostrarPagina: function (ev) {
            BI.mostrarPagina(ev, this);
        },
        eliminar: function (id) {
            var me = this;
            Swal.fire({
                title: "",
                text: BI.ConfirmaEliminar("este Precio"),
                icon: "warning",
                dangerMode: true,
                showCancelButton: true,
                buttons: ["Cancelar", "Eliminar"]
            }).then((result) => {
                if (result.isConfirmed) {
                    $("#divLoading").show();
                    var url = me.contextUrl + me.apis.urlDelete; 

                    $.ajax({
                        url: url,
                        type: 'post',
                        data: { id: id },
                        dataType: 'json',
                        beforeSend: function(){

                        },
                        success: function (rpta) {
                            BI.defaultSuccess(rpta);
                            me.buscar();
                        },
                        error: function(error){
                            BI.defaultError(error);
                        }
                    });

                }
            });
        },
        exportarExcel: function () {
            var me = this;
            var nameFile = "Precio";
            var oParam = me.getParams();
            var apiUrl = me.contextUrl + me.apis.urlListar;
            var url = `${apiUrl}?idItem=${oParam.idItem}&clase=${oParam.clase}&texto=${oParam.texto}&ordenamiento=${me.listParams.ordenamiento}&pagina=${0}&tamanio=${0}`;
            
            var exito = function(rpta){
                var data = rpta.data;
                var table='';
                if (data.length !== 0) {
                    table += "<tr>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Codigo</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Nombre</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Clase</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Precio</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Vigencia Desde</td>";
                    table += "</tr>";    
                    data.forEach(function (element) { 
                        var precio = (Math.round(element.precioBase * 100) / 100).toFixed(2);
                        table += "<tr>";
                        table += "<td>"+element.codigo+"</td>";
                        table += "<td>"+element.nombre+"</td>";
                        table += "<td>"+element.clase+"</td>";
                        table += "<td>"+precio+"</td>";
                        table += "<td>"+element.vigenciaDesde+"</td>";
                        table += "</tr>";    
                    });
                    BI.tableToExcel(table, nameFile);
                }
                else {
                    BI.MostrarPopupError(rpta.apiMensaje);
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
        listarProducto: function(){
            var me = this;
            var oParam = me.getParams();
            var apiUrl = me.contextUrl + me.apis.urlCombo;
            var url = `${apiUrl}?texto=${oParam.texto}&ordenamiento=${me.listParams.ordenamiento}&pagina=${me.listParams.paginacion.pagina}&tamanio=${me.listParams.paginacion.limite}`;
            
            var exito = function(rpta){
                let data = Array();
                data = rpta.data;
                var mySelect = $('#producto');
                console.log(data);
                data.forEach(function (element, index) {
                    mySelect.append(
                        $('<option></option>').val(element.id).html(element.nombre)
                    );
                });
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

$(function(e){
    oPrecio = new Precio();
    oPrecio.Core.init();
});  
