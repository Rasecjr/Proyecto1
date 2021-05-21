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
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.Status;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.Mensaje;
import utp.gestion.common.businessObject.ZonalVentaInput;
import utp.gestion.common.businessObject.ZonalVentaUbicacionInput;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class ZonalVentaDAO {
    private final IConexion conexion = new Conexion();
    
    public ZonalVentaDAO(){
        
    }
    
    public DataQuery searchZonales(DataQueryInput input) throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspZonalVentaBuscar(?,?,?,?,?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cstmt01.registerOutParameter(5, Types.INTEGER);
            
            cstmt01.setString(1, input.getTexto());
            cstmt01.setString(2, input.getOrdenamiento());
            cstmt01.setInt(3, input.getPagina());
            cstmt01.setInt(4, input.getTamanio());
            
            cstmt01.execute();
            
            while(rs01 == null){
                cstmt01.getMoreResults();
                rs01 = cstmt01.getResultSet();
            }
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("idDistrito", rs01.getString("idDistrito").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("nombre", rs01.getString("nombre").trim());
                mapRtn.put("telefono", rs01.getString("telefono").trim());
                mapRtn.put("idEstado", Integer.parseInt(rs01.getString("idEstado").trim()));
                mapRtn.put("estado", rs01.getString("estado").trim());
                mapRtn.put("color", rs01.getString("color").trim());
                mapRtn.put("fecha", rs01.getString("fecha").trim());
                mapRtn.put("pts", rs01.getString("pts").trim());
                mapRtn.put("esPropio", rs01.getBoolean("esPropio"));
                dataList.add(mapRtn);
            } 
            
            dataQuery.setData(dataList);
            dataQuery.setTotal(cstmt01.getInt(5)); 
        }catch(SQLException ex){
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
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
    
    public String CreateZonalVenta(ZonalVentaInput filter) throws SQLException, Exception {
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        String STR_RESULT = "";
        String id="";
        Connection cnx = null;
        String codigo = _repository.getNomenclatura("ZonalVenta");
        try{
            SQLCLL01 = "{CALL uspZonalVentaGuardar(?,?,?,?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);

            cnx.setAutoCommit(false);
            cstmt01.setString(1, filter.getIdDistrito());
            cstmt01.setString(2, codigo);
            cstmt01.setString(3, filter.getNombre());
            cstmt01.setString(4, filter.getTelefono());
            cstmt01.setString(5, filter.getColor());
            cstmt01.setBoolean(6, filter.isEsPropio());
            cstmt01.setInt(7, filter.getEstado());
            cstmt01.setString(8, filter.getUsuario()); //usuario login 
            cstmt01.setBoolean(9, false); 
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
                for (int i = 0; i < filter.getUbicaciones().size(); i++) {
                    //crear puntos
                    ZonalVentaUbicacionInput obj = filter.getUbicaciones().get(i); 
                    SQLCLL01 = "{CALL uspZonalVentaUbicacionesGuardar(?,?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, id);
                    cstmt01.setInt(2, obj.getOrden());
                    cstmt01.setString(3, obj.getLatitud());
                    cstmt01.setString(4, obj.getLongitud());
                    cstmt01.setString(5, filter.getUsuario()); //usuario login
                    cstmt01.execute();
                }
            }
            cnx.commit();
            
            STR_RESULT ="ok";
        } catch (Exception e) {
            e.printStackTrace();
            cnx.rollback();
            System.out.println("SQLException -> Message: " + e.getMessage());
            STR_RESULT = "error";
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        return STR_RESULT;
    }
    
    public String UpdateZonalVenta(ZonalVentaInput filter) throws SQLException, Exception {
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        String STR_RESULT = "";
        String id="";
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspZonalVentaActualizar(?,?,?,?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, filter.getId());
            cstmt01.setString(2, filter.getIdDistrito());
            cstmt01.setString(3, filter.getNombre());
            cstmt01.setString(4, filter.getTelefono());
            cstmt01.setString(5, filter.getColor());
            cstmt01.setBoolean(6, filter.isEsPropio());
            cstmt01.setInt(7, filter.getEstado());
            cstmt01.setString(8, filter.getUsuario()); //usuario login
            cstmt01.setBoolean(9, false);
           
            cstmt01.execute();
            
            if(!filter.getId().equals("")){
                //eliminar puntos
                SQLCLL01 = "{CALL uspZonalVentaUbicacionEliminar(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, filter.getId());
                cstmt01.execute();           
            
                for (int i = 0; i < filter.getUbicaciones().size(); i++) {
                    //crear puntos
                    ZonalVentaUbicacionInput obj = filter.getUbicaciones().get(i); 
                    SQLCLL01 = "{CALL uspZonalVentaUbicacionesGuardar(?,?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, filter.getId());
                    cstmt01.setInt(2, obj.getOrden());
                    cstmt01.setString(3, obj.getLatitud());
                    cstmt01.setString(4, obj.getLongitud());
                    cstmt01.setString(5, filter.getUsuario()); //usuario login
                    cstmt01.execute();
                }
            }
            cnx.commit();
            STR_RESULT ="ok";
        } catch (Exception e) {
            cnx.rollback();
            e.printStackTrace();
            System.out.println("SQLException -> Message: " + e.getMessage());
            STR_RESULT = "error";
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        return STR_RESULT;
    }
    
    public String deleteZonalVenta(String id) throws SQLException, Exception {
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String STR_RESULT = "";
        String SQLCLL01 = "{CALL uspZonalVentaEliminar(?)}";
        
        Connection cnx = null; 
        try {
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cstmt01.setString(1, id);
            cstmt01.execute();           
            
            STR_RESULT ="ok";
         } catch (SQLException ex) {
            String data = ex.getMessage();          
            STR_RESULT = "Error";
        } catch (Exception e) {
            String data = e.getMessage();     
            STR_RESULT = "Error";
         }finally {
            if (rs01 != null) {
                try { rs01.close(); } catch(SQLException e) { System.out.println("SQLException -> Message: " + e.getMessage()); }
            }
            if (cstmt01 != null) {
                try { cstmt01.close(); } catch(SQLException e) { System.out.println("SQLException -> Message: " + e.getMessage()); }
            }
            conexion.closeConnection(cnx);
        }
        
        return STR_RESULT;
    }
    
    public DataQuery searchZonalesCoordenadas() throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
            
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspZonalVentaUbicaciones()}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("nombre", rs01.getString("nombre").trim());
                mapRtn.put("telefono", rs01.getString("telefono").trim());
                mapRtn.put("color", rs01.getString("color").trim());
                mapRtn.put("pts", rs01.getString("pts").trim());
                dataList.add(mapRtn);
            }
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(dataList.size()); 
            }
            else{
                dataQuery.setApiEstado(Status.Error);
                dataQuery.setApiMensaje(Mensaje.NoExistenZonal);
            }
            
        }catch(SQLException ex){
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
        }catch (Exception ex) {
            dataQuery.setApiEstado(Status.Error);
            String data = ex.getMessage();
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
