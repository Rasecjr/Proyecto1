/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import utp.gestion.classes.DataQuery;
import utp.gestion.data.MonitorDAO;


public class MonitorLogic {
    private final MonitorDAO dao = new MonitorDAO();   
 
    public DataQuery search(int id) throws Exception{
        return dao.search(id);
    }
}
