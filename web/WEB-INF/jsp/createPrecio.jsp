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
            <div class="breadcrumb text-primary" style="padding:0;margin-bottom:0">Administración/Precio Vigencia</div> 
            <div class="card">
                <div class="col-lg-12 col-md-12 col-sm-12 centrado">
                    <h3><span><strong class="card-title" style="font-family:Montserrat" id="titulo">Crear Nuevo Precio Vigencia</strong></span></h3>
                </div>
                
                <form asp-action="Create" id="frmGuardar">
                    <input id="id" type="hidden" value=${id} />
                    <div class="row">
                        <div class="form-group col-lg-12 row">
                            <label class="col-sm-3 col-form-label position-static" style="text-align:right">(*) Producto:</label>
                            <div class="col-sm-4 col-md-4 col-lg-4">
                                <select id="sltProducto" class="custom-select" style="text-align-last:left !important;">
                                    <option value="">-- Seleccione Producto --</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group col-lg-12 row">
                            <label class="col-sm-3 col-form-label position-static" style="text-align:right">(*) Precio Base:</label>
                            <div class="col-sm-3 col-md-2 col-lg-2">
                                <input id="txtPrecioBase" type="number" step='0.01' style="text-align:right" class="form-control" value="" min="0"/>
                            </div>
                            <label class="col-sm-1 col-form-label position-static">Inc IGV.</label>
                        </div>
                        <div class="form-group col-lg-12 row">
                            <label class="col-sm-3 col-form-label position-static" style="text-align:right">(*) Vigencia Desde:</label>
                            <div class="col-sm-2 col-md-2 col-lg-2">
                                <input id="txtVigenciaDesde" type="date" style="text-align:right" class="form-control" value="" placeholder="dd/mm/yyyy" />
                            </div>
                        </div>
                    </div>
                </form>
                
                <div class="form-group row centrado">
                    <div class="col-sm-4 offset-md-2">
                        <button id="btnGuardar" type="button" class="btn btn-primary">
                            <span class="material-icons">save</span>
                            Guardar
                        </button>&nbsp;
                        <a href="precio" class="btn btn-secondary">
                            <span class="material-icons">backspace</span>
                            Volver
                        </a>
                    </div>
                </div>
                
                <div class="card-body">
                    <div class="row" style="margin:0 0 0 0;padding:0 0 0 0;">
                        <label>Historial de cambios de precio</label>
                        <input type="hidden" id="hdnorden" value="" />
                        <table class="table table-sm panel-content-border border-bottom border-left border-right table-hover" style="margin:0 0 0 0;padding:0 0 0 0" id="tablaResultado">
                            <thead class="card-header card-header-primary col-lg-12">
                                <tr>
                                    <th scope="col" width="100px">
                                        <a class="btn-default btn-link-head" data-order="codigo">
                                            <span id="codigoOrden"></span>Código
                                        </a>
                                    </th>
                                    <th scope="col">
                                        <a class="btn-default btn-link-head" data-order="nombre">
                                            <span id="nombreOrden"></span>&nbsp;Producto
                                        </a>
                                    </th>
                                    <th scope="col">
                                        <a class="btn-default btn-link-head" data-order="clase">
                                            <span id="nombreClase"></span>&nbsp;Clase
                                        </a>
                                    </th>
                                    <th scope="col" width="110px">
                                        <a class="btn-default btn-link-head" data-order="precioBase">
                                            <span id="precioOrden"></span>&nbsp;Precio Base
                                        </a>
                                    </th>
                                    <th scope="col" width="150px">
                                        <a class="btn-default btn-link-head" data-order="vigenciaDesde">
                                            <span id="vigenciaDesdeOrden"></span>&nbsp;Vigencia Desde
                                        </a>
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
        <script src="${pageContext.request.contextPath}/resources/js/administracion/precioGuardar.js"></script>
    </body>
</html>
