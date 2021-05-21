/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.classes;

/**
 *
 * @author José
 */
public class Mensaje {
    public static String DatosInvalidos = "Los datos ingresados son inv\u00e1lidos.";
    public static String ErrorServidor="Comun\u00edquese con Soporte T\u00e9cnico";
    
    //Item
    public static String GuardarItem="Se guard\u00f3 Producto satisfactoriamente.";
    public static String NoExistenItem ="No existen Productos.";
    public static String NoExisteItem ="No existe Producto.";
    public static String EliminarItem = "Se elimin\u00f3 Producto satisfactoriamente.";
    public static String ItemTipo = "Has omitido seleccionar un Tipo de producto.";
    public static String ItemClase = "Has omitido seleccionar una Clase de producto.";
    public static String ItemNombre = "Has omitido ingresar un Nombre de producto.";
    public static String ItemNombreMax = "El Nombre de Producto que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
    public static String ItemNombreDuplicado = "El Nombre que intentas ingresar ya est\u00e1 en uso.";
    public static String ItemNombreComercial = "Has omitido ingresar un Nombre Comercial de producto.";
    public static String ItemNombreComercialMax = "El Nombre de Producto que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
    public static String ItemNombreComercialDuplicado = "El Nombre Comercial que intentas ingresar ya est\u00e1 en uso.";
    public static String ItemDescripcion = "Has omitido ingresar una Descripci\u00f3n para el producto.";
    public static String ItemDescripcionMax = "La Descripci\u00f3n que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
    public static String ItemEstado = "Has omitido seleccionar un Estado v\u00e1lido.";
    
    //Precio
    public static String GuardarPrecio="Se guard\u00f3 Precio satisfactoriamente.";
    public static String NoExistenPrecio ="No existen Precios.";
    public static String NoExistePrecio ="No existe Precio.";
    public static String EliminarPrecio = "Se elimin\u00f3 Precio satisfactoriamente.";
    public static String PrecioBaseIdItem = "Has omitido seleccionar un Producto.";
    public static String PrecioBaseIdItemVacio = "Has omitido seleccionar un Producto.";
    public static String PrecioBasePrecio = "Has omitido ingresar un Precio Base para el producto.";
    public static String PrecioBaseFechaVigencia = "Has omitido elegir una Fecha v\u00e1lida.";
    public static String PrecioBaseFechaVigenciaNoMenor = "La fecha que intentas elegir es anterior al d\u00eda de hoy. Selecciona una fecha mayor o igual a la del d\u00eda actual.";
    public static String PrecioBaseRepetido = "Ya se cre\u00f3 un Precio Base con la misma fecha de vigencia para este producto Por favor elige una fecha distinta.";

    //Pedido
    public static String GuardarPedido="Se guard\u00f3 Pedido satisfactoriamente.";
    public static String NoExistenPedido ="No existen Pedidos.";
    public static String NoExistePedido ="No existe Pedido.";
    public static String EliminarPedido = "Se elimin\u00f3 Pedido satisfactoriamente.";
    public static String PedidoNombre = "Has omitido ingresar un Nombre del cliente.";
    public static String PedidoTipoDocumento = "Seleccione un tipo de documento v\u00e1lido.";
    public static String PedidoNumeroDocumentoDNI = "Debe ingresar un dni v\u00e1lido.";
    public static String PedidoNumeroDocumentoRUC = "Debe ingresar un ruc v\u00e1lido.";
    public static String PedidoDireccion = "Debe ingresar una direccion.";
    public static String PedidoItemCero = "Seleccione un item v\u00e1lido.";
    public static String PedidoItemDuplicado = "Items duplicados.";
    public static String PedidoItemPrecio = "El precio de los Items seleccionados ha cambiado.";
    public static String PedidoLatitud = "Debe ingresar una latitud de direccion.";
    public static String PedidoLongitud = "Debe ingresar una longitud de direccion.";
    public static String PedidoReferencia = "Debe ingresar una referencia.";
    public static String PedidoDistrito = "Debe ingresar un distrito.";
    public static String PedidoTipoPago = "Seleccione un medio de pago v\u00e1lido.";
    public static String PedidoTipoComprobante = "Seleccion un tipo de comprobante v\u00e1lido.";
    public static String PedidoClienteCP = "Debe ingresar un cliente para el comprobante para el comprobante.";
    public static String PedidoTipoDocumentoCP = "Seleccione un tipo de documento v\u00e1lido para el comprobante.";
    public static String PedidoNumeroDocumentoDNICP = "Debe ingresar un dni v\u00e1lido para el comprobante.";
    public static String PedidoNumeroDocumentoRUCCP = "Debe ingresar un ruc v\u00e1lido para el comprobante.";
    public static String PedidoDireccionCP = "Debe ingresar una direccion para el comprobante.";
    
    //Zonal
    public static String NoExistenZonal ="No existen Zonales.";
    public static String CoberturaInValida ="Dirección fuera de rango de cobertura.";
    
    //Menu
    public static String NoExistenMenu ="No existen Menus.";
    
    //Rol
    public static String NoExistenRol ="No existen Roles.";
    public static String GuardarRol = "Se guard\u00f3 Rol satisfactoriamente.";
    public static String EliminarRol = "Se elimin\u00f3 Rol satisfactoriamente.";
    public static String RolNombreVacio = "Has omitido ingresar un Nombre de Rol.";
    public static String RolNombreMax = "El Nombre que est\u00e1s intentando ingresar tiene m\u00e1s de 100 caracteres.";
    public static String RolNombreDuplicado = "El nombre que intentas ingresar ya est\u00e1 en uso.";
    public static String RolEstado = "Has omitido seleccionar un Estado.";
    public static String RolMenus = "Has omitido seleccionar al menos una opci\u00f3n del men\u00fa.";
    public static String RolMenuCero = "Has omitido asignar formularios al Rol actual.";
    
    // usuario
    public static String GuardarUsuario = "Se guard\u00f3 Usuario satisfactoriamente.";
    public static String UsuarioRolCero = "Has omitido asignar formularios al Rol actual.";
    public static String UsuarioRolDuplicado = "El Nombre de Rol que intentas ingresar ya est\u00e1 en uso.";
    public static String CorreoDuplicado = "El Correo de Usuario que intentas ingresar ya est\u00e1 en uso.";
    public static String UsuarioNombreDuplicado = "El Nombre de Usuario que intentas ingresar ya est\u00e1 en uso.";
    public static String UsuarioDuplicado = "Este usuario ya existe.";
}
