/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var oMonitor = null;
var mapMonitor;
var formas = [];
var oPedidos = [];
var oZonales = [];
var markerSize = { x: 25, y: 70 };
var markerSizeS = { x: 12, y: 10 };

function Label() {
    $("span").remove('.mapispan');
}
google.maps.Marker.prototype.labels = new Array();
google.maps.Marker.prototype.setLabel = function (label) {
    for (var i = 0; i < this.labels.length; i++) {
        this.labels[i].setMap(null);
    }

    this.label = new MarkerLabel({
        map: mapMonitor,
        marker: google.maps.Marker,
        text: label
    });
    this.label.bindTo('position', this, 'position');
    // this.label.bindTo('map', google.maps.Marker, 'map');
};

google.maps.Marker.prototype.setLabelS = function (label) {
    for (var i = 0; i < this.labels.length; i++) {
        this.labels[i].setMap(null);
    }

    this.label = new MarkerLabelS({
        map: mapMonitor,
        marker: google.maps.Marker,
        text: label
    });
    this.label.bindTo('position', this, 'position');
    // this.label.bindTo('map', google.maps.Marker, 'map');
};

var MarkerLabel = function (options) {  //var MarkerLabel
    this.setValues(options);
    this.span = document.createElement('span');
    this.span.className = 'map-marker-label mapispan';
};

var MarkerLabelS = function (options) {  //var MarkerLabel
    this.setValues(options);
    this.span = document.createElement('span');
    this.span.className = 'map-marker-label mapispan';
};

MarkerLabel.prototype = $.extend(new google.maps.OverlayView(), {
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

MarkerLabelS.prototype = $.extend(new google.maps.OverlayView(), {
    onAdd: function () {
        this.getPanes().overlayImage.appendChild(this.span);
        var self = this;
        this.listeners = [google.maps.event.addListener(this, 'position_changed', function () { self.draw(); })];
    },
    draw: function () {
        var text = String(this.get('text'));
        var position = this.getProjection().fromLatLngToDivPixel(this.get('position'));
        this.span.innerHTML = text;
        this.span.style.left = (position.x - markerSizeS.x) + 'px';
        this.span.style.top = (position.y - markerSizeS.y) + 'px';
    },
    onRemove: function () {

    }
});

var Monitor = function () {
    this.Core = {
        contextUrl: window.location.origin + "/WebGestionDelivery/",
        apis: {
            urlListar: 'monitor/search',
            urlZonales: 'zonalVenta/search'
        },
        init: function(){
            var me = this;
            
            $('#estado').change(function () {
                me.getPedidos();
            });
            
            me.getZonales();
            me.getPedidos();
            me.loadMapa();
        },
        loadMapa: function(){
            var me = this;
            var lati = -12.140544;
            var long = -76.9851392;

            if (lati !== 0 && long !== 0) {
                var lat = { lat: lati, lng: long }
                uluru = lat;
            }

            mapMonitor = new google.maps.Map(document.getElementById('map'), { /*iniciliza el mapa*/
                center: uluru,
                zoom: 12,
                streetViewControl: false,
                mapTypeId: 'roadmap'
            });

            //estilos
            mapMonitor.mapTypes.set('styled_map', me.mapstyle());
            mapMonitor.setMapTypeId('styled_map');

            //autocompletar
            var autocomplete = new google.maps.places.Autocomplete(document.getElementById('autocompletesearch'), { componentRestrictions: { country: 'Pe' } });
            autocomplete.bindTo('bounds', mapMonitor);
            infoWindow = new google.maps.InfoWindow();

            autocomplete.addListener('place_changed', function () {
                var place = autocomplete.getPlace();
                if (!place.geometry) {

                    window.alert("No details available for input: '" + place.name + "'");
                    return;
                }
                if (place.geometry.viewport) {
                    mapMonitor.fitBounds(place.geometry.viewport);
                } else {
                    mapMonitor.setCenter(place.geometry.location);
                    mapMonitor.setZoom(17);
                }
            });        
        },
        mapstyle: function () {
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
        dibujarPoligonos: function (value) {
            var poligono = new google.maps.Polygon({
                paths: value.coordenadas,
                strokeColor: value.color,
                strokeOpacity: 0.8,
                strokeWeight: 1,
                fillColor: value.color,
                fillOpacity: 0.1
            });
            poligono.setMap(mapMonitor);   
            
            var labelMarker = new google.maps.Marker({
                position: value.coordenadas[0],
                map: mapMonitor,
                title: value.nombre,
                visible: true,
                icon: "data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' version='1.0' width='1.000000pt' height='1.000000pt' viewBox='0 0 1.000000 1.000000' preserveAspectRatio='xMidYMid meet'%3e%3cg transform='translate(0.000000,15.000000) scale(0.100000,-0.100000)' fill='%23000000' stroke='none'%3e%3c/g%3e%3c/svg%3e"
            });
            labelMarker.setLabelS(value.nombre);
        },
        getZonales: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlZonales;
            var url = `${apiUrl}?texto=&ordenamiento=nombre&pagina=0&tamanio=0`;
            
            var exito = function(rpta){
               if(rpta.success){
                  $.each(rpta.data.data, function (index, value) {
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
        getPedidos: function(){
            var me = this;
            var apiUrl = me.contextUrl + me.apis.urlListar;
            var estado = $("#estado").val();
            var url = `${apiUrl}?estado=${estado}`;
            me.limpiarFormas();
            
            var exito = function(rpta){
                if(rpta.apiEstado === 'ok'){
                    $.each(rpta.data, function (index, value) {
                        me.dibujarPedido(value);
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
        dibujarPedido: function (value) {
            var me = this;
            var negro = 'resources/img/personas/hn.png';
            var negrox = 'resources/img/personas/hnx.png';
            var negroc = 'resources/img/personas/hnc.png';
            var negroxa = 'resources/img/personas/hnxa.png';
            var negrot = 'resources/img/personas/hnt.png';
            var negrom = 'resources/img/personas/hnm.png';
            var verde = 'resources/img/personas/hv.png';
            var amarillo = 'resources/img/personas/ha.png';
            var color = value.color;             
            
            var imagen = '';

            if (color === 'verde') {
                imagen = verde;
            } else if (color === 'amarillo') {
                imagen = amarillo;
            } else if (color === 'negro') {
                imagen = negro;                
            } else if (color === 'negrox') {
                imagen = negrox;
            } else if (color === 'negrot') {
                imagen = negrot;
            } else if (color === 'negroa') {
                imagen = negroxa;
            } else if (color === 'negroe') {
                imagen = negroc;
            } else if (color === 'negron') {
                imagen = negrom;
            }
            
            var icono = new google.maps.MarkerImage(me.contextUrl + imagen);
            
            var marker = new google.maps.Marker({
                position: { lat: parseFloat(value.latitud), lng: parseFloat(value.longitud) },
                map: mapMonitor,
                title: value.nombreCliente,
                visible: true,
                icon: icono,
                draggable: true
            });
            
            marker.setLabel(value.nombreCliente);
            var infowindow = new google.maps.InfoWindow({
                content: me.dibujarDetalle(value),
                maxWidth: 300,
                maxHeight: 500
            });

            marker.addListener('click', function () {
                infowindow.open(mapMonitor, marker);
            });

            formas.push(marker);
        },
        dibujarDetalle: function (value) {
            var stringHtml = '';
            var items = value.items.split('|').slice(0, -1);
            
            stringHtml = "<div class=''><p  style='margin:0'><div><p style='margin:0'><strong></strong></p>";
            stringHtml = stringHtml + "<p style='margin:0;text-align: center' ><b style='margin:0;text-align: center'><strong>" + value.codigo + "</strong ></b ></p > ";

            //fecha
            stringHtml = stringHtml + "<div class='row'>";
            stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>event</span></div >"
            stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <b class='text-primary'>" + value.fecha + "</b></div>";
            stringHtml = stringHtml + "</div> ";
            
            //Cliente
            stringHtml = stringHtml + "<div class='row'>";
            stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>account_circle</span></div >"
            stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <b class='text-primary'>" + value.nombreCliente + "</b></div>";
            stringHtml = stringHtml + "</div> ";

            //Direccion
            stringHtml = stringHtml + "<div class='row'>";
            stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>home</span></div >"
            stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <span><strong>" + value.direccion + "</strong></span></div>";
            stringHtml = stringHtml + "</div> ";

            //Items
            $.each(items, function (index, value) {
                var sValor = value.split(',');
                stringHtml = stringHtml + "<div class='row'>";
                stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>shopping_basket</span></div >"
                stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <span><strong> x" + sValor[0] + " " + sValor[1] + "</strong></span></div>";
                stringHtml = stringHtml + "</div> ";
            });                

            //Telefono
            stringHtml = stringHtml + "<div class='row'>";
            stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>phone</span></div >"
            stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <span><strong>" + value.telefono + "</strong></span></div>";
            stringHtml = stringHtml + "</div> ";

            //Monto
            stringHtml = stringHtml + "<div class='row'>";
            stringHtml = stringHtml + "<div class='col-sm-2'> <span class='material-icons'>money</span></div >"
            stringHtml = stringHtml + "<div class='col-sm-10' style='margin:0;text-align: left;padding-left: 0px;padding-top: 4px;'> <span><strong> S\. " + value.total + "</strong></span></div>";
            stringHtml = stringHtml + "</div>";

            return stringHtml;
        },
        limpiarFormas: function () {
            for (var i = 0; i < formas.length; i++) {
                formas[i].setMap(null);
            }
            $("span").remove('.mapispan');
            $(".map-marker-label, .map-marker-label-lg").remove();
        }
    };
};

$(function(e){
    oMonitor = new Monitor();
    oMonitor.Core.init();
});