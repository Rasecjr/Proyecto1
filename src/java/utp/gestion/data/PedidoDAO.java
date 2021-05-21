/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import utp.gestion.classes.BaseInputDelete;
import utp.gestion.classes.CheckStatus;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.SingleQuery;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.PedidoInput;
import utp.gestion.common.businessObject.PedidoItemInput;
import utp.gestion.common.businessObject.PedidoQueryInput;
import utp.gestion.common.businessObject.TarifaIndOutput;
import utp.gestion.common.entities.Pedido;
import utp.gestion.common.entities.PedidoItem;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class PedidoDAO {
    private final IConexion conexion = new Conexion();
    
    public PedidoDAO(){
    
    }
    
    public DataQuery search(DataQueryInput input) throws Exception{
        DataQuery dataQuery = new DataQuery();
        PedidoQueryInput queryInput = (PedidoQueryInput)input;
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspPedidoBuscar(?,?,?,?,?,?,?,?,?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cstmt01.registerOutParameter(9, Types.INTEGER);
            
            cstmt01.setInt(1, queryInput.getEstado());
            cstmt01.setString(2, queryInput.getFechaInicio());
            cstmt01.setString(3, queryInput.getFechaFin());
            cstmt01.setString(4, queryInput.getDistrito());
            cstmt01.setString(5, queryInput.getZonal());
            cstmt01.setString(6, queryInput.getOrdenamiento());
            cstmt01.setInt(7, queryInput.getPagina());
            cstmt01.setInt(8, queryInput.getTamanio());
            
            cstmt01.execute();
            
            while(rs01 == null){
                cstmt01.getMoreResults();
                rs01 = cstmt01.getResultSet();
            }
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("fecha", rs01.getString("fecha").trim());
                mapRtn.put("distrito", rs01.getString("distrito").trim());
                mapRtn.put("zonal", rs01.getString("zonal").trim());
                mapRtn.put("cantidad", rs01.getInt("cantidad"));
                mapRtn.put("nombreEstado", rs01.getString("nombreEstado").trim());
                mapRtn.put("fechaPedido", rs01.getString("fechaPedido"));
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(cstmt01.getInt(9)); 
            }
            else{
                dataQuery.setApiEstado(Status.Error);
                dataQuery.setApiMensaje(Mensaje.NoExistenPedido);
            }
            
        }catch(SQLException ex){
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            dataQuery.setApiEstado(Status.Error);
            String data = e.getMessage();
            System.out.println("SQLException -> Message: " + data);
        } finally {
            if (rs01 != null) {
                try {
                    rs01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            if (cstmt01 != null) {
                try {
                    cstmt01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            conexion.closeConnection(cnx);
        }
        return dataQuery;
    }
    
    public DataQuery searchItems() throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspItemPorPrecioBase}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("nombre", rs01.getString("nombre").trim());
                mapRtn.put("clase", rs01.getString("clase").trim());
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(dataList.size()); 
            }
            else{
                dataQuery.setApiEstado(Status.Error);
                dataQuery.setApiMensaje(Mensaje.NoExistenPrecio);
            }
            
        }catch(SQLException ex){
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            dataQuery.setApiEstado(Status.Error);
            String data = e.getMessage();
            System.out.println("SQLException -> Message: " + data);
        } finally {
            if (rs01 != null) {
                try {
                    rs01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            if (cstmt01 != null) {
                try {
                    cstmt01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            conexion.closeConnection(cnx);
        }
        return dataQuery;
    }
    
    public SingleQuery singleTarifaById(String id) throws Exception{
        TarifaIndOutput tarifa = null;
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspTarifaProductoPorId(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);  
            cstmt01.setString(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                tarifa = new TarifaIndOutput();
                tarifa.setTarifa(Double.parseDouble(rs01.getString("tarifa")));
                tarifa.setApiEstado(Status.Ok);
            }
            
            if(tarifa == null){
                tarifa = new TarifaIndOutput();
                tarifa.setApiEstado(Status.Error);
            }
            
        }catch(SQLException ex){
            tarifa = new TarifaIndOutput();
            tarifa.setApiEstado(Status.Error);
            tarifa.setApiMensaje(Mensaje.ErrorServidor);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            tarifa = new TarifaIndOutput();
            tarifa.setApiEstado(Status.Error);
            tarifa.setApiMensaje(Mensaje.ErrorServidor);
            String data = e.getMessage();
            System.out.println("SQLException -> Message: " + data);
        } finally {
            if (rs01 != null) {
                try {
                    rs01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            if (cstmt01 != null) {
                try {
                    cstmt01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            conexion.closeConnection(cnx);
        }
        return tarifa;
    }
    
    public SingleQuery singleById(String id) throws Exception{
        Pedido pedido = null;
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        List<PedidoItem> lista = new ArrayList<>();
        SQLCLL01 = "{CALL uspPedidoInd(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);  
            cstmt01.setString(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                pedido = new Pedido();
                pedido.setId(rs01.getString("id").trim());
                pedido.setCodigo(rs01.getString("codigo").trim());
                pedido.setFechaPedido(rs01.getString("fechaPedido"));
                pedido.setTipoComprobante(rs01.getInt("tipoComprobante"));
                pedido.setTipoPago(rs01.getInt("tipoPago"));
                pedido.setSubTotal(rs01.getDouble("subTotal"));
                pedido.setSubTotalEnvase(rs01.getDouble("subTotalEnvase"));
                pedido.setTotal(rs01.getDouble("total"));
                pedido.setDireccion(rs01.getString("direccion"));
                pedido.setReferencia(rs01.getString("referencia"));
                pedido.setLatitud(rs01.getString("latitud"));
                pedido.setLongitud(rs01.getString("longitud"));
                pedido.setObservacion(rs01.getString("observacion"));
                pedido.setIdUbigeo(rs01.getString("idUbigeo"));
                pedido.setIdZonalVenta(rs01.getString("idZonalVenta"));
                pedido.setNombre(rs01.getString("nombre"));
                pedido.setCorreo(rs01.getString("correo"));
                pedido.setTipoDocumento(rs01.getString("tipoDocumento"));     
                pedido.setNumeroDocumento(rs01.getString("numeroDocumento"));     
                pedido.setTelefono(rs01.getString("telefono"));     
                pedido.setMovil(rs01.getString("movil"));     
                pedido.setNombreCP(rs01.getString("nombreCP"));
                pedido.setTipoDocumentoCP(rs01.getString("tipoDocumentoCP"));     
                pedido.setNumeroDocumentoCP(rs01.getString("numeroDocumentoCP"));     
                pedido.setDireccionCP(rs01.getString("direccionCP"));
                pedido.setEstado(rs01.getInt("estado"));
                pedido.setApiEstado(Status.Ok);
            }
            
            if(pedido == null){
                pedido = new Pedido();
                pedido.setApiEstado(Status.Error);
            }
            else{
                SQLCLL01 = "{CALL uspPedidoItemBuscar(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, id);
                cstmt01.execute();
                
                rs01 = cstmt01.getResultSet();
                
                while (rs01.next()) {
                    PedidoItem pedidoItem = new PedidoItem();
                    pedidoItem.setId(rs01.getString("id"));
                    pedidoItem.setIdItem(rs01.getString("idItem"));
                    pedidoItem.setCantidad(rs01.getInt("cantidad"));
                    pedidoItem.setPrecioVenta(rs01.getDouble("precioVenta"));
                    lista.add(pedidoItem);
                }
                pedido.setItems(lista);
            }
            
        }catch(SQLException ex){
            pedido = new Pedido();
            pedido.setApiEstado(Status.Error);
            pedido.setApiMensaje(Mensaje.ErrorServidor);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            pedido = new Pedido();
            pedido.setApiEstado(Status.Error);
            pedido.setApiMensaje(Mensaje.ErrorServidor);
            String data = e.getMessage();
            System.out.println("SQLException -> Message: " + data);
        } finally {
            if (rs01 != null) {
                try {
                    rs01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            if (cstmt01 != null) {
                try {
                    cstmt01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            conexion.closeConnection(cnx);
        }
        return pedido;
    }
    
    public CheckStatus create(PedidoInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        String id="";
        String codigo = _repository.getNomenclatura("Pedido");
        
        try{
            SQLCLL01 = "{CALL uspPedidoGuardar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, codigo);
            cstmt01.setString(2, input.getFechaPedido());
            cstmt01.setInt(3, input.getTipoComprobante());
            cstmt01.setInt(4, input.getTipoPago());
            cstmt01.setDouble(5, input.getSubTotal());
            cstmt01.setDouble(6, input.getSubTotalEnvase());
            cstmt01.setDouble(7, input.getTotal());
            cstmt01.setString(8, input.getDireccion());
            cstmt01.setString(9, input.getReferencia());
            cstmt01.setString(10, input.getLatitud());
            cstmt01.setString(11, input.getLongitud());
            cstmt01.setString(12, input.getObservacion());
            cstmt01.setString(13, input.getIdUbigeo());
            cstmt01.setString(14, input.getIdZonalVenta());
            cstmt01.setString(15, input.getNombre());
            cstmt01.setString(16, input.getCorreo());
            cstmt01.setInt(17, input.getTipoDocumento());
            cstmt01.setString(18, input.getNumeroDocumento());
            cstmt01.setString(19, input.getTelefono());
            cstmt01.setString(20, input.getMovil());
            cstmt01.setString(21, input.getNombreCP());
            cstmt01.setInt(22, input.getTipoDocumentoCP());
            cstmt01.setString(23, input.getNumeroDocumentoCP());
            cstmt01.setString(24, input.getDireccionCP());
            cstmt01.setInt(25, input.getEstado());
            cstmt01.setString(26, input.getUsuario());
            cstmt01.setBoolean(27, false); 
            cstmt01.execute();
            
            rst = cstmt01.getResultSet();
            
            while(rst == null){
                cstmt01.getMoreResults();
                rst = cstmt01.getResultSet();
            }
            
            while (rst.next()) {
                id= rst.getString("id");
            }
            
            if(!id.equals("")){
                for (int i = 0; i < input.getItems().size(); i++) {
                    //crear puntos
                    PedidoItemInput obj = input.getItems().get(i); 
                    SQLCLL01 = "{CALL uspPedidoItemGuardar(?,?,?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, id);
                    cstmt01.setString(2, obj.getId());
                    cstmt01.setInt(3, obj.getCantidad());
                    cstmt01.setDouble(4, obj.getPrecioVenta());
                    cstmt01.setString(5, input.getUsuario());
                    cstmt01.setBoolean(6, false); 
                    cstmt01.execute();
                }
            }
            
            checkstatus.setId(id);
            checkstatus.setCodigo(codigo);
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.GuardarPrecio);
            
            cnx.commit();
        
        } catch (Exception e) {
            checkstatus.setApiEstado(Status.Error);
            checkstatus.setApiMensaje(Mensaje.ErrorServidor);
            e.printStackTrace();
            cnx.rollback();
            System.out.println("SQLException -> Message: " + e.getMessage());
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        
        return checkstatus; 
    }
    
    public CheckStatus update(PedidoInput input) throws SQLException, Exception {
        CheckStatus checkstatus = new CheckStatus();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        String id="";
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspPedidoActualizar(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getId());
            cstmt01.setString(2, input.getFechaPedido());
            cstmt01.setInt(3, input.getTipoComprobante());
            cstmt01.setInt(4, input.getTipoPago());
            cstmt01.setDouble(5, input.getSubTotal());
            cstmt01.setDouble(6, input.getSubTotalEnvase());
            cstmt01.setDouble(7, input.getTotal());
            cstmt01.setString(8, input.getDireccion());
            cstmt01.setString(9, input.getReferencia());
            cstmt01.setString(10, input.getLatitud());
            cstmt01.setString(11, input.getLongitud());
            cstmt01.setString(12, input.getObservacion());
            cstmt01.setString(13, input.getIdUbigeo());
            cstmt01.setString(14, input.getIdZonalVenta());
            cstmt01.setString(15, input.getNombre());
            cstmt01.setString(16, input.getCorreo());
            cstmt01.setInt(17, input.getTipoDocumento());
            cstmt01.setString(18, input.getNumeroDocumento());
            cstmt01.setString(19, input.getTelefono());
            cstmt01.setString(20, input.getMovil());
            cstmt01.setString(21, input.getNombreCP());
            cstmt01.setInt(22, input.getTipoDocumentoCP());
            cstmt01.setString(23, input.getNumeroDocumentoCP());
            cstmt01.setString(24, input.getDireccionCP());
            cstmt01.setInt(25, input.getEstado());
            cstmt01.setString(26, input.getUsuario());
           
            cstmt01.execute();
            
            if(!input.getId().equals("")){
                //eliminar items
                SQLCLL01 = "{CALL uspPedidoItemEliminar(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, input.getId());
                cstmt01.execute();           
            
                for (int i = 0; i < input.getItems().size(); i++) {
                    //crear items
                    PedidoItemInput obj = input.getItems().get(i); 
                    SQLCLL01 = "{CALL uspPedidoItemGuardar(?,?,?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, input.getId());
                    cstmt01.setString(2, obj.getId());
                    cstmt01.setInt(3, obj.getCantidad());
                    cstmt01.setDouble(4, obj.getPrecioVenta());
                    cstmt01.setString(5, input.getUsuario());
                    cstmt01.setBoolean(6, false); 
                    cstmt01.execute();
                }
            }
            cnx.commit();
            
            checkstatus.setId(input.getId());
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.GuardarPrecio);
            
        } catch (Exception e) {
            checkstatus.setApiEstado(Status.Error);
            checkstatus.setApiMensaje(Mensaje.ErrorServidor);
            cnx.rollback();
            e.printStackTrace();
            System.out.println("SQLException -> Message: " + e.getMessage());
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        return checkstatus;
    }
     
    public CheckStatus Delete(BaseInputDelete entity) throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01 = "{CALL uspPedidoEliminar(?)}";
        
        Connection cnx = null; 
        try {
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, entity.getId());
            cstmt01.execute();           
            cnx.commit();
            
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.EliminarPedido);
            
         } catch (SQLException ex) {
            String data = ex.getMessage();          
            cnx.rollback();
            checkstatus.setApiEstado(Status.Error);
            checkstatus.setApiMensaje(Mensaje.ErrorServidor);
        } catch (Exception e) {
            String data = e.getMessage();     
            cnx.rollback();
            checkstatus.setApiEstado(Status.Error);
            checkstatus.setApiMensaje(Mensaje.ErrorServidor);
         }finally {
            if (rs01 != null) {
                try { rs01.close(); } catch(SQLException e) { System.out.println("SQLException -> Message: " + e.getMessage()); }
            }
            if (cstmt01 != null) {
                try { cstmt01.close(); } catch(SQLException e) { System.out.println("SQLException -> Message: " + e.getMessage()); }
            }
            conexion.closeConnection(cnx);
        }
        
        return checkstatus;
    }
    
    public DataQuery search(int estado) throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspPedidosMonitor(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cstmt01.setInt(1, estado);
            cstmt01.execute();
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("fecha", rs01.getString("fecha").trim());
                mapRtn.put("subTotal", rs01.getString("subTotal").trim());
                mapRtn.put("subTotalEnvase", rs01.getString("subTotalEnvase").trim());
                mapRtn.put("total", rs01.getInt("total"));
                mapRtn.put("direccion", rs01.getString("direccion").trim());
                mapRtn.put("latitud", rs01.getString("latitud"));
                mapRtn.put("longitud", rs01.getString("longitud"));
                mapRtn.put("estado", rs01.getString("estado"));
                mapRtn.put("idZonalVenta", rs01.getString("idZonalVenta"));
                mapRtn.put("color", rs01.getString("color"));
                mapRtn.put("telefono", rs01.getString("telefono"));
                mapRtn.put("items", rs01.getString("items"));
                
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(cstmt01.getInt(9)); 
            }
            else{
                dataQuery.setApiEstado(Status.Error);
                dataQuery.setApiMensaje(Mensaje.NoExistenPedido);
            }
            
        }catch(SQLException ex){
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            dataQuery.setApiEstado(Status.Error);
            String data = e.getMessage();
            System.out.println("SQLException -> Message: " + data);
        } finally {
            if (rs01 != null) {
                try {
                    rs01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            if (cstmt01 != null) {
                try {
                    cstmt01.close();
                } catch (SQLException e) {
                    System.out.println("SQLException -> Message: " + e.getMessage());
                }
            }
            conexion.closeConnection(cnx);
        }
        return dataQuery;
    }
}
