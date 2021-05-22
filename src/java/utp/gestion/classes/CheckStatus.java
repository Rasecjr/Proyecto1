
package utp.gestion.classes;

public class CheckStatus {
    private String id;
    private String codigo;
    private String apiEstado;
    private String apiMensaje;
    
    public CheckStatus(String apiEstado, String apiMensaje)
    {
        id = "";
        codigo = "";
        this.apiEstado = apiEstado;
        this.apiMensaje = apiMensaje;
    }

    public CheckStatus()
    {
        id = "0";
        apiEstado = Status.Error;
        codigo = "";
        apiMensaje = "";
    }

    public CheckStatus(String id, String codigo, String apiEstado, String apiMensaje)
    {
        this.id = id;
        this.codigo =codigo;
        this.apiEstado = apiEstado;
        this.apiMensaje = apiMensaje;
    }
        
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the apiEstado
     */
    public String getApiEstado() {
        return apiEstado;
    }

    /**
     * @param apiEstado the apiEstado to set
     */
    public void setApiEstado(String apiEstado) {
        this.apiEstado = apiEstado;
    }

    /**
     * @return the apiMensaje
     */
    public String getApiMensaje() {
        return apiMensaje;
    }

    /**
     * @param apiMensaje the apiMensaje to set
     */
    public void setApiMensaje(String apiMensaje) {
        this.apiMensaje = apiMensaje;
    }   
    
}
