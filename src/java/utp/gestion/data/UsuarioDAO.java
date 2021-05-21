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
import utp.gestion.common.businessObject.UsuarioInput;
import utp.gestion.common.businessObject.UsuarioRolQuery;
import utp.gestion.common.entities.Usuario;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class UsuarioDAO {
    private final IConexion conexion = new Conexion();

    public UsuarioDAO() {
    }
    
    public DataQuery search(DataQueryInput input) throws Exception{
        DataQuery dataQuery = new DataQuery();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> mapRtn = new LinkedHashMap<>(); 
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspUsuarioBuscar(?,?,?,?,?)}";

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
                mapRtn.put("correo", rs01.getString("correo"));
                mapRtn.put("roles", rs01.getString("roles"));
                mapRtn.put("fecha", rs01.getString("fecha"));
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
        Usuario obj = null;
        UsuarioRolQuery rol = null;
        List<UsuarioRolQuery> roles = new ArrayList<>();
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspUsuarioInd(?)}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);  
            cstmt01.setString(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                obj = new Usuario();
                obj.setId(rs01.getString("id").trim());
                obj.setCodigo(rs01.getString("codigo").trim());
                obj.setContenido(rs01.getString("contenido"));
                obj.setIdPersona(rs01.getString("idPersona"));
                obj.setNombre(rs01.getString("nombre"));
                obj.setTipoDocumento(rs01.getInt("tipoDocumento"));
                obj.setNumeroDocumento(rs01.getString("numeroDocumento"));
                obj.setTelefono(rs01.getString("telefono"));
                obj.setEstado(rs01.getInt("estado"));
                obj.setApiEstado(Status.Ok);
            }
            
            if(obj == null){
                obj = new Usuario();
                obj.setApiEstado(Status.Error);
            }
            else{
                SQLCLL01 = "{CALL uspUsuarioRolBuscarPorIdUsuario(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, obj.getId());
                cstmt01.execute();
                
                rs01 = cstmt01.getResultSet();
                
                while (rs01.next()) {
                    rol = new UsuarioRolQuery();
                    rol.setId(rs01.getString("id").trim());
                    rol.setCodigo(rs01.getString("codigo").trim());
                    rol.setNombre(rs01.getString("nombre"));
                    roles.add(rol);
                } 
                obj.setRoles(roles);
            }
            
        }catch(SQLException ex){
            obj = new Usuario();
            obj.setApiEstado(Status.Error);
            obj.setApiMensaje(Mensaje.ErrorServidor);
            String data = ex.getMessage();
            System.out.println("SQLException -> Message: " + data);
        }catch (Exception e) {
            obj = new Usuario();
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
    
    public CheckStatus create(UsuarioInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        String id="";        
        String codigo = "";
        try{
            SQLCLL01 = "{CALL uspPersonaGuardar(?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getNombre());
            cstmt01.setInt(2, input.getTipoDocumento());
            cstmt01.setString(3, input.getNumeroDocumento());
            cstmt01.setString(4, input.getTelefono());
            cstmt01.setString(5, input.getUsuario());
            cstmt01.setBoolean(6, false);
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
                codigo = _repository.getNomenclatura("Usuario");
                
                SQLCLL01 = "{CALL uspUsuarioGuardar(?,?,?,?,?,?,?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, codigo);
                cstmt01.setString(2, input.getContrasenia());
                cstmt01.setString(3, input.getCorreo());
                cstmt01.setString(4, id);
                cstmt01.setInt(5, input.getEstado());
                cstmt01.setString(6, input.getUsuario());
                cstmt01.setBoolean(7, false); 
                cstmt01.execute();
                
                rst = cstmt01.getResultSet();
            
                while(rst == null){
                    cstmt01.getMoreResults();
                    rst = cstmt01.getResultSet();
                }

                while (rst.next()) {
                    id= rst.getString("id");
                }
                
                checkstatus.setId(id);
                checkstatus.setCodigo(codigo);
                checkstatus.setApiEstado(Status.Ok);
                checkstatus.setApiMensaje(Mensaje.GuardarUsuario);
                
                if (input.getRoles().length != 0){
                    for (String rol : input.getRoles()) {
                        //crear rol
                        SQLCLL01 = "{CALL uspUsuarioRolGuardar(?,?,?,?)}";
                        cstmt01 = cnx.prepareCall(SQLCLL01);
                        cstmt01.setString(1, id);
                        cstmt01.setString(2, rol);
                        cstmt01.setString(3, input.getUsuario());
                        cstmt01.setBoolean(4, false); 
                        cstmt01.execute();
                    }
                }
                cnx.commit();
            }
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
    
    public CheckStatus update(UsuarioInput input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        RepositoryDAO _repository = new RepositoryDAO();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        String id="";        
        String codigo = "";
        try{
            SQLCLL01 = "{CALL uspUsuarioActualizar(?,?,?,?,?,?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            
            cnx.setAutoCommit(false);
            cstmt01.setString(1, input.getId());
            cstmt01.setString(2, input.getNombre());
            cstmt01.setInt(3, input.getTipoDocumento());
            cstmt01.setString(4, input.getNumeroDocumento());
            cstmt01.setString(5, input.getTelefono());
            cstmt01.setString(6, input.getContrasenia());
            cstmt01.setString(7, input.getCorreo());
            cstmt01.setBoolean(8, input.isCambiarContrasenia());
            cstmt01.setInt(9, input.getEstado());
            cstmt01.setString(10, input.getUsuario());
            cstmt01.execute();
            
            if(!input.getId().equals("")){
                //eliminar roles
                SQLCLL01 = "{CALL uspUsuarioRolEliminar(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, input.getId());
                cstmt01.execute();           
            
                for (String rol : input.getRoles()) {
                    //crear rol
                    SQLCLL01 = "{CALL uspUsuarioRolGuardar(?,?,?,?)}";
                    cstmt01 = cnx.prepareCall(SQLCLL01);
                    cstmt01.setString(1, input.getId());
                    cstmt01.setString(2, rol);
                    cstmt01.setString(3, input.getUsuario());
                    cstmt01.setBoolean(4, false); 
                    cstmt01.execute();
                }
            }
            
            cnx.commit();
            
            checkstatus.setId(input.getId());
            checkstatus.setApiEstado(Status.Ok);
            checkstatus.setApiMensaje(Mensaje.GuardarUsuario);

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
        String SQLCLL01 = "{CALL uspUsuarioEliminar(?)}";
        
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
