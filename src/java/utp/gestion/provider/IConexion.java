/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.provider;

import java.sql.Connection;

        
/**
 *
 * @author José
 */
public interface IConexion {
    public Connection geConnection() throws Exception;
    public void closeConnection(Connection cnx) throws Exception;
}
