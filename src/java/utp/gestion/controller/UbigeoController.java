/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utp.gestion.classes.Configuration;
import utp.gestion.common.businessObject.DistritoQuery;
import utp.gestion.logic.UbigeoLogic;
import com.google.gson.Gson;
import java.io.PrintWriter;
import org.springframework.ui.ModelMap;


@Controller
public class UbigeoController {
    @RequestMapping(value = "/ubigeo/search",method = RequestMethod.GET)
    public void search(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        UbigeoLogic logic = new UbigeoLogic();
        List<DistritoQuery> listDistrito = new ArrayList<DistritoQuery>(0);
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        Gson json = new Gson();
        
        try {
            listDistrito = logic.searchDistrito();      
            map.put("success", true);
            map.put("data", listDistrito);
            out.write(json.toJson(map));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }
        
        //return new Gson().toJson(map);
    }
}
