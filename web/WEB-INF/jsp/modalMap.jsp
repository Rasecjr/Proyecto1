<%-- 
    Document   : modalMap
    Created on : 18/12/2020, 12:05:07 AM
    Author     : José
--%>

<!--modal mapa-->
<style>

    #textsearch {
        background-color: #FFF;
        z-index: 20;
        position: fixed;
        display: inline-block;
        float: left;
    }

    .modal {
        z-index: 20;
    }

    .modal-backdrop {
        z-index: 10;
    }

    .map-marker-label {
        position: absolute;
        color: black; /*whitesmoke;*/
        font-size: 12px;
        font-weight: bold;
        border-radius: 20px 20px 20px 20px !important;
        padding: 2px 5px 2px 5px !important;
        border: solid;
        border-bottom-width: thin;
        border-top-width: thin;
        border-right-width: thin;
        border-left-width: thin;
        margin: 2px;
        background-color: white;
    }

    .map-marker-label-lg {
        position: absolute;
        color: black; /*whitesmoke;*/
        font-size: 12px;
        font-weight: bold;
        border-radius: 20px 20px 20px 20px !important;
        padding: 2px 5px 2px 5px !important;
        margin: 2px;
    }

    .gm-style-iw {
        height: 350px;
        width: 300px !important;
        max-height: 350px !important;
    }

    .gm-style-iw-d {
        height: 350px;
        max-height: 350px !important;
    }
</style>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?&key=AIzaSyA4oJX-POu2gP1lntaRX-t9K9velO2DUEg&libraries=geometry,places"></script>
<div class="modal fade" id="modalmapa" tabindex="-1" role="dialog" aria-labelledby="modalmapaTitle" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Búsqueda de la dirección:</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <input type="text" id="textsearch" class="form-control" style="width:400px;z-index:99999;background-color:rgb(0,95,181); color:white;"/>
                <input type="hidden" id="latitud" class="form-control" style=" z-index:99999" />
                <input type="hidden" id="longitud" class="form-control" style="z-index:99999" />
                <div id="mapmodal" style="height:60vh;width:100%"></div>
                <div class="row">
                    <div class="form-group col-md-6 col-lg-6 row position-static">
                        <label class="col-sm-4 col-form-label position-static "><span>(*)&nbsp;</span>Distrito:</label>
                        <div class="col-sm-8 col-md-8 col-lg-8">
                            <select id="sltDistrito" class="custom-select">
                                <option value="">- Sel. Distrito -</option>
                            </select>

                        </div>
                    </div>
                    <div class="form-group col-md-6 col-lg-6 row position-static">
                        <label class="col-sm-4 col-form-label position-static">Referencia:</label>
                        <div class="col-sm-8 col-md-8 col-lg-8">
                            <input id="txtReferenciaDir" type="text" maxlength="100" class="form-control" value="" />
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal footer -->
            <div class="modal-footer">
                <button id="btnAgregarDireccion" type="button" class="btn btn-primary">Agregar</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">Cancelar</button>
            </div>
        </div>
    </div>
</div>

