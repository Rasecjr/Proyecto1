<%-- 
    Document   : modalFactura
    Created on : 18/12/2020, 12:05:18 AM
    Author     : José
--%>

<div class="modal" tabindex="-1" role="dialog" id="modalfactura">
    <div class="modal-dialog-centered" role="document" style="margin-left:5px;padding-left:5px">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Comprobante de Pago <span id="spnTipo"></span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true"></span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="form-group col-md-6 col-lg-6 row">
                        <label id="lblNombre" class="col-sm-3 col-form-label position-static">(*)Razón Social o Nombre:</label>
                        <div class="col-sm-8 col-md-8 col-lg-9">
                            <input id="txtClienteCP" type="text" class="form-control" value="" />
                        </div>
                    </div>
                    <div class="form-group col-lg-6 row">
                        <label class="col-sm-3 col-form-label position-static"></label>
                        <div class="col-sm-12 col-md-6 col-lg-6">
                        </div>
                    </div>

                    <div class="form-group col-md-6 col-lg-6 row">
                        <label class="col-sm-3 col-form-label position-static">(*) Tipo Documento:</label>
                        <div class="col-sm-6 col-md-6 col-lg-6">
                            <select id="sltTipoDocumentoCP" name="sltTipoDocumentoCP" class="custom-select">
                                <option value="">-- Seleccione Tipo Doc. --</option>
                                <option value="1">DNI</option>
                                <option value="2">RUC</option>
                                <option value="3">CARNET EXT</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group col-md-6 col-lg-6 row">
                        <label class="col-sm-3 col-form-label position-static">(*) Número Documento:</label>
                        <div class="col-sm-9 col-md-9 col-lg-9">
                            <input id="txtNumeroDocumentoCP" maxlength="100" name="txtNumeroDocumentoCP" type="text" class="form-control" value="" />
                        </div>
                    </div>

                    <div class="form-group col-md-6 col-lg-6 row">
                        <label class="col-sm-3 col-form-label  position-static">(*)Dirección:</label>
                        <div class="col-sm-8 col-md-8 col-lg-9">
                            <input type="text" class="form-control" id="txtDireccionCP" value="" />
                        </div>
                    </div>
                </div>
                
            </div>
            <div class="modal-footer">
                <button type="button" id="btnGuardarComprobante" class="btn btn-primary"><span class="material-icons" style="padding-right:5px">save</span>Guardar</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><span class="material-icons" style="padding-right:5px">backspace</span>salir</button>
            </div>
        </div>
    </div>
</div>