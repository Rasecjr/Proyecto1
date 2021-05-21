/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import java.util.List;
import utp.gestion.common.businessObject.DistritoQuery;
import utp.gestion.data.UbigeoDAO;

/**
 *
 * @author José
 */
public class UbigeoLogic {
    private final UbigeoDAO dao = new UbigeoDAO();
    
    public List<DistritoQuery> searchDistrito()throws Exception{
        return dao.searchDistrito();
    }
}
