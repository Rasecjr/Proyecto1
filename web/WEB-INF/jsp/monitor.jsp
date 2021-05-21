<%-- 
    Document   : monitor
    Created on : 03/12/2020, 02:41:14 AM
    Author     : José
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
        .map-marker-label {
            position: absolute;
            color: black;/*whitesmoke;*/
            font-size: 12px;
            font-weight: bold;
            border-radius: 20px 20px 20px 20px !important;
            padding: 2px 5px 2px 5px !important;
            border: solid;
            border-bottom-width:thin;
            border-top-width:thin;
            border-right-width:thin;
            border-left-width:thin;
            margin: 2px;
            background-color: white;
        }

        .map-marker-label-lg {
            position: absolute;
            color: black;/*whitesmoke;*/
            font-size: 12px;
            font-weight: bold;
            border-radius: 20px 20px 20px 20px !important;
            padding: 2px 5px 2px 5px !important;
            margin: 2px;
        }
    </style>
    </head>
    <body>
        <%@include file="layout.jsp" %>
        <div class="card-body">
            <div class="row" style="width:100%;text-align: center">
                <div class="col-lg-4 col-md-4 col-xs-12">
                    <input type="text" id="autocompletesearch" class="form-control col-lg-12" style="background-color:rgb(0,95,181); color:white" />
                </div>
                <div class="col-lg-5 col-md-5 col-xs-12">

                </div>
                <div class="col-lg-3 col-md-3 col-xs-12">
                    <select id="estado" class="custom-select">
                        <option value="0">Todos</option>
                        <option value="1">Pendiente</option>
                        <option value="2">En Camino</option>
                        <option value="3">Entregado</option>
                        <option value="4">No Entregado</option>
                        <option value="5">Cancelado</option>
                    </select>
                </div>
                <div id="map" class="content-center col-lg-12 col-md-12 col-xs-12" style="height: calc(100vh - 35vh);margin-top:5px;margin-left:15px;margin-right:15px;overflow:hidden;width:100%"></div>
            </div>
        </div>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?&key=AIzaSyA4oJX-POu2gP1lntaRX-t9K9velO2DUEg&libraries=geometry,places"></script>
        <script src="${pageContext.request.contextPath}/resources/js/administracion/monitor.js"></script>
    </body>
</html>
