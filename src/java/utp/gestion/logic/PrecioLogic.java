/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.logic;

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
import utp.gestion.common.businessObject.PrecioBaseInput;
import utp.gestion.data.PrecioDAO;
import utp.gestion.data.RepositoryDAO;


public class PrecioLogic {
    private final PrecioDAO dao = new PrecioDAO();
    private final RepositoryDAO _repository = new RepositoryDAO();
    
    public DataQuery search(DataQueryInput input) throws Exception{
        return dao.search(input);
    }
    
    public SingleQuery SingleById(String id) throws Exception{
        return dao.singleById(id);
    }
    
    private CheckStatus Validate(PrecioBaseInput input, String accion) throws Exception{
        CheckStatus checkstatus = new CheckStatus();
        String estado = Status.Ok;
        String mensaje = "";
        
        if (input != null)
        {
            if (StringUtils.isBlank(input.getIdItem())){
                estado = Status.Error;
                mensaje += Mensaje.PrecioBaseIdItemVacio;
            }
           
            if (input.getPrecio() <= 0)
            {
                estado = Status.Error;
                mensaje += Mensaje.PrecioBasePrecio;
            }
            
            if (StringUtils.isBlank(input.getFechaVigencia())){
                estado = Status.Error;
                mensaje += Mensaje.PrecioBaseFechaVigencia;
            }
            
            if (!estado.equals(Status.Error)){
                StringBuilder where = new StringBuilder();
                if (!accion.equals("")){
                    where = new StringBuilder();
                    where.append(" IdPrecioBase<>'"+ input.getId() +"' and IdItem= '"+ input.getIdItem()+"' and Eliminado=0 and FechaVigencia = convert(date,'"+ input.getFechaVigencia()+"',103)");

                    checkstatus = _repository.Validar("IdPrecioBase as id ","PrecioBase", "", where.toString());
                }
                else{
                    where = new StringBuilder();
                    where.append(" IdItem= '"+ input.getIdItem()+"' and Eliminado=0 and FechaVigencia = convert(date,'"+ input.getFechaVigencia()+"',103)");

                    checkstatus = _repository.Validar("IdPrecioBase as id ","PrecioBase", "", where.toString());
                }
                
                if(checkstatus.getApiEstado().equals(Status.Error)){
                    estado = checkstatus.getApiEstado();
                    mensaje += Mensaje.PrecioBaseRepetido;
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
        PrecioBaseInput input = (PrecioBaseInput)entity;
        checkstatus = Validate(input,"");
        
        if(checkstatus.getApiEstado().equals(Status.Ok)){   
            checkstatus = dao.create(input);
        }
        
        return checkstatus;
    }
    
    public CheckStatus update(BaseInputEntity entity)throws SQLException, Exception{
        CheckStatus checkstatus = new CheckStatus();
        PrecioBaseInput input = (PrecioBaseInput)entity;
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
}
