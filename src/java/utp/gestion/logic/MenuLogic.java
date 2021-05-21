/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import utp.gestion.classes.DataQuery;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.data.MenuDAO;

/**
 *
 * @author José
 */
public class MenuLogic {
    private final MenuDAO dao = new MenuDAO();
    
    public DataQuery search(DataQueryInput input) throws Exception{
        return dao.search(input);
    }
}
