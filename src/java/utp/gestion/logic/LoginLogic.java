/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import utp.gestion.common.businessObject.UsuarioLoginIndOutput;
import utp.gestion.common.businessObject.UsuarioLoginInput;
import utp.gestion.data.LoginDAO;


public class LoginLogic {
    private final LoginDAO dao = new LoginDAO();
    
    public UsuarioLoginIndOutput login(UsuarioLoginInput input) throws Exception{
        return dao.login(input);
    }
}
