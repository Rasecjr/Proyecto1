/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oPedidoGuardar = null;
var items = 0;
var mapmodal;
var marker = null;
var oZonales = [];
var distritos = [];

var PedidoGuardar = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",  
        oComprobante : null,
        productos:[],
        productosP:[],
        sltProducto:null,
        apis: {
            urlSingle: 'pedido/getSingle',
            urlGuardar: 'pedido/save',
            urlProductos:'pedido/searchItems',
            urlTarifa: 'pedido/getSingleTarifa',
            urlZonal:'zonalVenta/search',
            urlValidaCobertura:'zonalVenta/validaCobertura',
            urlCoordenadas: 'zonalVenta/coordenadas',
            urlDistrito: 'ubigeo/search'
        },
        init: function () {
            var me = this;
            var f = new Date();
            var toDay = ("00" + f.getDate()).slice(-2) + "/" + ("00" + (f.getMonth() + 1)).slice(-2) + "/" + f.getFullYear();
            $("#dtFecha").val(BI.convertFecha(toDay));

            $('#btnGuardar').on('click', function () {
                me.guardar();
            });
            
            $("#addItem").click(function () {
                me.agregarProducto({},"add");
            });
            
            $('#btnAgregarDireccion').click(function () {
                var latitud = document.getElementById('latitud').value;
                var longitud = document.getElementById('longitud').value;
                me.validaCobertura(latitud,longitud);        
            });
            
            $('#btnGuardarComprobante').click(function () {
                me.guardarDatosComprobante();
            });
            
            $("#txtTelefono, #txtMovil, #txtNumeroDocumentoCP,#txtNumeroDocumento").on("keypress", function (e) {
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
                } else {
                    $("#txtNumeroDocumento").attr('maxlength', '11');
                }
            });

            $('#sltTipoDocumentoCP').change(function () {
                if ($('#sltTipoDocumentoCP').val() === '1') {
                    $("#txtNumeroDocumentoCP").attr('maxlength', '8');
                } else {
                    $("#txtNumeroDocumentoCP").attr('maxlength', '11');
                }
            });
            
            me.guardarCambiosDeLista();
            me.loadZonalVenta();
            me.initMapa('-12.062106', '-77.036526');
        },
        valida: function () {
            var me = this;
            var result = true, mensaje = "";
            
            if ($("#sltTipoDocumento").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Tipo de Documento.";
                result = false;
            }

            if ($("#txtNumeroDocumento").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un n\u00famero de documento.";
                result = false;
            }
            else if ($("#sltTipoDocumento").val() === "1" && $("#txtNumeroDocumento").val().trim().length !== 8) {

                mensaje += "Has omitido ingresar un n\u00famero de documento DNI de 8 dig\u00edtos.";
                result = false;
            }
            else if ($("#sltTipoDocumento").val() === "2" && $("#txtNumeroDocumento").val().trim().length !== 11) {
                mensaje += "Has omitido ingresar un n\u00famero de documento RUC de 11 dig\u00edtos.";
                result = false;
            }

            if ($("#txtMovil").val().trim().length !== 0 &&
                !($("#txtMovil").val().trim().length === 9 || !esNumero($("#txtMovil").val().trim()))) {
                mensaje += "Has omitido ingresar un numero de m\u00f3vil de 9 digitos.";
                result = false;
            }

            if ($("#txtTelefono").val().trim().length !== 0 &&
                !($("#txtTelefono").val().trim().length === 7 || !esNumero($("#txtTelefono").val().trim()))) {
                mensaje += "Has omitido ingresar un n\u00famero de Tel\u00e9fono de 7 digitos.";
                result = false;
            }
            
            if ($("#txtEmail").val().trim().length !== 0 && !me.validateEmail($("#txtEmail").val().trim())) {
                mensaje += "Has ingresado un correo electr\u00f3nico no v\u00e1lido.";
                result = false;
            }
            else if ($("#txtEmail").val().trim().length !== 0 && $("#txtEmail").val().length > 100) {
                mensaje += "El correo electr\u00f3nico que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }
            
            if ($("#txtDireccion").val() === "") {
                mensaje += "Has omitido ingresar una direcci\u00f3n.";
                result = false;
            }
            
            if ($("#dtFecha").val().trim().length === 0) {
                mensaje += "Has omitido ingresar una Fecha.";
                result = false;
            }
            else{
                var d = new Date();
                var fechaActual = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
                
                if($("#dtFecha").val()<fechaActual){
                    mensaje += "Fecha de Pedido menor a la actual.";
                    result = false;
                }
            }
            
            if ($("#txtNombre").val().trim() === "") {
                mensaje += "Has omitido ingresar un Nombre.";
                result = false;
            }
            
            if ($("#txtDistrito").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Distrito.";
                result = false;
            }

            if ($("#sltEstado").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Estado.";
                result = false;
            }
            
            if ($("#sltZonalVenta").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Zonal de Venta.";
                result = false;
            }
            
            if ($("#txtReferencia").val().length > 100) {
                mensaje += "La referencia que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if ($('#tablaResultado tbody tr').length === 0) {
                mensaje += "Has omitido a\u00f1adir al menos un producto.";
                result = false;
            }
            else {
                var a = true, b = true; c = true;
                var prodList = [];
                $("#tablaResultado tbody tr").each(function () {
                    var tr = $(this);
                    var producto = me.getValorCol(tr, "producto");
                    var cantidad = me.getValorCol(tr, "cantidad");
                    var tarifa = me.getValorCol(tr, "tarifa");

                    prodList.push(producto);

                    if (!producto) {
                        a = false;
                    }
                    if (!cantidad) {
                        b = false;
                    }
                    if (!tarifa) {
                        c = false;
                    }
                });
                if (!a) {
                    mensaje += "Has omitido seleccionar un producto para la lista.";
                    result = false;
                }
                if (!b) {
                    mensaje += "Has omitido ingresar cantidad en al menos un producto de la lista.";
                    result = false;
                }
                if (!c) {
                    mensaje += "Hay productos con tarifa cero.";
                    result = false;
                }

                var newprodList = Array.from(new Set(prodList))       
                if(newprodList.length < prodList.length) {
                    mensaje += "Has ingresado un producto repetido.";
                    result = false;
                }
            }
            
            if ($("#sltMetodoPago").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un M\u00e9todo de pago.";
                result = false;
            }

            if ($("#sltComprobante").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Comprobante.";
                result = false;
            }
            
            if ($("#txtComentario").val().length > 100) {
                mensaje += "El comentario que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
                result = false;
            }

            if (me.oComprobante === null) {
                mensaje += "No olvides verificar los datos del comprobante antes de guardar este pedido.";
                result = false;
            }
            
            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;     
        },
        validateEmail: function(email){
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(email).toLowerCase());
        },
        getValorCol: function(tr, col){
            if (col === "producto") {
                return tr.find('td').eq(1).find('select').val().trim();
            }
            else if (col === "cantidad") {
                var strCant = tr.find('td').eq(2).find('input').val();
                if (!strCant.trim()) {
                    return 0;
                }
                else {
                    return parseInt(strCant, 10);
                }

            }
            else if (col === "tarifa") {
                return parseFloat(tr.find('td').eq(3).text().split('S/')[1].trim());
            }
            else if (col === "total") {
                return parseFloat(tr.find('td').eq(4).text().split('S/')[1].trim());
            }
        },
        setValorCol: function(tr, col, valor){
            if (col === "tarifa") {
                tr.find('td').eq(3).text(valor);
            }
            else if (col === "total") {
                tr.find('td').eq(4).text(valor);
            }
        },
        setEntity: function (rpta) {
            var me = this;
            
            $("#codigo").val(rpta.codigo);
            $("#dtFecha").val(BI.convertFecha(rpta.fechaPedido));
            $("#txtNombre").val(rpta.nombre);
            $("#sltTipoDocumento").val(rpta.tipoDocumento);
            $("#txtNumeroDocumento").val(rpta.numeroDocumento);
            $("#txtMovil").val(rpta.movil);
            $("#txtTelefono").val(rpta.telefono);
            $("#txtEmail").val(rpta.correo);
            $("#textsearch").val(rpta.direccion);
            $("#txtDireccion").val(rpta.direccion);
            $("#sltDistrito").val(rpta.idUbigeo);
            //me.initMapa(parseFloat(rpta.latitud), parseFloat(rpta.longitud));
            //me.addmarker({ lat: parseFloat(rpta.latitud), lng: parseFloat(rpta.longitud) });
            //google.maps.event.trigger(autocomplete, 'place_changed');
            distritos.forEach(function (value, index) {
                if (value.IdUbigeo === rpta.idUbigeo) {
                    $("#txtDistrito").val(value.Nombre);
                }
            });
            
            $("#txtReferencia").val(rpta.referencia);
            $("#txtReferenciaDir").val(rpta.referencia);
            $("#latitud").val(rpta.latitud);
            $("#longitud").val(rpta.longitud);
            $("#sltZonalVenta").val(rpta.idZonalVenta);
            $('#sltEstado').val(rpta.estado);
            
            $("#sltMetodoPago").val(rpta.tipoPago);
            $("#sltComprobante").val(rpta.tipoComprobante);
            
            $("#txtMonto").val('S/ ' + parseFloat(rpta.total).toFixed(2));
            $("#total").val('S/ ' + parseFloat(rpta.total).toFixed(2));
            $("#txtComentario").val(rpta.observacion);
            
            $("#sltTipoDocumentoCP").val(rpta.tipoDocumentoCP);
            $("#txtNumeroDocumentoCP").val(rpta.numeroDocumentoCP);
            $("#txtDireccionCP").val(rpta.direccionCP);
            $("#txtClienteCP").val(rpta.nombreCP);
            
            $.each(rpta.items, function (index, value) {
                me.agregarProducto(value, "");
            });
            
            me.guardarDatosComprobante();
            
            if (rpta.estado === 3 || rpta.estado === 4 || rpta.estado === 5)
            {
                me.bloquearPedido();
            }
        },
        getEntity: function () {
            var me = this;
            var oEntity = null;
            var id = $('#id').val().replaceAll("/","");
            
            oEntity = {
                id: id,
                fechaPedido: BI.convertFecha($("#dtFecha").val(), 1),
                nombre: $("#txtNombre").val(),
                idUbigeo: $('#sltDistrito').val(),
                tipoDocumento: $("#sltTipoDocumento").val(),
                numeroDocumento: $('#txtNumeroDocumento').val(),
                movil: $('#txtMovil').val(),
                telefono: $('#txtTelefono').val(),
                correo: $("#txtEmail").val(),
                direccion: $("#txtDireccion").val(),
                idDistrito: $("#sltDistrito").val(),
                referencia: $('#txtReferencia').val(),
                latitud: document.getElementById('latitud').value,
                longitud: document.getElementById('longitud').value,
                idZonalVenta: $('#sltZonalVenta').val(),
                estado: $('#sltEstado').val(),
                items: me.productos,
                tipoPago: $("#sltMetodoPago").val(),
                tipoComprobante: $("#sltComprobante").val(),
                observacion: $("#txtComentario").val(),
                nombreCP: me.oComprobante === null ? '' : me.oComprobante.nombre,
                tipoDocumentoCP: me.oComprobante === null ? '' : me.oComprobante.tipoDocumento,
                numeroDocumentoCP: me.oComprobante === null ? '' : me.oComprobante.numeroDocumento,
                direccionCP: me.oComprobante === null ? '' : me.oComprobante.direccion
            };

            return oEntity;
        },
        guardar: function(){
            var me = this;
            var data = me.getEntity();
            
            if (me.valida()){
                var exito = function(rpta){
                    if(rpta.apiEstado === "ok"){
                        console.log(rpta);
                        BI.defaultSuccess(rpta);
                        
                        let id = "";
                        let codigo = "";
                        id = rpta.id;
                        codigo = String(rpta.codigo);

                        if (id.length !== 0) {
                            $("#titulo").text("Editar Pedido");
                            $('#id').val(id);
                        }

                        if (codigo.length !== 0) {
                            $('#codigo').val(codigo);
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
                $("#titulo").text("Editar Pedido");
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
        listarProducto: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlProductos;
            
            var exito = function(rpta){
                let data = Array();
                data = rpta.data;
                me.productosP = rpta.data;
                me.sltProducto = "<option value=''>-- Seleccione Producto --</option>";

                data.forEach(function (element, index) {
                    me.sltProducto += `<option value="${element.id}">${element.nombre}</option>`;
                });
                
                me.getSingle();
            };
            
            $.ajax({
                url: apiUrl,
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
        agregarProducto: function(data,tipo){
            var me = this;
            if ($('#tablaResultado tbody tr').length === 0) {
                items = 0;
            }

            items += 1;   
            
            if (tipo === "add") {
                $("#tablaResultado tbody")
                    .append($("<tr>")
                        .append($('<td>')
                            .append($('<span class="btn btn-outline-danger mini-btn" onclick="oPedidoGuardar.Core.eliminarProducto(this, ' + items + ')"><span class="material-icons">remove</span></span>')))
                        .append($('<td>')
                            .append('<select data-item="' + items + '" class="seleccionar-producto custom-select">' + me.sltProducto))
                        .append($("<td>")
                            .append('<input data-item="' + items + '" type="text" class="ingresar form-control" maxlength="6" style="text-align:right" value="1">'))
                        .append($('<td style="text-align:right;">').text("S/ 0.00"))
                        .append($('<td style="text-align:right;">').text("S/ 0.00"))
                );

                me.productos.push({ item: items, id: "", cantidad: 1, precioVenta: 0, total: 0, producto: "" });
            }
            else{
                var total = parseFloat(data.cantidad) * parseFloat(data.precioVenta);
                $("#tablaResultado tbody")
                    .append($("<tr>")
                        .append($('<td>')
                            .append($('<span class="btn btn-outline-danger mini-btn" onclick="oPedidoGuardar.Core.eliminarProducto(this, ' + items + ')"><span class="material-icons">remove</span></span>')))
                        .append($('<td>')
                            .append('<select id="cbo' + items + '" data-item="' + items + '" class="seleccionar-producto custom-select">' + me.sltProducto))
                        .append($("<td>")
                            .append('<input data-item="' + items + '" data-idItem="'+ data.idItem +'" type="text" class="ingresar form-control" maxlength="6" style="text-align:right" value="' +data.cantidad + '">'))
                        .append($('<td style="text-align:right;">').text("S/ " + parseFloat(data.precioVenta).toFixed(2)))
                        .append($('<td style="text-align:right;">').text("S/ " + parseFloat(total).toFixed(2)))
                );        

                $('#cbo' + items).val(data.idItem);
                me.productos.push({ item: items, id: data.idItem, cantidad: data.cantidad, precioVenta: parseFloat(data.precioVenta), total: total, producto: data.nombre });
            }
        },
        eliminarProducto: function(elem, i){
            var me = this;
            var item = $(elem).parent().parent();
            item.remove();
            me.productos = me.productos.filter(p => p.item !== i);
            me.sumatotal();
        },
        sumatotal: function(){
            var me = this;
            var total = 0;
            
            for (var i = 0; i < me.productos.length; i++) {
                total += me.productos[i].total;
            }
            
            $('#total').add('text-align', 'right').val('S/ ' + parseFloat(total).toFixed(2));
            $('#txtMonto').add('text-align', 'right').val('S/ ' + parseFloat(total).toFixed(2));
        },
        initMapa: function(lati, long){
            var me = this;
            var uluru = { lat: -12.140544, lng: -76.9851392 };
            if (lati !== "" && long !== "") {
                var lat = { lat: parseFloat(lati), lng: parseFloat(long) }
                uluru = lat;
            }

            mapmodal = new google.maps.Map(document.getElementById('mapmodal'), { /*iniciliza el mapa*/
                center: uluru,
                zoom: 12,
                streetViewControl: false,
                mapTypeId: 'roadmap'
            });
            
            //infoWindow = new google.maps.InfoWindow();
            mapmodal.mapTypes.set('styled_map', me.mapstyle());
            mapmodal.setMapTypeId('styled_map');
            
            mapmodal.controls[google.maps.ControlPosition.TOP_CENTER].push(document.getElementById('textsearch'));
            var autocomplete = new google.maps.places.Autocomplete(document.getElementById('textsearch'), { componentRestrictions: { country: 'Pe' } });
            autocomplete.bindTo('bounds', mapmodal);
            infoWindow = new google.maps.InfoWindow();
            
            autocomplete.addListener('place_changed', function () {
                if (marker !== null) {
                    marker.setMap(null);
                }
                var place = autocomplete.getPlace();

                if (!place.geometry) {

                    window.alert("No details available for input: '" + place.name + "'");
                    return;
                }
                if (place.geometry.viewport) {
                    mapmodal.fitBounds(place.geometry.viewport);
                    document.getElementById('latitud').value = place.geometry.location.lat();
                    document.getElementById('longitud').value = place.geometry.location.lng();
                    me.addmarker(place.geometry.location); 
                } else {
                    mapmodal.setCenter(place.geometry.location);
                    mapmodal.setZoom(17);
                }
            });
            
            me.getZonalesPoligonos();
        },
        mapstyle: function(){
            return new google.maps.StyledMapType(
                [
                    {
                        "featureType": "poi.business",
                        "stylers": [
                            {
                                "visibility": "off"
                            }
                        ]
                    },
                    {
                        "featureType": "poi.park",
                        "elementType": "labels.text",
                        "stylers": [
                            {
                                "visibility": "off"
                            }
                        ]
                    }
                ], { name: 'Styled Map' });
        },
        guardarCambiosDeLista: function(){
            var me = this;
            $("tbody").on("change", ".seleccionar-producto", function (e) {
                var idProducto = $(this).val();
                
                if (idProducto) {
                    var currentRow = $(this).closest("tr");
                    var col = currentRow.find("td").children("span").children("input");
                    col.attr("data-idItem", idProducto);
                    var item = parseInt($(this).attr("data-item"), 10);
                    var nombre = "";

                    me.getTarifa(idProducto, $(this), item);
                    me.productos.forEach(function (p) {
                        if (p.item === item) {
                            p.id = idProducto;
                            p.producto = nombre;
                        }
                    });
                }
                else {
                    var tr = $(this).parent().parent();
                    me.setValorCol(tr, "tarifa", "S/ 0.00");
                    me.setValorCol(tr, "total", "S/ 0.00");
                }

            });
            
            $("tbody").on("keyup", ".ingresar", function (e) {
                var tr = $(e.currentTarget.parentNode.parentNode);
                var valor = $(this).val();
                var id = parseInt($(this).attr("data-item"), 10);
                var tarifa = me.getValorCol(tr, "tarifa");
                var total = parseInt(valor ? valor : 0, 10) * tarifa;
                var totalDec = (Math.round(total * 100) / 100).toFixed(2);
                
                tr.children().last().text("S/ " + totalDec);
                
                me.productos.forEach(function (p) {
                    if (p.item === id) {
                        p.cantidad = parseInt(valor, 10);
                        p.total = total;
                    }
                });

                me.sumatotal();
            });
            
            $("tbody").on("keypress", ".ingresar", function (e) {
                var evento = e || window.event;
                var codigoCaracter = evento.charCode || evento.keyCode;
                var caracter = String.fromCharCode(codigoCaracter);
                if (!["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"].includes(caracter)) {
                    return false;
                }
            });
        },
        getTarifa: function(idProducto,elem,item){
            var me = this;
            if (idProducto) {
                var apiUrl = me.contextUrl + me.apis.urlTarifa;
                var url = `${apiUrl}?id=${idProducto}`;
                
                var exito = function(rpta){
                    if (rpta.apiEstado === "ok") {
                        var cantidad = me.getValorCol(elem.parent().parent(), "cantidad");
                        var tarifa = rpta.tarifa;
                        var tarifaDec = (Math.round(rpta.tarifa * 100) / 100).toFixed(2);
                        var total = cantidad * tarifa;

                        me.productos.forEach(function (p) {
                            if (p.item === item) {
                                p.precioVenta = tarifa;
                                p.total = total;
                            }
                        });
                        
                        var totalDec = (Math.round(total * 100) / 100).toFixed(2)
                        elem.parent().next().next().text(`S/ ${tarifaDec}`);
                        elem.parent().next().next().next().text(`S/ ${totalDec}`);
                        me.sumatotal();
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
            else {
                elem.parent().next().text("");
            }
        },
        loadZonalVenta: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlZonal;
            var url = `${apiUrl}?texto=&ordenamiento=nombre&pagina=0&tamanio=0`;
         
            var exito = function(rpta){
                if(rpta.success){
                   let data = Array();
                   data = rpta.data.data;
                   
                   var mySelect = $('#sltZonalVenta');
                   data.forEach(function (element, index) {
                       mySelect.append(
                           $('<option></option>').val(element.id).html(element.nombre)
                       );
                   });
                   
                   me.loadDistrito();
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
        addmarker: function(pos){
            var me = this;
            marker = new google.maps.Marker({
                position: pos,
                map: mapmodal,
                title: 'mostrar',
                visible: true,
                draggable: true
            });
            
            me.geocodePosition(marker.getPosition());
            google.maps.event.addListener(marker, 'dragend', function () {
                me.geocodePosition(marker.getPosition());
            });
        },
        geocodePosition: function(pos){
            var me = this;

            geocoder = new google.maps.Geocoder();
            geocoder.geocode
                ({
                    latLng: pos
                },
                function (results, status) {
                if (status === google.maps.GeocoderStatus.OK) {
                    marker.setTitle(results[0].formatted_address);
                    console.log(pos.lat());
                    document.getElementById('textsearch').value = results[0].formatted_address;
                    document.getElementById('latitud').value = pos.lat();
                    document.getElementById('longitud').value = pos.lng();
                    var oUbigeo = new Ubigeo({
                        lat: pos.lat(),
                        lng: pos.lng()
                    });
                    
                    oUbigeo.getUbigeo(function (resultUbigeo) {
                        distritos.forEach(function (value, index) {
                            if (value.Nombre === resultUbigeo.distrito.nombre) {
                                $('#sltDistrito').val(value.IdUbigeo);
                            }
                        });
                    });
                }
                else {
                    marker.setTitle('Mueva el marcador para posicionar correctamente');
                }
            });
        },
        validaCobertura: function(latitud,longitud){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlValidaCobertura;
              
            var exito = function(rpta){
                if(rpta.apiEstado === "ok"){
                    $('#sltZonalVenta').val(rpta.id);
                    $('#txtReferencia').val($('#txtReferenciaDir').val());
                    $("#txtDistrito").val($("#sltDistrito").find('option:selected').text());
                    $('#txtDireccion').val(document.getElementById('textsearch').value);
                    
                    var oUbigeo = new Ubigeo({
                        lat: parseFloat(latitud),
                        lng: parseFloat(longitud)
                    });
                    
                    /*oUbigeo.getUbigeo(function (resultUbigeo) {
                        console.log(resultUbigeo);
                        //$('#sltDistrito').val(resultUbigeo.ubigeo);
                        $('#txtDireccion').val(document.getElementById('textsearch'));
                        $('#txtDistrito').val(resultUbigeo.distrito.nombre);
                    });*/
                    
                    $('#modalmapa').modal('hide');
                    
                }else{
                    var htmlMessage = "<p style='text-align: center;font-size: medium;'>"+ rpta.apiMensaje+"</p>";
                      Swal.fire({
                          title: "Error",
                          icon: "error",
                          html: htmlMessage
                      });
                }
            };

            $.ajax({
              url: apiUrl,
              type: 'post',
              data: {latitud:latitud,longitud:longitud},
              dataType: 'json',
              beforeSend: function(){

              },
              success: exito,
              error: function(error){
                  console.log(error);
              }
            });
        },
        dibujarPoligonos: function (value) {
            var poligono = new google.maps.Polygon({
                paths: value.coordenadas,
                strokeColor: value.color,
                strokeOpacity: 0.8,
                strokeWeight: 1,
                fillColor: value.color,
                fillOpacity: 0.1
            });
            poligono.setMap(mapmodal);   
        },
        getZonalesPoligonos: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlCoordenadas;

            var exito = function(rpta){
               
               if(rpta.apiEstado === "ok"){
                  $.each(rpta.data, function (index, value) {
                      var oDatos = {}; 
                      var pts = value.pts.split('|').slice(0, -1);                
                      oDatos.codigo = value.codigo;
                      oDatos.color = value.color;
                      oDatos.id = value.id;
                      oDatos.nombre = value.nombre;    
                      oDatos.telefono = value.telefono;
                      var lista = [];
                      $.each(pts, function (index, value) {
                          var latlg = value.split(',');
                          var coordenada = { lat: parseFloat(latlg[0]), lng: parseFloat(latlg[1]) };
                          lista.push(coordenada);
                      });
                      oDatos.coordenadas = lista;                
                      oZonales.push(oDatos);
                  });
                  
                  $.each(oZonales, function (index, value) {
                      me.dibujarPoligonos(value);     
                  });
               } 
            };

            $.ajax({
              url: apiUrl,
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
        loadDistrito: function(){
          var me = this;
         
          var exito = function(rpta){
             if(rpta.success){
                let data = Array();
                data = rpta.data;
                distritos = rpta.data;
                var mySelect = $('#sltDistrito');
                data.forEach(function (element, index) {
                    mySelect.append(
                        $('<option></option>').val(element.IdUbigeo).html(element.Nombre)
                    );
                });
                
                me.listarProducto();
             } 
          };
          
          $.ajax({
            url: me.contextUrl + me.apis.urlDistrito,
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
        guardarDatosComprobante: function(){
            var me = this;
            if (me.validarComprobante()) {
                me.oComprobante = {};
                me.oComprobante.nombre = $("#txtClienteCP").val();
                me.oComprobante.tipoDocumento = $("#sltTipoDocumentoCP").val();
                me.oComprobante.numeroDocumento = $("#txtNumeroDocumentoCP").val();
                me.oComprobante.direccion = $('#txtDireccionCP').val();

                $('#modalfactura').modal('hide');
            }
        },
        validarComprobante: function(){
            var result = true, mensaje = "";

            if ($("#sltTipoDocumentoCP").val().trim().length === 0) {
                mensaje += "Has omitido seleccionar un Tipo de Documento.";
                result = false;
            }

            if ($("#txtNumeroDocumentoCP").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un n\u00famero de documento.";
                result = false;
            }
            else if ($("#sltTipoDocumentoCP").val() === "1" && $("#txtNumeroDocumentoCP").val().trim().length !== 8) {

                mensaje += "Has omitido ingresar un n\u00famero de documento DNI de 8 dig\u00edtos.";
                result = false;
            }
            else if ($("#sltTipoDocumentoCP").val() === "2" && $("#txtNumeroDocumentoCP").val().trim().length !== 11) {
                mensaje += "Has omitido ingresar un n\u00famero de documento RUC de 11 dig\u00edtos.";
                result = false;
            }

            if ($("#txtDireccionCP").val() === "") {
                mensaje += "Has omitido ingresar una direcci\u00f3n.";
                result = false;
            }

            if ($("#txtClienteCP").val() === "") {
                mensaje += "Has omitido ingresar un Nombre.";
                result = false;
            }

            if (!result) {
                BI.MostrarPopupError(mensaje);
            }

            return result;
        },
        bloquearPedido: function(){
            $("#dtFecha").prop("disabled", true);
            $("#sltMetodoPago").prop("disabled", true);
            $("#sltComprobante").prop("disabled", true);
            $("#txtReferencia").prop("disabled", true);
            $("#sltEstado").prop("disabled", true);
            $("#txtDescuento").prop("disabled", true);
            $("#txtMonto").prop("disabled", true);
            $("#txtSubTotal").prop("disabled", true);
            $("#total").prop("disabled", true);
            $("#sltZonalVenta").prop("disabled", true);
            $("#txtNombre").prop("disabled", true);
            $("#sltTipoDocumento").prop("disabled", true);
            $("#txtNumeroDocumento").prop("disabled", true);
            $("#txtMovil").prop("disabled", true);
            $("#txtTelefono").prop("disabled", true);
            $("#txtEmail").prop("disabled", true);
            $("#sltDireccion").prop("disabled", true);
            $("#txtComentario").prop("disabled", true);
            $("#txtDistrito").prop("disabled", true);
            $("#txtReferenciaDir").prop("disabled", true);
            $(".ingresar").prop("disabled", true);
            $("#mapaDireccion").prop("disabled", true);


            //Documento
            $("#txtSerieCP").prop("disabled", true);
            $("#txtNumeroCP").prop("disabled", true);
            $("#txtClienteCP").prop("disabled", true);
            $("#sltTipoDocumentoCP").prop("disabled", true);
            $("#txtNumeroDocumentoCP").prop("disabled", true);
            $("#txtDireccionCP").prop("disabled", true);
            $("#btnGuardarComprobante").prop("disabled", true);

            $("#addItem").hide();
            $("#btnGuardar").prop("disabled", true);
        }
    };
};

$(function (e) {
    oPedidoGuardar = new PedidoGuardar();
    oPedidoGuardar.Core.init();
});