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
            <div class="breadcrumb text-primary" style="padding:0;margin-bottom:0">Seguridad/Roles</div>
            <div class="card">
                <div class="card-header">
                    <form id="frmBuscar">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 centrado">
                                <h3 style="align-content:center;text-align:center"><span><strong class="card-title" style="font-family:Montserrat">Roles</strong></span></h3>
                                <a href="createRol" style="float:right !important;right:0;margin-right:15px;position:absolute;margin-top:5px;"  class="btn btn-primary">
                                    <span class="fa fa-plus"> </span>  Crear
                                </a>
                            </div>
                            <div class="form-group col-md-10 col-lg-10 row">
                                <label class="col-sm-1 col-xl-1 col-md-1 col-lg-1 col-form-label position-static left">Buscar:</label>
                                <div class="col-sm-12 col-md-6 col-lg-6 input-group">
                                    <input type="text" class="form-control" id="texto" name="texto" value="" placeholder="Buscar por código o nombre" />
                                    <button id="btnBuscar" type="button" class="btn btn-primary input-group-addon" style="padding-left:5px;margin-top:0;height:37px">
                                    <span class="material-icons" style="padding-right:5px">search</span>Buscar</button>
                                </div>

                            </div>
                            <div class="col-lg-2 col-md-2">
                                <button id="btnExportar" type="button" class="btn btn-primary" style="float:right;right:0;margin-top:10px;height:37px"><span class="material-icons" style="padding-right:5px">cloud_download</span>exportar</button>
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
                                        <th scope="col" width="100px">
                                            <a class="btn-default btn-link-head" data-order="codigo">
                                                <span id="codigoOrden"></span>Código
                                            </a>
                                        </th>
                                        <th scope="col" width="100px">
                                            <a class="btn-default btn-link-head" data-order="nombre">
                                                <span id="nombreOrden"></span>&nbsp;Nombre
                                            </a>
                                        </th>
                                        <th scope="col" width="100px">
                                            <a class="btn-default btn-link-head" data-order="formularios">
                                                <span id="formulariosOrden"></span>&nbsp;Formularios
                                            </a>
                                        </th>
                                        <th scope="col" width="100px">
                                            <a class="btn-default btn-link-head" data-order="fecha">
                                                <span id="fechaOrden"></span>&nbsp;Fecha de Creación
                                            </a>
                                        </th>                                
                                        <th scope="col" width="50px">
                                            <a class="btn-default btn-link-head" data-order="estado">
                                                <span id="estadoOrden"></span>&nbsp;Estado
                                            </a>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody id="trol" class="card-body">

                                </tbody>
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
        <script src="${pageContext.request.contextPath}/resources/js/seguridad/rolListar.js"></script>
    </body>
</html>
