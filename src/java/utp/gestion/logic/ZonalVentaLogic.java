/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.CoberturaIndOutput;
import utp.gestion.common.businessObject.CoberturaQueryOutput;
import utp.gestion.common.businessObject.ZonalVentaInput;
import utp.gestion.data.ZonalVentaDAO;

/**
 *
 * @author José
 */
public class ZonalVentaLogic {
    private final ZonalVentaDAO dao = new ZonalVentaDAO();
    
    public DataQuery searchZonales(DataQueryInput input) throws Exception{
        return dao.searchZonales(input);
    }
    
    public String CreateZonalVenta(ZonalVentaInput filter) throws SQLException, Exception{
        return dao.CreateZonalVenta(filter);
    }
    
    public String UpdateZonalVenta(ZonalVentaInput filter) throws SQLException, Exception{
        return dao.UpdateZonalVenta(filter);
    }
    
    public String deleteZonalVenta(String id) throws SQLException, Exception{
        return dao.deleteZonalVenta(id);
    }
    
    public DataQuery searchZonalesCoordenadas() throws Exception{
        return dao.searchZonalesCoordenadas();
    }
    
    public CoberturaIndOutput GetExisteCobertura(String latitud, String longitud) throws Exception{
        CoberturaIndOutput cobertura = new CoberturaIndOutput();
        cobertura.setApiEstado(Status.Error);
        cobertura.setApiMensaje(Mensaje.CoberturaInValida);
        boolean existe;
        DataQuery coordenadasQueries = new DataQuery();
        coordenadasQueries = dao.searchZonalesCoordenadas();
        
        for (Map<String, Object> obj : coordenadasQueries.getData()) {
            List<String> ptos = Arrays.asList(obj.get("pts").toString().substring(0, obj.get("pts").toString().length() - 1).split("[|]"));
            existe = ValidatePointInPoly(Double.parseDouble(latitud), Double.parseDouble(longitud), ptos);
            
            if (existe){
                cobertura.setId(obj.get("id").toString());
                cobertura.setApiEstado(Status.Ok);
                cobertura.setApiMensaje("");
            }
        }
        
        return cobertura;
    }
    
    private boolean ValidatePointInPoly(double x, double y, List<String> poly) throws Exception{
        boolean inside = false;
        int n = poly.size();
        double p1x = Double.parseDouble(poly.get(0).split(",")[0]);
        double p1y = Double.parseDouble(poly.get(0).split(",")[1]);
        int counter = 0;
        double xinters;

        for (int i = 1; i <= poly.size(); i++)
        {
            double p2x = Double.parseDouble(poly.get(i % n).split(",")[0]);
            double p2y = Double.parseDouble(poly.get(i % n).split(",")[1]);
            if (y > Math.min(p1y, p2y))
            {
                if (y <= Math.max(p1y, p2y))
                {
                    if (x <= Math.max(p1x, p2x))
                    {
                        if (p1y != p2y)
                        {
                            xinters = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x;
                            if (p1x == p2x || x <= xinters)
                            {
                                counter++;
                            }
                        }
                    }
                }
            }
            p1x = p2x;
            p1y = p2y;
        }

        if (counter % 2 != 0)
        {
            inside = !inside;
        }
        return inside;
    }
}
