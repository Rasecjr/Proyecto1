
package utp.gestion.classes;

public class BaseInputDelete {
    private String id;
    private String usuario;
    private String idUsuario;
    
    public BaseInputDelete() {
        id = "";
        idUsuario = "";
    }

    public BaseInputDelete(String id)
    {
        this.id = id;
        idUsuario = "";
    }

    public BaseInputDelete(String id,String idUsuario)
    {
        this.id = id;
        this.idUsuario = idUsuario;
    }

    public BaseInputDelete(String id, String idUsuario, String usuario)
    {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuario = "";
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
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the idUsuario
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
