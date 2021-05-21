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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.Status;
import utp.gestion.provider.Conexion;
import utp.gestion.provider.IConexion;

/**
 *
 * @author José
 */
public class MonitorDAO {
    private final IConexion conexion = new Conexion();

    public MonitorDAO() {
    }
    
    public DataQuery search(int id) throws Exception{
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
            
            cstmt01.setInt(1, id);
            cstmt01.execute();
            
            rs01 = cstmt01.getResultSet();
            
            while (rs01.next()) {
                mapRtn = new LinkedHashMap<>();
                mapRtn.put("id", rs01.getString("id").trim());
                mapRtn.put("codigo", rs01.getString("codigo").trim());
                mapRtn.put("fecha", rs01.getString("fecha").trim());
                mapRtn.put("subTotal", rs01.getDouble("subTotal"));
                mapRtn.put("subTotalEnvase", rs01.getDouble("subTotalEnvase"));
                mapRtn.put("total", rs01.getDouble("total"));
                mapRtn.put("direccion", rs01.getString("direccion").trim());
                mapRtn.put("latitud", rs01.getString("latitud"));
                mapRtn.put("longitud", rs01.getString("longitud"));
                mapRtn.put("estado", rs01.getInt("estado"));
                mapRtn.put("idZonalVenta", rs01.getString("idZonalVenta"));
                mapRtn.put("color", rs01.getString("color"));
                mapRtn.put("nombreCliente", rs01.getString("nombreCliente"));
                mapRtn.put("telefono", rs01.getString("telefono"));
                mapRtn.put("items", rs01.getString("items"));
                dataList.add(mapRtn);
            } 
            
            if(dataList.size()>0){
                dataQuery.setApiEstado(Status.Ok);
                dataQuery.setData(dataList);
                dataQuery.setTotal(dataList.size()); 
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
