/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

import java.io.FileOutputStream;
import java.sql.SQLException;
import utp.gestion.classes.CheckStatus;
import utp.gestion.classes.DataQuery;
import utp.gestion.classes.DataQueryInput;
import utp.gestion.classes.Mensaje;
import utp.gestion.classes.Status;
import utp.gestion.common.businessObject.ItemInput;
import utp.gestion.data.ItemDAO;
import org.apache.commons.lang3.StringUtils;
import utp.gestion.classes.BaseInputDelete;
import utp.gestion.classes.BaseInputEntity;
import utp.gestion.classes.SingleQuery;
import utp.gestion.data.RepositoryDAO;


public class ItemLogic {
    private final ItemDAO dao = new ItemDAO();
    private final RepositoryDAO _repository = new RepositoryDAO();
    
    public DataQuery search(DataQueryInput input) throws Exception{
        return dao.search(input);
    }
    
    public SingleQuery SingleById(String id) throws Exception{
        return dao.singleById(id);
    }
    
    private CheckStatus Validate(ItemInput input, String accion) throws Exception{
        CheckStatus checkstatus = new CheckStatus();
        String estado = Status.Ok;
        String mensaje = "";
        
        if (input != null)
        {
            // clase
            if (input.getClase() == 0)
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemClase;
            }
            // nombre
            if (StringUtils.isBlank(input.getNombre()))
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemNombre;
            }
            else if (input.getNombre().length() > 100)
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemNombreMax;
            }
            else
            {
                StringBuilder where = new StringBuilder();
                if (!accion.equals(""))
                {                   
                    where = new StringBuilder();
                    where.append(" IdItem<>'"+ input.getId() +"' and Nombre= '"+ input.getNombre().trim()+"' and Eliminado=0 ");
                    checkstatus = _repository.Validar("IdItem as id ","Item", "", where.toString());
                }
                else
                {
                    where = new StringBuilder();
                    where.append(" Nombre= '"+ input.getNombre().trim()+"' and Eliminado=0 ");
                    checkstatus = _repository.Validar("IdItem as id ","Item", "", where.toString());
                }
                
                if(checkstatus.getApiEstado().equals(Status.Error)){
                    estado = checkstatus.getApiEstado();
                    mensaje += Mensaje.ItemNombreDuplicado;
                }
            }

            // nombre comercial
            if (StringUtils.isBlank(input.getNombreComercial()))
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemNombreComercial;
            }
            else if (input.getNombreComercial().length() > 100)
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemNombreComercialMax;
            }
            
            if(input.getDescripcion().trim().length() > 100)
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemDescripcionMax;
            }
            if (!(input.getEstado() == 1 || input.getEstado() == 2))
            {
                estado = Status.Error;
                mensaje += Mensaje.ItemEstado;
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
        ItemInput input = (ItemInput)entity;
        checkstatus = Validate(input,"");
        
        if(checkstatus.getApiEstado().equals(Status.Ok)){   
            checkstatus = dao.create(input);
        }
        
        return checkstatus;
    }
    
    public CheckStatus update(BaseInputEntity entity)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        ItemInput input = (ItemInput)entity;
        checkstatus = Validate(input,"editar");
        
        if(checkstatus.getApiEstado().equals(Status.Ok)){   
            checkstatus = dao.update(input);
        }
        
        return checkstatus;
    }
    
    public CheckStatus delete(BaseInputDelete input)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        checkstatus = dao.Delete(input);
        return checkstatus;
    }
    
    /*public FileOutputStream export(DataQueryInput input) throws Exception{
        DataQuery data = new DataQuery();
        data = dao.search(input);      
        
        Workbook workbook = null;
        
        
        
    }*/
}
