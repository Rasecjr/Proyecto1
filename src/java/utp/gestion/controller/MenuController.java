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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.logic.MenuLogic;

/**
 *
 * @author José
 */
@Controller
public class MenuController {
    @RequestMapping(value = "/menu/search",method = RequestMethod.GET)
    public void search(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        MenuLogic logic = new MenuLogic();
        DataQuery data = new DataQuery();
        DataQueryInput input = new DataQueryInput();
        input.setTexto(req.getParameter("texto"));
        input.setOrdenamiento(req.getParameter("ordenamiento"));
        input.setPagina(Integer.parseInt(req.getParameter("pagina")));
        input.setTamanio(Integer.parseInt(req.getParameter("tamanio")));
        
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        Gson json = new Gson();
        try {
            data = logic.search(input);      
            out.write(json.toJson(data));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }   
    }
}
