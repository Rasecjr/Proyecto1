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
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLEngineResult;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.MenuQuery;
import utp.gestion.common.businessObject.UsuarioLoginIndOutput;
import utp.gestion.common.businessObject.UsuarioLoginInput;
import utp.gestion.common.entities.Usuario;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class LoginDAO {
    private final IConexion conexion = new Conexion();
    
    public LoginDAO(){
        
    }
    
    public UsuarioLoginIndOutput login(UsuarioLoginInput input) throws Exception{
        UsuarioLoginIndOutput usu = new UsuarioLoginIndOutput();
        List<MenuQuery> menus = new ArrayList<>();
        MenuQuery menu = new MenuQuery();
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        Connection cnx = null;
        SQLCLL01 = "{CALL uspUsuarioBuscarPorCorreoClave(?,?)}";
        try{ 
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);

            cstmt01.setString(1, input.getUsuario());
            cstmt01.setString(2, input.getContrasenia());  
            cstmt01.execute();
            
            rst = cstmt01.getResultSet();
            
            while (rst.next()) {
                usu = new UsuarioLoginIndOutput();
                usu.setId(rst.getString("id").trim());
                usu.setNombre(rst.getString("nombre").trim());
                usu.setCodigo(rst.getString("codigo").trim());
                usu.setCorreo(rst.getString("correo").trim());
            } 
            
            if(!usu.getId().equals("")){
                SQLCLL01 = "{CALL uspMenuBuscarPorIdUsuario(?)}";
                cstmt01 = cnx.prepareCall(SQLCLL01);
                cstmt01.setString(1, usu.getId());
                cstmt01.execute();
                
                rst = cstmt01.getResultSet();
                
                while (rst.next()) {
                    menu = new MenuQuery();
                    menu.setId(rst.getString("id").trim());
                    menu.setCodigo(rst.getString("codigo").trim());
                    menu.setNombre(rst.getString("nombre"));
                    menu.setUrl(rst.getString("url"));
                    menu.setModulo(rst.getString("modulo"));
                    menu.setIdModulo(rst.getInt("idModulo"));
                    menus.add(menu);
                } 
                usu.setMenus(menus);
                
                usu.setApiEstado(Status.Ok);
            }
            else{
                usu.setApiEstado(Status.Error);
                usu.setApiMensaje(Mensaje.DatosInvalidos);
            } 
        }catch(Exception e){
            usu = new UsuarioLoginIndOutput();
            usu.setApiEstado(Status.Error);
            usu.setApiMensaje(Mensaje.DatosInvalidos);
        } finally {
            if (rst != null) {
                try {
                    rst.close();
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
        
        return usu;
    }
}
