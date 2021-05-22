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
import utp.gestion.common.businessObject.DistritoQuery;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;


public class UbigeoDAO {
    private final IConexion conexion = new Conexion();
    
    public UbigeoDAO(){
        
    }
    
    public List<DistritoQuery> searchDistrito()throws Exception{
        List<DistritoQuery> lstRtn = new ArrayList<DistritoQuery>(0);
        DistritoQuery objRtn;
            
        CallableStatement cstmt01 = null;
        ResultSet rs01 = null;
        String SQLCLL01;
        SQLCLL01 = "{CALL uspDistritoUbigeo()}";

        Connection cnx = null;
        
        try{
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                objRtn = new DistritoQuery();
                objRtn.setIdUbigeo(rs01.getString("IdUbigeo").trim());
                objRtn.setNombre(rs01.getString("nombre").trim());
                
                lstRtn.add(objRtn);   
            }
        }catch(SQLException ex){
            String data = ex.getMessage();
        }catch (Exception e) {
            String data = e.getMessage();
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
        return lstRtn;
    }
}
