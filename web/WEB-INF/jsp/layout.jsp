<%-- 
    Document   : layout
    Created on : 22/11/2020, 01:52:03 AM
    Author     : José
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="utp.gestion.common.businessObject.UsuarioLoginIndOutput"%>
<%@page import="utp.gestion.common.businessObject.MenuQuery"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String usuarioWeb = "";
    List<MenuQuery> menus = new ArrayList<>();
    HttpSession _sesion = (HttpSession) request.getSession();
    Object _usuario = _sesion.getAttribute("usuario");
    if(_usuario !=null){
        UsuarioLoginIndOutput _o_usuario = (UsuarioLoginIndOutput)_usuario;
        usuarioWeb = _o_usuario.getNombre();
        menus = _o_usuario.getMenus();
    }
    else{
        response.sendRedirect("index");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <!--link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" /-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css">
        <link href="${pageContext.request.contextPath}/resources/css/material-icons.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/resources/css/master.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/resources/css/site.css" rel="stylesheet" type="text/css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestion de Pedidos Delivery</title>
    </head>
    <body class="" style="margin-top:0;padding:0">
        <!-- Navbar -->    
        <div class="card fadeIn animated" style="margin:0;padding:0">
            <nav class="navbar navbar-expand-lg bg-dark navbar-dark card-header-primary" style="margin:0;padding-top:0 0 15px 0">
                 <div class="navbar-header">
                    <img id="#milogo" src="https://www.flaticon.es/svg/static/icons/svg/854/854878.svg" style="width:100px;height:50px;" />
                    <button type="button" class="navbar-toggler fadeIn animated" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="navbar-toggler-icon"></span>
                    </button>
                </div>
                
                <div class="collapse navbar-collapse">
                  <ul class="navbar-nav mr-auto nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="material-icons">house</i>
                            Inicio
                        </a>
                    </li>
                    <%
                        int count1 = 0;
                        for(MenuQuery menu : menus)
                        {
                            if (menu != null && menu.getModulo().equals("Pedidos")) { count1++; }
                        }
                        if (count1 != 0)
                        {
                    %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                            <i class="material-icons">dashboard</i>
                            Pedidos
                        </a>
                        
                        <ul id="divpedido" class="dropdown-menu fadeIn animated" aria-labelledby="navbarMenulink">
                            <li class=" ">
                                <% for(MenuQuery menu : menus){
                                    if(menu.getIdModulo() == 2){
                                        
                                %>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}<%= menu.getUrl() %>"><%= menu.getNombre() %></a>
                                <%
                                    }
                                }
                                %>
                                <!--a class="dropdown-item" href="${pageContext.request.contextPath}/monitor">Monitor</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/createPedido">Registrar</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/pedido">Historial</a-->
                            </li>
                        </ul>
                    </li>
                    <%
                        }
                        int count2 = 0;
                        for(MenuQuery menu : menus)
                        {
                            if (menu != null && menu.getModulo().equals("Administración")) { count2++; }
                        }
                        if (count2 != 0)
                        {
                    %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="material-icons">how_to_reg</i>
                            Administración
                        </a>
                        <ul id="divpedido" class="dropdown-menu" aria-labelledby="navbarMenulink">
                            <li>
                                <% for(MenuQuery menu : menus){
                                    if(menu.getIdModulo() == 3){
                                %>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}<%= menu.getUrl() %>"><%= menu.getNombre() %></a>
                                <%
                                    }
                                }
                                %>
                                <!--a class="dropdown-item" href="${pageContext.request.contextPath}/item">Producto</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/precio">Precio Vigencia</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/zonalVenta">Zonal de Venta</a-->
                            </li>
                        </ul>
                    </li>
                    <%
                        }
                        int count3 = 0;
                        for(MenuQuery menu : menus)
                        {
                            if (menu != null && menu.getModulo().equals("Seguridad")) { count3++; }
                        }
                        if (count3 != 0)
                        {
                    %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                          <i class="material-icons">lock</i>
                          SEGURIDAD
                        </a>
                        <ul id="divpedido" class="dropdown-menu" aria-labelledby="navbarMenulink">
                            <li>
                                <% for(MenuQuery menu : menus){
                                    if(menu.getIdModulo() == 1){
                                %>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}<%= menu.getUrl() %>"><%= menu.getNombre() %></a>
                                <%
                                    }
                                }
                                %>
                                <!--a class="dropdown-item" href="${pageContext.request.contextPath}/rol">Roles</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/usuario">Usuarios</a-->
                            </li>
                        </ul>
                    </li>
                    <% }
                    %>
                  </ul>                   
                </div>  
                <div>
                    <ul class="navbar-nav">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" aria-haspopup="true" aria-expanded="true">
                                <i class="material-icons">account_box</i>
                                <%=usuarioWeb%>
                            </a>
                            <ul id="divpedido" class="dropdown-menu fadeIn animated" aria-labelledby="navbarMenulink">
                                <li><a href="${pageContext.request.contextPath}/index/logout" class="dropdown-item">Cerrar Sesión</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
        <!-- End Navbar -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/jquery.validate.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/sweetalert2.all.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/_helper.js" type="text/javascript"></script>
    </body>
</html>
