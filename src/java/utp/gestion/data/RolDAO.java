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
import utp.gestion.common.businessObject.RolInput;
import utp.gestion.common.businessObject.RolMenuQuery;
import utp.gestion.common.entities.Rol;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

public class RolDAO {
    private final IConexion conexion = new Conexion();

    public RolDAO() {
    }
    
    public DataQuery search(DataQueryInput input) throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspRolBuscar(?,?,?,?,?)}";

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
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("nombre", rs01.getString("nombre").trim());
                mapRtn.put("formularios", rs01.getInt("formularios"));
                mapRtn.put("fecha", rs01.getString("fecha"));
                mapRtn.put("idEstado", rs01.getInt("idEstado"));
                mapRtn.put("estado", rs01.getString("estado"));
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(cstmt01.getInt(5)); 
            }
            else{
                dataQuery.setApiEstado(Status.Error);
                dataQuery.setApiMensaje(Mensaje.NoExistenRol);
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
        Rol obj = null;
        RolMenuQuery menu = null;
        List<RolMenuQuery> menus = new ArrayList<>();
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspRolInd(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);  
            cstmt01.setString(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                obj = new Rol();
                obj.setId(rs01.getString("id").trim());
                obj.setCodigo(rs01.getString("codigo").trim());
                obj.setNombre(rs01.getString("nombre"));
                obj.setEstado(rs01.getInt("estado"));
                obj.setApiEstado(Status.Ok);
            }
            
            if(obj == null){
                obj = new Rol();
                obj.setApiEstado(Status.Error);
            }
            else{
                SQLCLL01 = "{CALL uspRolMenuBuscarPorIdRol(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, obj.getId());
                cstmt01.execute();
                
                rs01 = cstmt01.getResultSet();
                
                while (rs01.next()) {
                    menu = new RolMenuQuery();
                    menu.setId(rs01.getString("id").trim());
                    menu.setCodigo(rs01.getString("codigo").trim());
                    menu.setNombre(rs01.getString("nombre"));
                    menu.setDescripcion(rs01.getString("descripcion"));
                    menu.setFechaCreacion(rs01.getString("fechaCreacion"));
                    menu.setIdModulo(rs01.getInt("idModulo"));
                    menu.setModulo(rs01.getString("modulo"));
                    menus.add(menu);
                } 
                obj.setMenus(menus);
            }
            
        }catch(SQLException ex){
            obj = new Rol();
            obj.setApiEstado(Status.Error);
            obj.setApiMensaje(Mensaje.ErrorServidor);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            obj = new Rol();
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
    
    public CheckStatus create(RolInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        String id="";
        String codigo = _repository.getNomenclatura("Rol");
        
        try{
            SQLCLL01 = "{CALL uspRolGuardar(?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, codigo);
            cstmt01.setString(2, input.getNombre());
            cstmt01.setInt(3, input.getEstado());
            cstmt01.setString(4, input.getUsuario());
            cstmt01.setBoolean(5, false);
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
                for (String menu : input.getMenus()) {
                    //crear menus
                    SQLCLL01 = "{CALL uspRolMenuGuardar(?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, menu);
                    cstmt01.setString(2, id);
                    cstmt01.setString(3, input.getUsuario());
                    cstmt01.setBoolean(4, false); 
                    cstmt01.execute();
                }
            }
            
            checkstatus.setId(id);
            checkstatus.setCodigo(codigo);
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.GuardarRol);
            
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
    
    public CheckStatus update(RolInput input) throws SQLException, Exception {
        CheckStatus checkstatus = new CheckStatus();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspRolActualizar(?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getId());
            cstmt01.setString(2, input.getNombre());
            cstmt01.setInt(3, input.getEstado());
            cstmt01.setString(4, input.getUsuario());
           
            cstmt01.execute();
            
            if(!input.getId().equals("")){
                //eliminar menus
                SQLCLL01 = "{CALL uspRolMenuEliminar(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, input.getId());
                cstmt01.execute();           
            
                for (String menu : input.getMenus()) {
                    //crear menus
                    SQLCLL01 = "{CALL uspRolMenuGuardar(?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, menu);
                    cstmt01.setString(2, input.getId());
                    cstmt01.setString(3, input.getUsuario());
                    cstmt01.setBoolean(4, false); 
                    cstmt01.execute();
                }
            }
            cnx.commit();
            
            checkstatus.setId(input.getId());
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.GuardarRol);
            
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
        String SQLCLL01 = "{CALL uspRolEliminar(?)}";
        
        Connection cnx = null; 
        try {
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cnx.setAutoCommit(false);
            cstmt01.setString(1, entity.getId());
            cstmt01.execute();           
            cnx.commit();
            
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.EliminarRol);
            
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
