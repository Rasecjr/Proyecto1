/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.DataQuery;
import utp.gestion.common.businessObject.CoberturaIndOutput;
import utp.gestion.common.businessObject.UsuarioLoginIndOutput;
import utp.gestion.common.businessObject.ZonalVentaInput;
import utp.gestion.logic.ZonalVentaLogic;

/**
 *
 * @author José
 */
@Controller
public class ZonalVentaController {
    @RequestMapping(value = "/zonalVenta")
    public ModelAndView zonalVenta(){
        return new ModelAndView();
    }
    
    @RequestMapping(value = "/zonalVenta/search",method = RequestMethod.GET)
    public void search(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ZonalVentaLogic logic = new ZonalVentaLogic();
        DataQuery data = new DataQuery();
        DataQueryInput input = new DataQueryInput();
        input.setTexto(req.getParameter("texto"));
        input.setOrdenamiento(req.getParameter("ordenamiento"));
        input.setPagina(Integer.parseInt(req.getParameter("pagina")));
        input.setTamanio(Integer.parseInt(req.getParameter("tamanio")));
        
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        Gson json = new Gson();
        try {
            data = logic.searchZonales(input);      
            map.put("success", true);
            map.put("data", data);
            out.write(json.toJson(map));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }   
    }
    
    @RequestMapping(value = "/zonalVenta/mantenance",method = RequestMethod.POST)
    public void mantenance(HttpServletRequest req, HttpServletResponse res,Model model) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        ZonalVentaLogic logic = new ZonalVentaLogic();
        Gson json = new Gson();
        String option = req.getParameter("option");
        String rpta="";
        String jsonData="";
        ZonalVentaInput zonalVenta = new ZonalVentaInput();
        try {
            HttpSession _sesion = (HttpSession) req.getSession();
            UsuarioLoginIndOutput _usuario = (UsuarioLoginIndOutput)_sesion.getAttribute("usuario");
            
            switch(option){
                case "I":
                    jsonData = req.getParameter("data");
                    zonalVenta = new Gson().fromJson(jsonData, ZonalVentaInput.class);
                    zonalVenta.setUsuario(_usuario.getNombre());
                    rpta = logic.CreateZonalVenta(zonalVenta);
                    break;
                case "U":
                    jsonData = req.getParameter("data");
                    zonalVenta = new Gson().fromJson(jsonData, ZonalVentaInput.class);
                    zonalVenta.setUsuario(_usuario.getNombre());
                    rpta = logic.UpdateZonalVenta(zonalVenta);
                    break;
                case "D":
                    String id = req.getParameter("id");
                    rpta = logic.deleteZonalVenta(id);
                    break;
            }
            
            map.put("success", true);
            map.put("data", rpta);
            out.write(json.toJson(map));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }
    }
    
    @RequestMapping(value = "/zonalVenta/coordenadas",method = RequestMethod.GET)
    public void searchCoordenadas(HttpServletRequest req, HttpServletResponse res) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ZonalVentaLogic logic = new ZonalVentaLogic();
        DataQuery data = new DataQuery();
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        Gson json = new Gson();
        try {
            data = logic.searchZonalesCoordenadas();      
            out.write(json.toJson(data));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }   
    }
    
    @RequestMapping(value = "/zonalVenta/validaCobertura",method = RequestMethod.POST)
    public void validaCobertura(HttpServletRequest req, HttpServletResponse res,Model model) throws IOException{
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        LinkedHashMap<String, Object> map =  new LinkedHashMap<>();        
        ZonalVentaLogic logic = new ZonalVentaLogic();
        Gson json = new Gson();
        String latitud = req.getParameter("latitud");
        String longitud = req.getParameter("longitud");

        try {
            CoberturaIndOutput cobertura = logic.GetExisteCobertura(latitud, longitud);
            out.write(json.toJson(cobertura));
        } catch (Exception e) {
            map.put("error", e.getMessage());
            out.write(json.toJson(map));
        } finally {
            out.close();
        }
    }
}
