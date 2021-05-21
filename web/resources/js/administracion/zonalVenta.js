/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oRegistro = null;
var mapRegistro;
var urlContext = window.location.origin + "/WebGestionDelivery/";
var oZonalesList = [];
var ubicaciones = [];
var path = new Array();
var flageliminado;
var polyline;
var markerSize = { x: 2, y: 2 };
var miquitarSize = { x: -12, y: 10 };
var minameSize = { x: 2, y: 30 };
var mimoverSize = { x: 18, y: 10 };
var miaddSize = { x: 2, y: 10 };
var _infoWindow;
var labelMarker;


var Registro = function(){
    this.Core = {
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
            ultimo : "btnUltimo",
        },
        init : function(){
            var me = this;
             
            $("#txtTelefono").on("keypress", function (e) {
                var evento = e || window.event;
                var codigoCaracter = evento.charCode || evento.keyCode;
                var caracter = String.fromCharCode(codigoCaracter);
                if (!["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"].includes(caracter)) {
                    return false;
                }
            });  
        
            $("#btnCrear").on("click", function () {
                $("#formulario").show();
            });
            
            $("#btnCancelar").on("click", function () {
                me.limpiar();
                $("#formulario").hide();
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
            
            $("#tablaResultado").on('click', '.editar', function (ev) {
                var currentRow = $(this).closest("tr");
                var col2 = currentRow.find("td:eq(1)").children("span");
                var data = col2.attr('data-id');
                me.setEntity(data);
                $("#formulario").show();
            });
            
            $("#btnAplicar").click(function(){
                me.movercursor();
            });
            
            $("#btnGuardar").click(function(){
               me.guardar(); 
            });
            
            me.loadDistrito();
            me.loadPrototype();
            me.buscar();
            me.loadMapa();
        },
        loadPrototype: function(){
            var me = this;
            
            google.maps.Marker.prototype.labels = new Array();
            
            //Agregar metodo al objeto Marker de google para que muestre una etiqueta
            google.maps.Marker.prototype.setLabel = function (label) {
                for (var i = 0; i < this.labels.length; i++) {
                    this.labels[i].setMap(null);
                }

                 if (this.title !== '0') { //en este ambito se estable el tipo de etiqueta
                        this.label = new MarkerLabel({
                            map: mapRegistro,
                            marker: google.maps.Marker,
                            text: mapRegistro.recalcularmarcadores(),
                            name: mapRegistro.recalcularmarcadores()
                        });
                        this.label.bindTo('position', this, 'position');
                }
                else {
                    this.label = new MarkerLabel({
                        map: mapRegistro,
                        marker: google.maps.Marker,
                        text: mapRegistro.markers.length,
                        name: '+'
                    });
                    this.label.bindTo('position', this, 'position');
                }
            }; 
            
             //se crea la etiqueta 
            var MarkerLabel = function (options) {
                this.setValues(options);
                this.quitar = document.createElement('span');
                this.nombre = document.createElement('span');
                this.addmarcador = document.createElement('span');
                this.labelgroup = document.createElement('label');
                this.quitar.className = 'map-marker-button mapispan';
                this.nombre.className = 'map-marker-text mapispan';
                this.addmarcador.className = 'map-marker-button mapispan';
                this.labelgroup.className = 'map-marker-button mapispan';
            };
            
            MarkerLabel.prototype = $.extend(new google.maps.OverlayView(), {
                onAdd: function () {
                    if (String(this.get('name')) !== "+") {                      //validamos el tipo de etiqueta
                        //  this.getPanes().overlayImage.appendChild(this.mover);
                            this.getPanes().overlayImage.appendChild(this.quitar);
                            this.getPanes().overlayImage.appendChild(this.nombre);
                    }
                    else {
                        this.getPanes().overlayImage.appendChild(this.addmarcador);
                    }
                    var self = this;
                    this.listeners = [google.maps.event.addListener(this, 'position_changed', function () { self.draw(); })];
                    
                },
                draw: function () {
                    var text = String(this.get('text'));
                    var name = String(this.get('name'));
                    var position = this.getProjection().fromLatLngToDivPixel(this.get('position'));
                    //this.quitar.innerHTML = '<span class="map-marker-button bg-danger eliminarMarker" data-text="'+text+'">-</span>';
                    this.quitar.innerHTML = '<span class="map-marker-button bg-danger" onclick="oRegistro.Core.eliminarmarkerspan(' + text + ')">-</span>';
                    this.quitar.style.left = (position.x - miquitarSize.x) + 'px';
                    this.quitar.style.top = (position.y - miquitarSize.y) + 'px';
                    this.nombre.innerHTML = text;
                    this.nombre.style.left = (position.x - minameSize.x) + 'px';
                    this.nombre.style.top = (position.y - minameSize.y) + 'px';
                    //this.addmarcador.innerHTML = '<span  class="map-marker-button bg-primary insertmarkerspan"  data-text="'+text+'">' + name + '</span>';
                    this.addmarcador.innerHTML = '<span  class="map-marker-button bg-primary"  onclick="oRegistro.Core.insertmarkerspan(' + text + ')">' + name + '</span>';
                    this.addmarcador.style.left = (position.x - miaddSize.x) + 'px';
                    this.addmarcador.style.top = (position.y - miaddSize.y) + 'px';
                },
                onRemove: function () {

                }
            });
            
            /*marker label*/
            var MarkerLabeletiq = function (options) {  //var MarkerLabel
                this.setValues(options);
                this.span = document.createElement('span');
                this.span.className = 'map-marker-label label label-primary mapispan';
            };
            MarkerLabeletiq.prototype = $.extend(new google.maps.OverlayView(), {
                onAdd: function () {
                    this.getPanes().overlayImage.appendChild(this.span);
                    var self = this;
                    this.listeners = [google.maps.event.addListener(this, 'position_changed', function () { self.draw(); })];
                },
                draw: function () {
                    var text = String(this.get('text'));
                    var position = this.getProjection().fromLatLngToDivPixel(this.get('position'));
                    this.span.innerHTML = text;
                    this.span.style.left = (position.x - markerSize.x) + 'px';
                    this.span.style.top = (position.y - markerSize.y) + 'px';
                },
                onRemove: function () {

                }
            });

            //conteo de marcadores predeterminado
            google.maps.Map.prototype.recalcularmarcadores = function() {
                var returnfun = 0;
                gotitas = [];
                for (var i = 0; i < this.markers.length; i++) {
                    if (this.markers[i].title !== "0") {
                        returnfun = returnfun + 1;
                    }
                }
                return returnfun;
            };
            //function que limpia todo s los marcadores
            google.maps.Map.prototype.markers = new Array();
            google.maps.Map.prototype.clearMarkers = function() {
                for (var i = 0; i < this.markers.length; i++){
                    this.markers[i].setMap(null);
                }
                this.markers = new Array();
                me.RemoveLabel();
            };
            var oldSetMap = google.maps.Marker.prototype.setMap;
            google.maps.Marker.prototype.setMap = function (mapRegistro) {
                if (mapRegistro) {
                    mapRegistro.markers.push(this);
                }
                oldSetMap.call(this, mapRegistro);
            }; 
        },
        loadMapa: function(){
            var me= this;
            var lati = -12.140544;
            var long = -76.9851392;
            var uluru;
            if (lati !== 0 && long !== 0) {
                var lat = { lat: lati, lng: long };
                uluru = lat;
            }

            mapRegistro = new google.maps.Map(document.getElementById('mapRegistro'), { /*iniciliza el mapa*/
                center: uluru,
                zoom: 12,
                streetViewControl: false,
                mapTypeId: 'roadmap'
            });

            //estilos
            mapRegistro.mapTypes.set('styled_map', me.mapstyle());
            mapRegistro.setMapTypeId('styled_map');
            
            var autocomplete = new google.maps.places.Autocomplete(document.getElementById('autocompletesearch2'), { componentRestrictions: { country: 'Pe' } });
            autocomplete.bindTo('bounds', mapRegistro);
            infoWindow = new google.maps.InfoWindow();
            
            autocomplete.addListener('place_changed', function () {
                var place = autocomplete.getPlace();
                if (!place.geometry) {

                    window.alert("No details available for input: '" + place.name + "'");
                    return;
                }
                if (place.geometry.viewport) {
                    mapRegistro.fitBounds(place.geometry.viewport);
                } else {
                    mapRegistro.setCenter(place.geometry.location);
                    mapRegistro.setZoom(17);
                }
            });
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
        loadDistrito: function(){
          var me = this;
         
          var exito = function(rpta){
             if(rpta.success){
                let data = Array();
                data = rpta.data;
                var mySelect = $('#sltDistrito');
                data.forEach(function (element, index) {
                    mySelect.append(
                        $('<option></option>').val(element.IdUbigeo).html(element.Nombre)
                    );
                });
             } 
          };
          
          $.ajax({
            url: urlContext + 'ubigeo/search',
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
        getEntity: function(){
            var i=1;
            ubicaciones = [];
            $('#div_t tr').each(function () {
                var lat = $(this).find("td").eq(1).text();
                var lon = $(this).find("td").eq(2).text();
                if(lat!=="" && lon!==""){
                    ubicaciones.push({ latitud: lat, longitud: lon, orden: i });
                    i++;
                }
            });
           var oEntity ={
               id: $('#id').val(),
               idDistrito : $("#sltDistrito").val(),
               nombre: $("#txtNombre").val(),
               telefono: $("#txtTelefono").val(),
               color:  $("#tc_color").val(),
               esPropio: ($("#sltEsPropio").val() === "1"? true : false),
               estado: $("#sltEstado").val(),
               ubicaciones: ubicaciones
           }; 
           console.log(oEntity);
           return oEntity;
        },
        setEntity: function (id) {
            var me = this;
            var rpta = oZonalesList.filter(p => p.id === id)[0];
            
            me.limpiar();
            
            $('#id').val(rpta.id);
            $('#txtCodigo').val(rpta.codigo);
            $('#sltDistrito').val(rpta.idDistrito);
            $('#txtNombre').val(rpta.nombre);
            $('#txtTelefono').val(rpta.telefono);
            $('#tc_color').val(rpta.color);
            $('#sltEsPropio').val(rpta.esPropio ? "1" : "0");
            $('#sltEstado').val(rpta.idEstado);
            
            var ubicacionesList = rpta.pts.slice(0, -1).split("|");
            var orden=1;
            $("#div_t").empty();
            for (var i = 0; i < ubicacionesList.length; i++) {
                
                var ubicacion = ubicacionesList[i].split(",");
                $("#div_t")
                    .append($("<tr>")
                        .append($("<td>").text(`Punto ${orden}:`))
                        .append($("<td>").text(`${ubicacion[0]}`))
                        .append($("<td>").text(`${ubicacion[1]}`)));
                orden++;
            }

            rpta.pts = rpta.pts.slice(0, rpta.pts.length- 1);        
            me.editarPoligono(rpta.pts);
        },
        buscar: function(){
            var me = this;
            var texto="";
            
            var exito = function(rpta){
                
            me.limpiar();
            $("#formulario").hide();
            $('#totalRegistros').text(rpta.data.total);
                me.listParams.paginacion.totalPaginas = Math.ceil((rpta.data.total / me.listParams.paginacion.limite));
                $('#totalPagina').text(me.listParams.paginacion.totalPaginas);

                var mySelect = $('#numeropagina');
                mySelect.empty();

                for (let index = 1; index <= me.listParams.paginacion.totalPaginas; index++) {
                    mySelect.append(
                        $('<option></option>').val(index).html(index)
                    );
                }

                mySelect.val(me.listParams.paginacion.pagina);

                var data = rpta.data.data;
                $("#totalRegistros").text(rpta.data.total);
                $("#tablaResultado tbody").empty();
                oZonalesList = [];
                if (data.length !== 0) {
                    data.forEach(function (element) {
                        oZonalesList.push(element);
                        $("#tablaResultado tbody")
                            .append($('<tr>')
                                .append($('<td>')
                                    .append($('<span class="btn btn-outline-primary editar" style="margin: 2px 2px 2px 2px; padding: 2px 2px 2px 2px" data-id="' + element.id + '" ><span class="material-icons">edit</span></span>')))
                                .append($('<td>')
                                    .append($('<span class="btn btn-outline-danger eliminar" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px" data-id="' + element.id + '"><span class="material-icons">remove</span></span>')))
                                .append($('<td>').text(element.codigo))
                                .append($('<td>').text(element.nombre))
                                .append($('<td>').text(element.estado))
                                .append($('<td>').text(element.esPropio ? "SI" : "NO"))
                                .append($('<td>')
                                    .append($('<div style="width:10px;height:10px;background-color:'+element.color+'" data-color="'+element.color+'"></div>'))))
                    });
                }
                else {
                    $("#tablaResultado tbody")
                        .append($("<tr>")
                            .append($("<td>").attr("colspan", "6").append($("<div>").addClass("text-center").text("No existen registros."))));
                }
            };
            
            $.ajax({
            url: urlContext + 'zonalVenta/search?texto=' + texto + '&ordenamiento=' + me.listParams.ordenamiento + '&pagina=' + me.listParams.paginacion.pagina + '&tamanio=' + me.listParams.paginacion.limite,
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
        mostrarPagina: function (ev) {
            var me = this;
            var tipoPagina = ev.currentTarget.id;

            if (tipoPagina === me.listParams.primero) {
                me.listParams.paginacion.pagina = 1;
            } else if (tipoPagina === me.listParams.anterior) {
                if (me.listParams.paginacion.pagina !== 1) {
                    me.listParams.paginacion.pagina = me.listParams.paginacion.pagina - 1;
                }
            } else if (tipoPagina === me.listParams.siguiente) {
                if (me.listParams.paginacion.pagina !== me.listParams.paginacion.totalPaginas) {
                    me.listParams.paginacion.pagina = me.listParams.paginacion.pagina + 1;
                }
            } else if (tipoPagina === me.listParams.ultimo) {
                me.listParams.paginacion.pagina = me.listParams.paginacion.totalPaginas;
            }
        },
        eliminar: function (id) {
            Swal.fire({
                title: '',
                text: "\u00bfEst\u00e1 seguro de eliminar este Zonal de Venta.?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Eliminar',
                cancelmButtonText: 'Cancelar'
              }).then((result) => {
                if (result.isConfirmed) {
                    var me = this;
                    
                    var exito = function(rpta){
                        
                        if(rpta.data === "ok"){
                            Swal.fire(
                                'Eliminado!',
                                'Se elimino el Zonal de Venta correctamente.',
                                'success'
                            );
                            me.buscar();
                            oCobertura.Core.init();
                        }else{
                            Swal.fire(
                                'Error!',
                                'No se puedo eliminar el Zonal de Venta, intente mas tarde.',
                                'error'
                            );
                        }
                    };

                    $.ajax({
                        url: urlContext + 'zonalVenta/mantenance',
                        type: 'post',
                        data: { option:"D",id: id },
                        dataType: 'json',
                        beforeSend: function(){

                        },
                        success: exito,
                        error: function(error){
                            console.log(error);
                        }
                    });
                }
              });
        },
        guardar: function(){
            var me = this;
            var entity = me.getEntity();
            var option ="";
            if (me.valida()){
                
                var exito = function(rpta){
                    
                    if(rpta.data === "ok"){
                        Swal.fire(
                            'Ok',
                            'Se guardo el Zonal de Venta correctamente.',
                            'success'
                        );
                        me.buscar();
                    }else{
                        me.MostrarPopupError("No se puedo guardar el Zonal de Venta, intente mas tarde.");
                    }
                };
                
                if($("#id").val()===""){
                    option ="I";
                }else{
                    option ="U";
                }
                
                $.ajax({
                    url: urlContext + 'zonalVenta/mantenance',
                    type: 'post',
                    data: {option:option,data: JSON.stringify(entity)},
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
        valida: function(){
            var me = this;
            var result = true, mensaje = "";
            
            if ($("#txtNombre").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un nombre.";
                result = false;
            }
            if ($("#txtTelefono").val().trim().length < 7) {
                mensaje += "Has omitido ingresar un n\u00famero de tel\u00e9fono v\u00e1lido.";
                result = false;
            }
            if ($("#tc_color").val().trim().length === 0) {
                mensaje += "Has omitido ingresar un color.";
                result = false;
            }
            if ($("#sltDistrito").val() === "") {
                mensaje += "Has omitido seleccionar un distrito.";
                result = false;
            }
            if (ubicaciones.length === 0) {
                mensaje += "Has omitido seleccionar al menos tres ubicaciones.";
                result = false;
            }
            if ($("#sltEsPropio").val() === "") {
                mensaje += "Has omitido seleccionar si el zonal es propio o no.";
                result = false;
            }
            if ($("#sltEstado").val() === "") {
                mensaje += "Has omitido seleccionar un estado.";
                result = false;
            }

            if (!result) {
                me.MostrarPopupError(mensaje);
            }
            return result;
        },
        limpiar: function(){
            var me = this;
            $('#id').val("");
            $('#txtCodigo').val("");
            $('#sltDistrito').val("");
            $('#txtNombre').val("");
            $('#txtTelefono').val("");
            $('#tc_color').val("");
            $('#sltEsPropio').val("");
            $('#sltEstado').val("");
            $("#div_t").empty();
            
            for (var i = 0; i < 3; i++) {
                $("#div_t")
                    .append($("<tr>")
                        .append($("<td>").text(`Punto ${i}:`))
                        .append($("<td>").text(""))
                        .append($("<td>").text("")));
            }
            mapRegistro.clearMarkers();
            me.RemoveLabel();
            path = [];
            ubicaciones = [];
            
            try {
                polyline.setMap(null);
            } catch (e) {
                console.log('no exite poligono');
            }

            google.maps.event.clearListeners(mapRegistro, 'click');
            google.maps.event.clearListeners(mapRegistro, 'dragend');
        },
        MostrarPopupError: function(mensaje = ''){
            var arreglo = Array();
            arreglo = mensaje.split(".");
            let texto = "";
            arreglo.forEach(function (elemento) {
                if (elemento.length !== 0) {
                    texto += '<li class="text-left">' + elemento.replace('_', '.') + '.</li>';
                }
            });
            const wrapper = document.createElement('ul');
            wrapper.innerHTML = texto;
            Swal.fire({
                title: 'Error',
                icon: "error",
                html: wrapper
            });
        },
        movercursor: function(){
            var me = this;
            var ne = new google.maps.LatLng(-34.00, 150.00);
            var nw = new google.maps.LatLng(-34.00, 150.50);
            var sw = new google.maps.LatLng(-35.00, 150.50);
            var se = new google.maps.LatLng(-35.00, 150.00);
            google.maps.event.addListener(mapRegistro, 'mousemove', function (event) {
                if ((event.latLng.lat() > se.lat()) && (event.latLng.lat() < ne.lat()) &&
                    (event.latLng.lng() > ne.lng()) && (event.latLng.lng() < sw.lng())) {
                    mapRegistro.setOptions({ draggableCursor: "url('resources/img/cursor.cur'), auto" });
                }
                else {
                    mapRegistro.setOptions({
                        draggableCursor: "url('resources/img/cursor.cur'), pointer"
                    });
                }
            });

            me.addMarkersEdision();
            me.reactivarEventoclick();
        },
        addMarkersEdision: function(){
            var me = this;
            mapRegistro.addListener('click', function (event) {
                me.addMarker(event.latLng);
            });
        },
        addMarker: function(location){
            var me = this; 

            var icono = {
                url: 'img/cursor32.fw.png'
            };
            var marker = new google.maps.Marker({
                position: location,
                draggable: true,
                map: mapRegistro,
                icon: icono,
                title: 'c',
                zIndex: mapRegistro.recalcularmarcadores()
            });
            /*evento arrastre del marcador permite usar el metodo redibujo de todos los marcadores*/
            google.maps.event.addListener(marker, 'dragend', function () { document.getElementById('div_t').innerHTML = ""; me.redibujo(); });
            path.push(marker); /*path es un array de marcadores usado en varios metodos*/
            document.getElementById('div_t').innerHTML = "";
            me.redibujo(); /* function que redibuja los marcadores recorre el array path para volver a establecer los markers*/
        },
        reactivarEventoclick: function(){
            var me= this;
            mapRegistro.addListener('click', function (event) {
                if (flageliminado === 1) {
                    me.addMarkersEdision();
                    flageliminado = 0;
                }
            });
        },
        redibujo: function(){
            var me = this;
            mapRegistro.clearMarkers();/*limpiar mapa*/
            document.getElementById('div_t').innerHTML = '';/*limpiar div tabla*/
            try {
                if (path.length > 0) {
                    polyline.setMap(null); /*quita el poligono si lo hubiera*/
                }
            } catch (e) {
                console.log('no hay poligono');
            }

            /*se crea la tabla de puntos*/
            var tablahtml = '<table class="table " style="font-size:10px"><tbody class="panel-body">';
            var arreglo = new Array();
            var geography = new Array();
            for (var i = 0; i < path.length; i++) {
                tablahtml = tablahtml + '<tr class=><td><p style="margin:0"> Punto:' + i + '</p></td><td>' + path[i].position.lat() + '</td><td>' + path[i].position.lng() + '</td></tr>';
                arreglo.push(path[i].position.lat() + ',' + path[i].position.lng()); /* arreglos que guardar el order de los puntos latitud y longitud para ser guardados*/
                geography.push(path[i].position.lng() + ' ' + path[i].position.lat()); /*arreglo que guarda en cierto orden los puntos para crear un objeto polygon en sql server*/
                if (i > 0) {
                    if (i > 0) {
                        /* se crea una interpolacion para agregar una etiqueta que permitira insertar entre puntos*/
                        inBetween = google.maps.geometry.spherical.interpolate(new google.maps.LatLng(path[i].position.lat(), path[i].position.lng()), new google.maps.LatLng(path[i - 1].position.lat(), path[i - 1].position.lng()), 0.5);

                        labelMarker = new google.maps.Marker({
                            position: inBetween,
                            map: mapRegistro,
                            title: '0',
                            label: '+',
                            visible: false,
                            zIndex: mapRegistro.recalcularmarcadores()
                        });
                    }
                    if (i === (path.length - 1)) {
                        /* se crea una interpolacion para agregar una etiqueta que permitira insertar entre puntos*/
                        inBetween = google.maps.geometry.spherical.interpolate(new google.maps.LatLng(path[i].position.lat(), path[i].position.lng()), new google.maps.LatLng(path[0].position.lat(), path[0].position.lng()), 0.5);

                        labelMarker = new google.maps.Marker({
                            position: inBetween,
                            map: mapRegistro,
                            title: '0',
                            label: '+',
                            visible: false,
                            zIndex: mapRegistro.recalcularmarcadores()
                        });
                    }
                }
            }

            document.getElementById('div_t').innerHTML = tablahtml + '</table></tbody>';/*cierre de tabla */

            if (path.length > 0) {
                geography.push(path[parseInt(0)].position.lng() + ' ' + path[parseInt(0)].position.lat());
            }
            if (path.length > 0) {
                $('#hdntextvalidar').val(geography.join(','));
                $('#hdntext').val(arreglo.join('|'));
                me.polilineasZona(arreglo.join('|')); /*se llama a la  funcion que dibujara poligono o polilinea*/
            }
        },
        polilineasZona: function(arreglopoint){
            var me = this;
            var d = arreglopoint;
            path = new Array();
            var tit;
            var arr = d.toString().split('|');
            var territorio = new Array(arr.length);
            for (var i = 0; i < arr.length; i++) {
                var coordenas = arr[i].split(',');
                var la = parseFloat(coordenas[0]);
                var lo = parseFloat(coordenas[1]);
                var pol = new google.maps.LatLng(la, lo);
                var a = i;
                tit = la + ',' + lo + '-' + a;
                me.polimas(pol, tit, i); //crear marcadores uno a uno
                territorio[i] = { lat: la, lng: lo };
            }
            
            polyline = new google.maps.Polygon({
                path: territorio,
                map: mapRegistro,
                strokeColor: document.getElementById('tc_color').value,
                strokeWeight: 5,
                strokeOpacity: 0.8,
                fillColor: document.getElementById('tc_color').value,
                fillOpacity: 0.15,
                clickable: false
            });
        },
        polimas: function(b, ti, i){
            var me = this;
            /*crear marcadores si check is true*/
            var icono;
            icono = {
                url: 'resources/img/alfiler.png'
            };

            var marker = new google.maps.Marker({
                position: b,
                label: {
                    text: i.toString(),
                    fontSize: '16px',
                    label: i.toString(),
                    fontWeight: 'bold'
                },
                map: mapRegistro,
                draggable: true,
                icon: icono
            });
            /*se agrega el marcador a Path con los respectivos eventos para el marcador*/
            path.push(marker);
            google.maps.event.addListener(marker, 'click', function () { document.getElementById('div_t').innerHTML = ""; me.redibujo(); });
            google.maps.event.addListener(marker, 'dragend', function () { document.getElementById('div_t').innerHTML = ""; me.redibujo(); });
        },
        RemoveLabel: function(){
            $("span").remove('.mapispan');
            $("button").remove('.mapispan');
        },
        eliminarmarkerspan: function(indice){
            var me = this;
            google.maps.event.clearListeners(mapRegistro, 'click');
            //  google.maps.event.clearListeners(map, 'drag');
            google.maps.event.clearListeners(mapRegistro, 'dragend');

            flageliminado = 1;/*flag para indicar que se elimino un marcador*/
            path.splice(indice, 1);
            me.refrescar(0);
            me.reactivarEventoclick();/*activa la funcion de creacion de marcador*/
        },
        insertmarkerspan: function(indice) {
            var me = this;
            me.quitarEventoClick_temporal();
            me.InsertmarkerEdision(indice);
        },
        refrescar : async function(condicion){
            var me = this;
            const quitaevento = await me.quitarEventoClick_temporal();
            const redibuja = await me.redibujo();
            if (condicion === 1) {
                const volverevento = await me.addMarkersEdision(); //addMarkersEdision();
            }
            else {
                if (path.length === 0) {
                    flageliminado = 1;
                    me.reactivarEventoclick();
                }
            } 
        },
        quitarEventoClick_temporal: function(){
            google.maps.event.clearListeners(mapRegistro, 'click');
            google.maps.event.clearListeners(mapRegistro, 'click');
        },
        editarPoligono: async function(points){
            var me = this;

            let territorio = await me.polilineas(points); /*crear marcadores*/
            let trazos = await me.tipotrazo(territorio); /* crea traza en el mapa*/
            let redibujar = await me.redibujo();
            me.movercursor(); /*habilita la edicion de marcadores*/
        },
        polilineas : async function(arreglopoint){
            var me = this;
            var d = arreglopoint;
            path = new Array();
            var tit;
            var arr = d.toString().split('|');/*obtiene el array*/
            var territorio = new Array(arr.length);/*nuevo arreglo para latitud y longitud*/
            for (var i = 0; i < arr.length; i++) {
                var coordenas = arr[i].split(',');
                var la = parseFloat(coordenas[0]);
                var lo = parseFloat(coordenas[1]);
                var pol = new google.maps.LatLng(la, lo);
                var a = i;
                tit = la + ',' + lo + '-' + a;
                me.polimas(pol, tit, i); //metodo que crear marcadores uno a uno
                territorio[i] = { lat: la, lng: lo };
            }
            return territorio;
        },
        tipotrazo: async function(territorio){
            var me = this;
            polyline = new google.maps.Polygon({
                path: territorio,
                map: mapRegistro,
                strokeColor: document.getElementById('tc_color').value,
                strokeWeight: 5,
                strokeOpacity: 0.8,
                fillColor: document.getElementById('tc_color').value,
                fillOpacity: 0.15,
                clickable: false
            });
            
            var p1 = territorio[0];
            var p2 = territorio[parseInt((territorio.length - 1) / 2)];

            inBetween = google.maps.geometry.spherical.interpolate(new google.maps.LatLng(p1.lat, p1.lng), new google.maps.LatLng(p2.lat, p2.lng), 0.5);
            imag = new google.maps.MarkerImage('img/148764.png');
            labelMarker = new google.maps.Marker({
                position: inBetween,
                map: mapRegistro,
                title: '0',
                visible: false,
                icon: imag
            });
            me.attachPolygonInfoWindow(labelMarker);
        },
        attachPolygonInfoWindow: function(marker){
            mapRegistro.setCenter(marker.getPosition());
            marker._infowindow = new google.maps.InfoWindow({
                content: $('#tx_nombre').val(),
                position: marker.getPosition(),
                zoom: 17
            });
            google.maps.event.addListener(marker, 'click', function () {
                this._infowindow.open(mapRegistro, this);
            });
            // open marker on load
            google.maps.event.trigger(marker, 'click');
        },
        InsertmarkerEdision: function(indi){
            var me = this;
            mapRegistro.addListener('click', function (event) {
                me.insertmarker(event, indi);
            });
        },
        insertmarker: function(localtion, indice){
            var me = this;
            var insertmarker = new google.maps.Marker({
                position: localtion.latLng,
                draggable: true,
                map: mapRegistro
            });
            path.splice(parseInt(indice), 0, insertmarker);
            /*redibuja los marcadores */
            me.refrescar(1);
        },
        exportarExcel: function () {
            var me = this;
            var nameFile = "ZonalVenta";
            var texto="";
            var url = urlContext + 'zonalVenta/search?texto=' + texto + '&ordenamiento=' + me.listParams.ordenamiento + '&pagina=0&tamanio=0';

            var exito = function(rpta){
                var data = rpta.data.data;
                var table='';
                if (data.length !== 0) {
                    table += "<tr>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Codigo</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Zonal de Venta</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Estado</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Es Propio</td>";
                    table += "<td style='font-weight: bold;background-color: #D8F6CE;'>Color</td>";
                    table += "</tr>";    
                    data.forEach(function (element) { 
                        table += "<tr>";
                        table += "<td>"+element.codigo+"</td>";
                        table += "<td>"+element.nombre+"</td>";
                        table += "<td>"+element.estado+"</td>";
                        table += "<td>"+(element.esPropio ? "SI" : "NO")+"</td>";
                        table += "<td style='font-weight: bold;background-color: "+element.color+";' ></td>";
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
    oRegistro = new Registro();
    oRegistro.Core.init();
});      


