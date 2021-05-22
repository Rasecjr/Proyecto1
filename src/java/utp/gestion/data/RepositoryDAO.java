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
import utp.gestion.classes.CheckStatus;
import utp.gestion.classes.Status;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;


public class RepositoryDAO {
    private final IConexion conexion = new Conexion();
    
    public RepositoryDAO(){
        
    }
    
    public String getNomenclatura(String entidad) throws SQLException, Exception {
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        String codigo="";
        Connection cnx = null;
        try{
            SQLCLL01 = "{CALL uspNomenclaturaGenerar(?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);

            cstmt01.setString(1, entidad);
            cstmt01.execute();
            
            rst = cstmt01.getResultSet();
            
            while(rst == null){
                cstmt01.getMoreResults();
                rst = cstmt01.getResultSet();
            }
            
            while (rst.next()) {
                codigo= rst.getString("codigo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQLException -> Message: " + e.getMessage());
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        return codigo;
    }
    
    public CheckStatus Validar(String consulta, String tabla,String innerjoin, String where) throws Exception{
        CheckStatus checkStatus = new CheckStatus();
        int total = 0;
        CallableStatement cstmt01 = null;
        ResultSet rst = null;
        String SQLCLL01;
        String data=null;
        Connection cnx = null;
        
        try{
            SQLCLL01 = "{CALL uspValidadorDataExistencia(?,?,?,?,?)}";
            cnx = conexion.geConnection(); 
            cstmt01 = cnx.prepareCall(SQLCLL01);

            cstmt01.registerOutParameter(5, Types.INTEGER);
            
            cstmt01.setString(1, consulta);
            cstmt01.setString(2, tabla);
            cstmt01.setString(3, innerjoin);
            cstmt01.setString(4, where);
            cstmt01.execute();
            
            rst = cstmt01.getResultSet();
            
            while (rst.next()) {
                data = rst.getString("id");
            }
            
            total = cstmt01.getInt(5);
            
            if (data != null)
            {
                checkStatus.setApiEstado(Status.Error);
                checkStatus.setId(data);
            }
            else if (data == null)
            {
                checkStatus.setApiEstado(Status.Ok);
                checkStatus.setId("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQLException -> Message: " + e.getMessage());
        } finally {
            SQLCLL01 = null;
            conexion.closeConnection(cnx);
        }
        
        return checkStatus;
    }
}
