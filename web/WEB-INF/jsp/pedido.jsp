<%-- 
    Document   : pedido
    Created on : 03/12/2020, 02:40:45 AM
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
        <div class="card-body">
            <div class="breadcrumb text-primary" style="padding:0;margin-bottom:0">Pedidos/Historial de Pedidos</div>
            <div class="card">
                <div class="card-header">
                    <form id="frmBuscar">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <h3 style="align-content:center;text-align:center"><span><strong class="card-title" style="font-family:Montserrat">Historial de Pedidos</strong></span></h3>
                            </div>
                            <div class="row">
                                <div class="form-group col-lg-3 row">
                                    <label class="col-sm-4 col-form-label position-static">Inicio:</label>
                                    <div class="col-sm-8 col-md-8 col-lg-8">
                                        <input type="date" class="form-control" id="ini" />
                                    </div>
                                </div>
                                <div class="form-group col-lg-3 row">
                                    <label class="col-sm-4 col-form-label position-static">Fin:</label>
                                    <div class="col-sm-10 col-md-8 col-lg-8">
                                        <input type="date" class="form-control" id="fin" />
                                    </div>
                                </div>
                       
                                <div class="form-group col-lg-3 row">
                                    <label class="col-sm-4 col-form-label position-static">Estados:</label>
                                    <div class="col-sm-10 col-md-8 col-lg-8">
                                        <select id="estado" class="custom-select">
                                            <option value="0">Todos</option>
                                            <option value="1">Pendiente</option>
                                            <option value="2">En Camino</option>
                                            <option value="3">Entregado</option>
                                            <option value="4">No Entregado</option>
                                            <option value="5">Cancelado</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group col-lg-3 row searchable">
                                    <label class="col-sm-4 col-form-label position-static"> Distritos:</label>
                                    <div class="col-sm-10 col-md-10 col-lg-8">
                                        <select id="distrito" class="custom-select">
                                            <option value="">Todos</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group col-lg-4 row">
                                    <label class="col-sm-5 col-form-label position-static"> Zonal de Ventas:</label>
                                    <div class="col-sm-10 col-md-10 col-lg-7">
                                        <select id="sltZonalVenta" class="custom-select">
                                            <option value="">Todos</option>
                                        </select>
                                    </div>
                                </div>
                                
                                <div class="col-lg-8">
                                    <button type="button" id="btnExportar" class="btn btn-primary" style="padding-left:5px;margin-top:0;height:37px;float:right;margin-right:15px"><span class="material-icons" style="padding-right:5px">cloud_download</span>exportar</button>                                     
                                    <button type="button" id="btnBuscar" class="btn btn-primary" style="padding-left:5px;margin-top:0;height:37px;float:right;margin-right:15px"><span class="material-icons" style="padding-right:5px">search</span>Buscar</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="card-body fadeIn animated" style="margin:0 0 0 0;padding:0 0 0 0;">
                        <div class="row" style="margin:0 0 0 0;padding:0 0 0 0;">
                            <input type="hidden" id="hdnorden" value="" />
                            <table class="table table-hover" style="margin:0 0 0 0;padding:0 0 0 0" id="tablaResultado">
                                <thead class="card-header card-header-primary col-lg-12">
                                    <tr>
                                        <th style="width:15px">

                                        </th>
                                        <th style="width:15px">

                                        </th>
                                        <th style="width:100px !important">
                                            <a data-order="codigo" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Código
                                        </th>
                                        <th style="width:250px !important">
                                            <a data-order="distrito" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Distrito
                                        </th>
                                        <th style="width:300px !important">
                                            <a data-order="zonal" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Zonal de Venta
                                        </th>
                                        <th style="width:120px !important">
                                            <a data-order="fecha" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Fecha Pedido
                                        </th>
                                        <th style="width:120px !important">
                                            <a data-order="cantidad" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Total Productos
                                        </th>
                                        <th style="width:120px !important">
                                            <a data-order="fechaPedido" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Fecha y Hora
                                        </th>
                                        <th style="width:80px !important">
                                            <a data-order="nombreEstado" style="cursor:pointer"><span class="fa fa-sort "></span></a>
                                            Estado
                                        </th>
                                    </tr>
                                </thead>
                                <tbody id="trol" class="card-body"></tbody>
                            </table>
                        </div>
                        
                        <div class="float-right paddingRight-10 marginTop-10">
                            Total de registros: <span class="card-header-primary mini-btn" id="totalRegistros">0</span>
                        </div><br /><br />
                        
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

                                <li class="page-item ">
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
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/resources/js/administracion/pedidoListar.js"></script>
    </body>
</html>
