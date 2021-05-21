<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .modal {
                z-index: 20;
            }

            .modal-backdrop {
                z-index: 10;
            }
        </style>
    </head>
    <body>
        <%@include file="layout.jsp" %>
        <div class="card-body">
        <div class="breadcrumb text-primary" style="padding:0;margin-bottom:0">Pedido/Realizar un Pedido</div>
            <div class="card">
                <div class="card-header">
                    <form asp-action="Create" id="frmGuardar">
                        <input id="id" type="hidden" value=${id} />
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <h3 style="align-content:center;text-align:center"><span><strong class="card-title" style="font-family:Montserrat" id="titulo"> Realizar Pedido</strong></span></h3>
                            </div>

                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <strong>Ingresar datos del Cliente:</strong>
                                <hr />
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static ">Pedido:</label>
                                <div class="col-sm-10 col-md-6 col-lg-6">
                                    <input id="codigo" type="text" class="col-lg-12 form-control " readonly value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static">
                                    <span>(*)&nbsp;</span>
                                    Fecha:
                                </label>
                                <div class="col-sm-8 col-md-6 col-lg-6">
                                    <input id="dtFecha" type="date" name="validar" class="form-control" value="" data-name="Fecha" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static">
                                    <span>(*)&nbsp;</span>
                                    Nombre:
                                </label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input type="text" class="form-control" maxlength="100" id="txtNombre" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row">
                                <label class="col-sm-3 col-form-label position-static"><span>(*)&nbsp;</span>Identificador:</label>
                                <div class="col-sm-3 col-md-3 col-lg-3">
                                    <select id="sltTipoDocumento" class="custom-select">
                                        <option value="">- Sel. Doc. -</option>
                                        <option value="1">DNI</option>
                                        <option value="2">RUC</option>
                                        <option value="3">CARNET EXT</option>
                                    </select>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-6">
                                    <input id="txtNumeroDocumento" maxlength="11" type="text" class="form-control" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row">
                                <label class="col-sm-3 col-form-label  position-static">Móvil:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input type="text" class="form-control" maxlength="9" id="txtMovil" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row">
                                <label class="col-sm-3 col-form-label  position-static">T. Fijo:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input type="text" class="form-control" maxlength="7" id="txtTelefono" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row">
                                <label class="col-sm-3 col-form-label position-static">Email:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input id="txtEmail" type="text" maxlength="100" class="form-control" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static "><span>(*)&nbsp;</span>Distrito:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input id="txtDistrito" readonly type="text" maxlength="100" class="form-control" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static">
                                    <span>(*)&nbsp;</span>Dirección:
                                </label>
                                <div class="col-sm-8 col-md-8 col-lg-8">
                                    <input id="txtDireccion" readonly type="text" name="validar" maxlength="100" class="form-control" data-name="Dirección" value="" />
                                </div>
                                <span id="mapaDireccion" type="button" class="col-sm-1 col-1 input-group-addon btn btn-outline-primary"
                                      data-toggle="modal" data-target="#modalmapa" style="margin:0;padding:0">
                                    <span class="material-icons" style="padding-top: 0px;margin-top: 0px;vertical-align:middle;">location_on</span>
                                </span>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static">Referencia:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <input id="txtReferencia" readonly type="text" maxlength="100" class="form-control" value="" />
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static"><span>(*)&nbsp;</span>Zonal:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <select id="sltZonalVenta" class="custom-select">
                                        <option value="">- Sel. Zonal -</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static"><span>(*)&nbsp;</span>Estado:</label>
                                <div class="col-sm-9 col-md-9 col-lg-9">
                                    <select id="sltEstado" class="custom-select">
                                        <option value="">- Sel. Estado -</option>
                                        <option value="1">Pendiente</option>
                                        <option value="2">En Camino</option>
                                        <option value="3">Entregado</option>
                                        <option value="4">No Entregado</option>
                                        <option value="5">Cancelado</option>
                                    </select>
                                </div>
                            </div>


                        </div>

                        <br /><br />
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <div style="display:inline;">
                                    <strong>Indicar Productos:</strong>
                                    <span id="addItem" class="btn btn-primary" style="margin:2px 2px 2px 2px;padding:2px 2px 2px 2px">
                                        <span class="material-icons">add</span>
                                    </span>
                                </div>

                                <hr />
                            </div>

                            <div class="card fadeIn animated" style="margin-top:0px">

                                <div class="card-body" style="padding-top:0px">
                                    <div class="card" style="justify-content:center;align-items:center">
                                        <table id="tablaResultado" class="table table-hover" style="text-align:center;">
                                            <thead class="card-header card-header-primary">
                                                <tr>
                                                    <th style="width:15px"></th>
                                                    <th style="width:500px">Producto</th>
                                                    <th style="width:100px">Cantidad</th>
                                                    <th style="text-align:right;width:100px">Tarifa</th>
                                                    <th style="text-align:right;width:100px">
                                                        Total
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody class="card-body"></tbody>
                                        </table>
                                    </div>
                                    <div class="form-group col-md-12 col-lg-12 row position-static" style="margin:0;padding:0">
                                        <label class="col-sm-11 col-md-11 col-form-label position-static">Total:</label>
                                        <div class="col-sm-1 col-md-1 col-lg-1 " style="margin:0;padding:0">
                                            <input type="text" id="total" class="form-control col-lg-12" disabled style="width:100%;text-align:right" value="" />
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>


                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <strong>Pago:</strong>
                                <hr />
                            </div>
                            
                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-4 col-form-label position-static"><span>(*)&nbsp;</span>Método de pago:</label>
                                <div class="col-sm-10 col-md-10 col-lg-6 input-group">
                                    <select id="sltMetodoPago" class="custom-select">
                                        <option value="">-- Seleccione Método Pago --</option>
                                        <option value="1">Efectivo</option>
                                        <option value="2">POS</option>
                                    </select>
                                </div>

                            </div>
                            
                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-4 col-form-label position-static"><span>(*)&nbsp;</span>Comprobante:</label>
                                <div class="col-sm-8 col-md-8 col-lg-8 input-group">
                                    <select id="sltComprobante" class="custom-select">
                                        <option value="">-- Seleccione Comprobante --</option>
                                        <option value="1">Boleta</option>
                                        <option value="2">Factura</option>
                                    </select>
                                    <span id="btnfac" class="col-sm-1 col-1 btn btn-outline-primary" style="margin:0;padding:0"
                                          data-toggle="modal" data-target="#modalfactura">
                                        <span style="padding-top: 0px;margin-top: 0px;vertical-align:middle;" class="material-icons">payment</span>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group col-md-6 col-lg-6 row position-static">
                                <label class="col-sm-3 col-form-label position-static">Total:</label>
                                <div class="col-sm-10 col-md-10 col-lg-6 input-group">
                                    <input id="txtMonto" disabled type="text" class="form-control col-lg-10 position-static"
                                           maxlength="100"
                                           style="width:100%; padding-left:15px;margin-left:35px" value="" />
                                </div>

                            </div>
                            <div class="form-group col-md-6 col-lg-6 row position-static">


                            </div>
                            <div class="card-body" style="margin-bottom:0;margin-top:0;padding-bottom:0;padding-top:0;margin-left:18px">
                                <label class="col-sm-3 col-form-label">Comentario:</label>
                            </div>
                            <div class="form-group row col-lg-12 col-md-12 position-static">
                                <input id="txtComentario" type="text" class="form-control col-lg-10 position-static"
                                       maxlength="100"
                                       style="width:100%; padding-left:15px;margin-left:35px" value="" />
                            </div>
                        </div>
                    </form>
                    <div class="form-group row">
                        <div class="col-sm-6 col-lg-12 col-md-12 centrado">
                            <button id="btnGuardar" type="button" class="btn btn-primary"><span class="material-icons">save</span>Guardar</button>
                            <a href="pedido" class="btn btn-secondary">
                                <span class="material-icons" style="padding-right:5px">backspace</span>
                                Volver
                            </a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        
        <!--modal-->
        <%@include file="modalMap.jsp" %>
        <%@include file="modalFactura.jsp" %>
        <script src="${pageContext.request.contextPath}/resources/js/gmaps.ubigeo.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/administracion/pedidoGuardar.js"></script>
    
    </body>
</html>
