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
import utp.gestion.common.businessObject.PrecioBaseInput;
import utp.gestion.common.businessObject.PrecioBaseQueryInput;
import utp.gestion.common.entities.PrecioBase;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class PrecioDAO {
    private final IConexion conexion = new Conexion();
    
    public PrecioDAO(){
    
    }
    
    public DataQuery search(DataQueryInput input) throws Exception{
        DataQuery dataQuery = new DataQuery();
        PrecioBaseQueryInput queryInput = (PrecioBaseQueryInput)input;
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspPrecioBaseBuscar(?,?,?,?,?,?,?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cstmt01.registerOutParameter(7, Types.INTEGER);
            
            cstmt01.setString(1, queryInput.getIdItem());
            cstmt01.setString(2, queryInput.getClase());
            cstmt01.setString(3, queryInput.getTexto());
            cstmt01.setString(4, queryInput.getOrdenamiento());
            cstmt01.setInt(5, queryInput.getPagina());
            cstmt01.setInt(6, queryInput.getTamanio());
            
            cstmt01.execute();
            
            while(rs01 == null){
                cstmt01.getMoreResults();
                rs01 = cstmt01.getResultSet();
            }
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("nombre", rs01.getString("nombre").trim());
                mapRtn.put("precioBase", rs01.getString("precioBase").trim());
                mapRtn.put("vigenciaDesde", rs01.getString("vigenciaDesde").trim());
                mapRtn.put("fecha", rs01.getString("fecha").trim());
                mapRtn.put("clase", rs01.getString("clase").trim());
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(cstmt01.getInt(7)); 
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
    
    public SingleQuery singleById(String id) throws Exception{
        PrecioBase obj = null;
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspPrecioBaseInd(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);  
            cstmt01.setString(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                obj = new PrecioBase();
                obj.setId(rs01.getString("id").trim());
                obj.setIdItem(rs01.getString("idItem").trim());
                obj.setPrecio(rs01.getDouble("precio"));
                obj.setFechaVigencia(rs01.getString("fechaVigencia"));
                obj.setApiEstado(Status.Ok);
            }
            
            if(obj == null){
                obj = new PrecioBase();
                obj.setApiEstado(Status.Error);
            }
            
        }catch(SQLException ex){
            obj = new PrecioBase();
            obj.setApiEstado(Status.Error);
            obj.setApiMensaje(Mensaje.ErrorServidor);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            obj = new PrecioBase();
            obj.setApiEstado(Status.Error);
            obj.setApiMensaje(Mensaje.ErrorServidor);
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
        return obj;
    }
    
    public CheckStatus create(PrecioBaseInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspPrecioGuardar(?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getIdItem());
            cstmt01.setDouble(2, input.getPrecio());
            cstmt01.setString(3, input.getFechaVigencia());
            cstmt01.setString(4, input.getUsuario());
            cstmt01.setBoolean(5, false); 
            cstmt01.execute();
            
            rst = cstmt01.getResultSet();
            
            while(rst == null){
                cstmt01.getMoreResults();
                rst = cstmt01.getResultSet();
            }
            
            while (rst.next()) {
                checkstatus.setId(rst.getString("id"));
                checkstatus.setApiEstado(Status.Ok);
                checkstatus.setApiMensaje(Mensaje.GuardarPrecio);
            }
            
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
    
    public CheckStatus update(PrecioBaseInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspPrecioBaseActualizar(?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getId());
            cstmt01.setString(2, input.getIdItem());
            cstmt01.setDouble(3, input.getPrecio());
            cstmt01.setString(4, input.getFechaVigencia());
            cstmt01.setString(5, input.getUsuario());
            cstmt01.execute();
            
            checkstatus.setId(input.getId());
            checkstatus.setCodigo("");
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
    
    public CheckStatus Delete(BaseInputDelete entity) throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01 = "{CALL uspPrecioBaseEliminar(?)}";
        
        Connection cnx = null; 
        try {
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, entity.getId());
            cstmt01.execute();           
            cnx.commit();
            
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.EliminarPrecio);
            
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
}
