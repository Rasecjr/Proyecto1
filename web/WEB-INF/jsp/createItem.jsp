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
            <div class="breadcrumb text-primary" style="padding:0;margin-bottom:0">Administración/Productos</div>
            <div class="card">
                <div class="col-lg-12 col-md-12 col-sm-12 centrado">
                    <h3><span><strong class="card-title" style="font-family:Montserrat" id="titulo">Crear Nuevo Producto</strong></span></h3>
                </div>
                
                <form asp-action="Create" id="frmGuardar">
                    <input id="id" type="hidden" value=${id} />
                    <div class="row">
                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">(*) C&#243;digo:</label>
                            <div class="col-sm-6 col-md-6 col-lg-6">
                                <input id="txtCodigo" type="text" class="form-control" readonly value="" />
                            </div>
                        </div>
                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">(*) Clase:</label>
                            <div class="col-sm-6 col-md-6 col-lg-6">
                                <select id="sltClase" name="sltClase" class="custom-select">
                                    <option value="0">-- Seleccione Clase --</option>
                                    <option value="1">Carga</option>
                                    <option value="2">Envase</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">(*) Nombre:</label>
                            <div class="col-sm-9 col-md-9 col-lg-9">
                                <input id="txtNombre" maxlength="100" name="txtNombre" type="text" class="form-control" value="" />
                            </div>
                        </div>

                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">(*) Nombre Comercial:</label>
                            <div class="col-sm-9 col-md-9 col-lg-9">
                                <input id="txtNombreComercial" maxlength="100" name="txtNombreComercial" type="text" class="form-control" value="" />
                            </div>
                        </div>

                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">Descripci&#243;n:</label>
                            <div class="col-sm-9 col-md-9 col-lg-9">
                                <input id="txtDescripcion" maxlength="100" name="txtDescripcion" type="text" class="form-control" value="" />
                            </div>
                        </div>

                        <div class="form-group col-lg-6 row">
                            <label class="col-sm-3 col-form-label position-static">(*) Estado:</label>
                            <div class="col-sm-6 col-md-6 col-lg-6">
                                <select id="sltEstado" name="sltEstado" class="custom-select">
                                    <option value="0">-- Seleccione Estado --</option>
                                    <option value="1">Activo</option>
                                    <option value="2">Inactivo</option>
                                </select>
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
                        <a href="item" class="btn btn-secondary">
                            <span class="material-icons">backspace</span>
                            Volver
                        </a>
                    </div>
                </div>
            </div>          
        </div>
        <script src="${pageContext.request.contextPath}/resources/js/administracion/itemGuardar.js"></script>
    </body>
</html>
