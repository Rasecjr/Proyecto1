/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import utp.gestion.classes.BaseInputDelete;
import utp.gestion.classes.BaseInputEntity;
import utp.gestion.classes.CheckStatus;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.SingleQuery;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.PedidoInput;
import utp.gestion.common.businessObject.PedidoItemInput;
import utp.gestion.common.entities.Item;
import utp.gestion.data.ItemDAO;
import utp.gestion.data.PedidoDAO;


public class PedidoLogic {
    private final PedidoDAO dao = new PedidoDAO();
    private final ItemDAO daoItem = new ItemDAO();
    
    public DataQuery search(DataQueryInput input) throws Exception{
        return dao.search(input);
    }
    
    public SingleQuery SingleById(String id) throws Exception{
        return dao.singleById(id);
    }
    
    public DataQuery searchItems() throws Exception{
        return dao.searchItems();
    }
    
    public SingleQuery singleTarifaById(String id) throws Exception{
        return dao.singleTarifaById(id);
    }
    
    private CheckStatus Validate(PedidoInput input, String accion) throws Exception{
        CheckStatus checkstatus = new CheckStatus();
        String estado = Status.Ok;
        String mensaje = "";
        
        if (input != null)
        {
            if (StringUtils.isBlank(input.getNombre()))
            {
                estado = Status.Error;
                mensaje += Mensaje.PedidoNombre;
            }
            
            if (!(input.getTipoDocumento() == 1 || input.getTipoDocumento() == 2 || input.getTipoDocumento() == 3))               
            {
                estado = Status.Error;
                mensaje += Mensaje.PedidoTipoDocumento;
            }
            
            if (input.getTipoDocumento() == 1)
            {
                if(StringUtils.isBlank(input.getNumeroDocumento()) ||
                    input.getNumeroDocumento().length() != 8 || !isNumeric(input.getNumeroDocumento()))
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoNumeroDocumentoDNI;
                }
            }
            
            if (input.getTipoDocumento() == 2)
            {
                if(StringUtils.isBlank(input.getNumeroDocumento()) ||
                    input.getNumeroDocumento().length() != 11 || !isNumeric(input.getNumeroDocumento()))
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoNumeroDocumentoRUC;
                }
            }
            
            if (StringUtils.isBlank(input.getDireccion()))
            {
                estado = Status.Error;
                mensaje += Mensaje.PedidoDireccion;
            }
            
            if (input.getItems() == null || input.getItems().size() == 0)
            {
                estado = Status.Error;
                mensaje += Mensaje.PedidoItemCero;
            }
            else
            {
                //validar cero
                if (input.getItems().stream().filter(p -> StringUtils.isBlank(p.getId())).count() != 0)
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoItemCero;
                }
                
                //validar duplicidad
                if (input.getItems().size() != input.getItems().stream().distinct().count())
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoItemDuplicado;
                }
            }
            
            if (!(input.getTipoDocumentoCP() == 1 || input.getTipoDocumentoCP() == 2 || input.getTipoDocumentoCP() == 3))               
            {
                estado = Status.Error;
                mensaje += Mensaje.PedidoTipoDocumentoCP;
            }
            
            if (input.getTipoDocumentoCP() == 1)
            {
                if(StringUtils.isBlank(input.getNumeroDocumentoCP()) ||
                    input.getNumeroDocumentoCP().length() != 8 || !isNumeric(input.getNumeroDocumentoCP()))
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoNumeroDocumentoDNICP;
                }
            }
            
            if (input.getTipoDocumentoCP() == 2)
            {
                if(StringUtils.isBlank(input.getNumeroDocumentoCP()) ||
                    input.getNumeroDocumentoCP().length() != 11 || !isNumeric(input.getNumeroDocumentoCP()))
                {
                    estado = Status.Error;
                    mensaje += Mensaje.PedidoNumeroDocumentoRUCCP;
                }
            }
        }
        else
        {
            estado = Status.Error;
            mensaje = Mensaje.DatosInvalidos;
        }

        checkstatus.setApiEstado(estado);
        checkstatus.setApiMensaje(mensaje);
        
        return checkstatus;
    }
    
    public CheckStatus create(BaseInputEntity entity)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        PedidoInput input = (PedidoInput)entity;
        checkstatus = Validate(input,"");
        
        if(checkstatus.getApiEstado().equals(Status.Ok)){   
            CalcularTotales(input);
            checkstatus = dao.create(input);
        }
        
        return checkstatus;
    }
    
    public CheckStatus update(BaseInputEntity entity)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        PedidoInput input = (PedidoInput)entity;
        checkstatus = Validate(input,"editar");
        
        if(checkstatus.getApiEstado().equals(Status.Ok)){   
            CalcularTotales(input);
            checkstatus = dao.update(input);
        }
        
        return checkstatus;
    }
    
    public CheckStatus delete(BaseInputDelete input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        checkstatus = dao.Delete(input);
        return checkstatus;
    }
    
    private void CalcularTotales(PedidoInput pedido) throws Exception{
        double subtotal=0;
        double subtotalEnvase=0;
        
        for(PedidoItemInput item : pedido.getItems())
        {
            Item itemData = (Item)daoItem.singleById(item.getId());
            int clase = itemData.getClase();
            
            if (clase == 1)
            {
                subtotal += item.getCantidad() * item.getPrecioVenta();
            }
            else if (clase == 2)
            {
                subtotalEnvase += item.getCantidad() * item.getPrecioVenta();
            }
            
            pedido.setSubTotal(subtotal);
            pedido.setSubTotalEnvase(subtotalEnvase);
            pedido.setTotal(subtotal + subtotalEnvase);
        }
    }
    
    private static boolean isNumeric(String cadena){
	try {
		//Integer.parseInt(cadena);
                Long.parseLong(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
    }
    
    public CheckStatus InsertarPedidoCloud(PedidoInput input){
        CheckStatus checkStatus = new CheckStatus();
        try {
            URL url = new URL("http://localhost:3002/RestWebserviceDemo/rest/json/product/dynamicData?size=5");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return checkStatus;
    }
}
