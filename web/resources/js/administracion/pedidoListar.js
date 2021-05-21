/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var oPedido = null;

var Pedido = function(){
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",
        listParams: {
            ordenamiento: 'fechaPedido desc',
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
            urlListar: 'pedido/search',
            urlExportar: 'pedido/export',
            urlDelete: 'pedido/delete',
            urlZonal:'zonalVenta/search',
            urlUbigeo:'ubigeo/search'
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
            
            me.loadDistrito();
            me.loadZonalVenta();
            me.buscar();
        },
        getParams: function () {
            var oEntity = null;
            var distrito = $('#distrito').val();
            var estado = $('#estado').val();
            var fechainicio = $("#ini").val() === "" ? "" : BI.convertFecha($("#ini").val(), 1);
            var fechafin = $("#fin").val() === "" ? "" : BI.convertFecha($("#fin").val(), 1);           
            var zonalventa = $('#sltZonalVenta').val();

            oEntity = {
                distrito : distrito,
                estado : estado,
                fechaInicio: fechainicio,
                fechaFin: fechafin,
                zonal: zonalventa
            };

            return oEntity;
        },
        buscar: function () {
            var me = this;
            var oParam = me.getParams();
            var apiUrl = me.contextUrl + me.apis.urlListar;
            var url = `${apiUrl}?distrito=${oParam.distrito}&estado=${oParam.estado}&fechaInicio=${oParam.fechaInicio}&fechaFin=${oParam.fechaFin}&zonal=${oParam.zonal}&ordenamiento=${me.listParams.ordenamiento}&pagina=${me.listParams.paginacion.pagina}&tamanio=${me.listParams.paginacion.limite}`;
            
            var exito = function(rpta){
                console.log(rpta);
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
                        
                        $("#tablaResultado tbody")
                        .append($("<tr>")
                            .append($('<td>')
                                .append($('<a href="createPedido?id=' + element.id + '" class="btn btn-outline-primary editar" style="margin: 2px 2px 2px 2px; padding: 2px 2px 2px 2px" data-id="' + element.id + '" ><span class="material-icons">edit</span></span>')))
                            .append($('<td>')
                                .append($('<span class="btn btn-outline-danger eliminar" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px" data-id="' + element.id + '"><span class="material-icons">remove</span></span>')))
                            .append($("<td>").text(element.codigo))
                            .append($("<td>").text(element.distrito))
                            .append($("<td>").text(element.zonal))
                            .append($("<td>").text(element.fecha))
                            .append($("<td style='text-align: right;'>").text(element.cantidad))
                            .append($("<td>").text(element.fechaPedido))
                            .append($("<td>").text(element.nombreEstado))
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
                text: BI.ConfirmaEliminar("este Pedido"),
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
        loadDistrito: function(){
            var me = this;
         
            var exito = function(rpta){
                if(rpta.success){
                   let data = Array();
                   data = rpta.data;
                   var mySelect = $('#distrito');
                   data.forEach(function (element, index) {
                       mySelect.append(
                           $('<option></option>').val(element.IdUbigeo).html(element.Nombre)
                       );
                   });
                } 
            };
          
            $.ajax({
                url: me.contextUrl + me.apis.urlUbigeo,
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
        loadZonalVenta: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlZonal;
            var url = `${apiUrl}?texto=&ordenamiento=nombre&pagina=0&tamanio=0`;
         
            var exito = function(rpta){
                
                if(rpta.success){
                   let data = Array();
                   data = rpta.data.data;
                   console.log(data);
                   var mySelect = $('#sltZonalVenta');
                   data.forEach(function (element, index) {
                       mySelect.append(
                           $('<option></option>').val(element.id).html(element.nombre)
                       );
                   });
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
        exportarExcel: function () {
            var me = this;
            var nameFile = "Item";
            var oParam = me.getParams();
            var apiUrl = me.contextUrl + me.apis.urlListar;
            var url = `${apiUrl}?distrito=${oParam.distrito}&estado=${oParam.estado}&fechaInicio=${oParam.fechaInicio}&fechaFin=${oParam.fechaFin}&zonal=${oParam.zonal}&ordenamiento=${me.listParams.ordenamiento}&pagina=${0}&tamanio=${0}`;

            var exito = function(rpta){
                var data = rpta.data;
                var table='';
                if (data.length !== 0) {
                    table += "<tr>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Codigo</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Distrito</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Zonal de Venta</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Fecha Pedido</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Total Productos</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Fecha y Hora</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Estado</td>";
                    table += "</tr>";    
                    data.forEach(function (element) { 
                        table += "<tr>";
                        table += "<td>"+element.codigo+"</td>";
                        table += "<td>"+element.distrito+"</td>";
                        table += "<td>"+element.zonal+"</td>";
                        table += "<td>"+element.fecha+"</td>";
                        table += "<td>"+element.cantidad+"</td>";
                        table += "<td>"+element.fechaPedido+"</td>";
                        table += "<td>"+element.nombreEstado+"</td>";
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
        }
    };
};

$(function(e){
    oPedido = new Pedido();
    oPedido.Core.init();
});  




