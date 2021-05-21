<%-- 
    Document   : zonalVenta
    Created on : 22/11/2020, 02:47:34 AM
    Author     : José
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <%@include file="layout.jsp" %>
       <div class="card-body" style="background-color: #FFFFFF;">
           <div class="card">
                <div class="row"> 
                    <div class="form-group col-lg-4 row">
                        <div class="panel-heading" style="position: absolute;">
                            <strong class="card-title">Mis Zonales de Venta</strong>
                            <button id="btnCrear" class="btn btn-primary  mini-btn-2" onclick=""><span class="fa fa-plus"></span></button>
                            <button id="btnExportar" type="button" class="btn btn-primary" style="float:right;right:0;margin-top: -7px;height: 30px;margin-left: 80px;"><span class="material-icons" style="padding-right:5px">cloud_download</span>exportar</button>
                        </div>
                        <!-- Listado -->
                        <div class="panel-body" style="margin:25px 0 0 0;padding:0">
                            <div class="row" style="margin:0 0 0 0;padding:0 0 0 0;">
                                <input type="hidden" id="hdnorden" value="" />
                                <table class="table table-sm panel-content-border border-bottom border-left border-right table-hover" style="margin:0 0 0 0;padding:0 0 0 0" id="tablaResultado">
                                    <thead class="card-header card-header-primary col-lg-12">
                                        <tr>
                                            <th style="width:15px">
                                            </th>
                                            <th style="width:15px">
                                            </th>
                                            <th style="width:100px !important;padding:0;margin:0">
                                                <a name="codigo" style="padding:1px;font-size:15px;">
                                                    <span id="codigoOrden"></span>Código
                                                </a>
                                            </th>
                                            <th style="width:100px !important;padding:0;margin:0">
                                                <a name="nombre" style="padding:1px;font-size:15px;">
                                                    <span id="nombreOrden"></span>Zonal de Venta
                                                </a>
                                            </th>
                                            <th style="width:100px !important;padding:0;margin:0">
                                                <a name="estado" style="padding:1px;font-size:15px;">
                                                    <span id="estadoOrden"></span>Estado
                                                </a>

                                            </th>
                                            <th style="width:100px !important;padding:0;margin:0">
                                                <a name="esPropio" style="padding:1px;font-size:15px;">
                                                    <span id="esPropioOrden"></span>Es Propio
                                                </a>

                                            </th>
                                            <th style="width:100px !important;padding:0;margin:0">
                                                <a name="color" style="padding:1px;font-size:15px;">
                                                    <span id="colorOrden"></span>Color
                                                </a>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody id="trol" class="card-body">

                                    </tbody>
                                </table>
                            </div>
                            
                            <div class="row" style="margin:0;padding:0">
                                <div class="col-6"></div>
                                <div class="col-6 float-right paddingRight-10 marginTop-10">
                                    <div style="height: 19px;text-align: left !important;">Total de registros:</div>
                                    <span class="float-right card-header-primary mini-btn" style="padding: 2px; font-size: smaller" id="totalRegistros">0</span>
                                </div>
                            </div>
                            
                            <div class="row" style="margin:0;padding:0">
                                <nav aria-label="pagination" id="paginacion" class="pagination centrado text-primary">
                                    <ul class="pagination justify-content-center text-primary">
                                        <li class="page-item">
                                            <button id="btnPrimero" class="page-link" aria-label="Primero">
                                                <span aria-hidden="true">&#124;&lt;</span>
                                                <span class="sr-only">Primero</span>
                                            </button>
                                        </li>
                                        
                                        <li class="page-item">
                                            <button id="btnAnterior" class="page-link" aria-label="Anterior">
                                                <span aria-hidden="true">&lt;</span>
                                                <span class="sr-only">Anterior</span>
                                            </button>
                                        </li>
                                        <li class="page-item paddingLeft-10 textoBold">
                                            <div class="paddingTop-5 paddingBottom-10 nav-green">
                                                Página
                                            </div>
                                        </li>

                                        <li class="page-item paddingRight-10 paddingLeft-10">
                                            <div class=" paddingBottom-10">
                                                <select class="custom-select" style="height:30px"
                                                        name="numeropagina" id="numeropagina"></select>
                                            </div>
                                        </li>

                                        <li class="page-item paddingRight-10 paddingBottom-10 textoBold">
                                            <div class="paddingTop-5 nav-green">
                                                de <span id="totalPagina" class="nav-green"></span>
                                            </div>
                                        </li>

                                        <li class="page-item">
                                            <button id="btnSiguiente" class="page-link">
                                                <span aria-hidden="true">&gt;</span>
                                                <span class="sr-only">Siguiente</span>
                                            </button>

                                        </li>

                                        <li class="page-item">
                                            <button id="btnUltimo" class="page-link" aria-label="Ultimo">
                                                <span aria-hidden="true">&gt;&#124;</span>
                                                <span class="sr-only">Ultimo</span>
                                            </button>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                        <!-- End Listado -->
                        
                        <!-- Formulario -->
                        <div id="formulario" style="display: none;">
                            <div class="card" style="margin:0;padding:0">
                                <input id="id" type="hidden" value="" />
                                <div class="card-body row" style="margin:0;padding:0">
                                    <div class="col-lg-12 row">
                                        <strong id="titulo" style="font-size: 15px;font-weight: normal;color: #3c4858;">&nbsp;&nbsp;Crear Zonal Venta</strong>
                                    </div>
                                    <br />
                                    
                                    <div class="col-lg-12 row">
                                        <label class="col-sm-5 col-form-label position-static"
                                               style="text-align: left !important;">Código:</label>
                                        <div class="col-sm-7 col-md-7 col-lg-7">
                                            <input id="txtCodigo" type="text" class="form-control" value="" readonly />
                                        </div>
                                    </div>
                                    
                                    <div class=" col-lg-12 row">
                                        <label class="col-sm-5 col-form-label position-static" style="text-align: left !important;">Nombre:</label>
                                        <div class="col-sm-7 col-md-7 col-lg-7">
                                            <input type="text" style="text-align:left" class="form-control" id="txtNombre" value="" />
                                        </div>
                                    </div>
                                    
                                    <div class=" col-lg-12 row">
                                        <label class="col-sm-5 col-form-label position-static" style="text-align: left !important;">Teléfono</label>
                                        <div class="col-sm-7 col-md-7 col-lg-7">
                                            <input type="text" style="text-align:left" maxlength="9" class="form-control" id="txtTelefono" value="" />
                                        </div>
                                    </div>
                                    
                                    <div class="col-lg-12 row" style="margin:0;padding:0">
                                        <label class="col-sm-5 col-form-label position-static" style="text-align: left !important;">Distrito:</label>
                                        <div class="col-sm-6 col-md-6 col-lg-6" style="padding-right:1px">
                                            <select id="sltDistrito" class="custom-select">
                                                <option value="">Seleccione</option>
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <div class="col-lg-12 row">
                                        <label class="col-sm-5 position-static" style="text-align: left !important;">Color:</label>
                                        <div class="col-sm-7 col-md-7 col-lg-3">
                                            <input type="color" id="tc_color" style="text-align:left" class="" value="#ecc300" />
                                        </div>
                                        <!---<span class="material-icons">apps</span>-->
                                    </div>
                                    <div class="col-lg-12 row">
                                        <label class="col-sm-5 position-static" style="text-align: left !important;">Es Propio:</label>
                                        <div class="col-sm-7 col-md-7 col-lg-7">
                                            <select id="sltEsPropio" class="custom-select">
                                                <option value="">Seleccione</option>
                                                <option value="1">SI</option>
                                                <option value="0">NO</option>
                                            </select>
                                        </div>
                                        <!---<span class="material-icons">apps</span>-->
                                    </div>
                                    
                                    <div class="col-lg-12 row">
                                        <label class="col-sm-5 position-static" style="text-align: left !important;">Estado:</label>
                                        <div class="col-sm-7 col-md-7 col-lg-7">
                                            <select id="sltEstado" class="custom-select">
                                                <option value="">Seleccione</option>
                                                <option value="1">Activo</option>
                                                <option value="2">Inactivo</option>
                                            </select>
                                        </div>
                                        <!---<span class="material-icons">apps</span>-->
                                    </div>
                                    <br /><br />
                                    
                                    <div class="col-lg-12 row">
                                        <div class="form-group col-lg-12 row" style="margin:0;padding:0">
                                            <span>
                                                <button id="addPts" type="button" class="btn btn-primary mini-btn-2"
                                                        data-toggle="modal" data-target="#modalMensaje"
                                                        style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px">
                                                    <span class="fa fa-plus" style="vertical-align:middle"></span>
                                                </button>Añadir puntos
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row" style="overflow-y:scroll;height:100px;margin:0;padding:0">
                                <table class="table table-hover" style="margin:0 0 0 15px;font-size:11px">
                                    <tbody id="div_t">
                                        <tr>
                                            <td>
                                                Punto 1:
                                            </td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Punto 2:
                                            </td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Punto 3:
                                            </td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            
                            <div class="form-group col-lg-12 row">
                                <button id="btnGuardar" type="button" class="btn btn-primary" >
                                    <span class="material-icons">save</span>Guardar
                                </button>
                                <button id="btnCancelar"  class="btn btn-secondary">
                                    <span class="material-icons">backspace</span>
                                    Cancelar
                                </button>
                            </div>
                        </div>
                        <!-- End Formulario -->
                    </div>
                    
                    <div class="form-group col-lg-8 row">
                        <div class="col-lg-6 col-md-6 col-xs-12 ">
                            <input type="text" id="autocompletesearch2" class="form-control col-lg-12" style="background-color:rgb(0,95,181); color:white" />
                        </div>
                        
                        <div id="mapRegistro" class="content-center col-lg-12 col-md-12 col-xs-12" style="height: 680px;margin-top:5px;margin-left:0px;margin-right:15px;overflow:hidden;width:90%;position:relative;"></div>
                    </div>
                </div>
            </div>
       </div>
       <!-- The Modal -->
        <div class="modal fade" id="modalMensaje" tabindex="-1" role="dialog" aria-labelledby="modalMensajeLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <!-- Modal Header -->
                    <div class="modal-header">
                        <h4 class="modal-title">Añadir puntos</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <!-- Modal body -->
                    <div class="modal-body">
                        Primero pulsa el bot&oacute;n Aplicar y luego haz click sobre el mapa con el bot&oacute;n izquierdo del rat&oacute;n para comenzar a añadir puntos a la nueva Geocerca.
                    </div>
                    <!-- Modal footer -->
                    <div class="modal-footer">
                        <button id="btnAplicar" type="button" class="btn btn-info" data-dismiss="modal" >Aplicar</button>
                    </div>
                </div>
            </div>
        </div>       
       
        <input type="hidden" id="hdntextvalidar" />
        <input type="hidden" id="hdntext" />
        
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?&key=AIzaSyA4oJX-POu2gP1lntaRX-t9K9velO2DUEg&libraries=geometry,places"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sweetalert2.all.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/administracion/zonalVenta.js"></script>
    </body>
</html>