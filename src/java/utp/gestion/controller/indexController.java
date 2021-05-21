/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;
import utp.gestion.classes.Configuration;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.UsuarioLoginIndOutput;
import utp.gestion.common.businessObject.UsuarioLoginInput;
import utp.gestion.logic.LoginLogic;

/**
 *
 * @author José
 */
@Controller
public class indexController {
    @RequestMapping(value = "/index")
    public ModelAndView index(){
        return new ModelAndView();
    }
    
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(HttpServletRequest req, HttpServletResponse res,Model model) throws IOException, ServletException{
        UsuarioLoginInput input = new UsuarioLoginInput();
        UsuarioLoginIndOutput loginIndOutput = new UsuarioLoginIndOutput();
        LoginLogic logic = new LoginLogic();
        input.setUsuario(req.getParameter("username"));
        input.setContrasenia(req.getParameter("password"));
       
        try {
            HttpSession sesion = (HttpSession) req.getSession();
            
            loginIndOutput = logic.login(input);
            sesion.setAttribute("usuario", loginIndOutput); 
            if(loginIndOutput.getApiEstado().equals(Status.Ok)){
                res.sendRedirect(Configuration.URL + "home");
            }
            else{
                res.sendRedirect(Configuration.URL + "index");
            }
        } catch (Exception e) {
            res.sendRedirect(Configuration.URL + "index");
        } finally {

        } 
    }
    
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public void logout(HttpServletRequest req, HttpServletResponse res,Model model) throws IOException, ServletException{
        try{
            HttpSession sesion = (HttpSession) req.getSession();
            sesion.setAttribute("usuario", null);
            res.sendRedirect(Configuration.URL);
        }catch(Exception ex){
            
        }   
    }
}
