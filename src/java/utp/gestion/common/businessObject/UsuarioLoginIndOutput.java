/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.common.businessObject;

import java.util.List;
import utp.gestion.classes.SingleQuery;

/**
 *
 * @author José
 */
public class UsuarioLoginIndOutput extends SingleQuery{
    private String id;
    private String codigo;
    private String nombre;
    private String correo;
    private List<MenuQuery> menus;
    
    public UsuarioLoginIndOutput()
    {
        this.id = "";
        this.codigo="";
        this.nombre = "";
        this.correo = "";
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    /**
     * @return the menus
     */
    public List<MenuQuery> getMenus() {
        return menus;
    }

    /**
     * @param menus the menus to set
     */
    public void setMenus(List<MenuQuery> menus) {
        this.menus = menus;
    }
}
