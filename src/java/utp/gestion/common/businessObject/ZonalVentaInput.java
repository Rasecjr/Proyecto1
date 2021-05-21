/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.common.businessObject;

import java.util.List;
import utp.gestion.classes.BaseInputEntity;

/**
 *
 * @author José
 */
public class ZonalVentaInput extends BaseInputEntity
{
    private String id;
    private String idDistrito;
    private String nombre;
    private String telefono;
    private String color;
    private boolean esPropio;
    private int estado;
    private List<ZonalVentaUbicacionInput> ubicaciones; 

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
     * @return the idDistrito
     */
    public String getIdDistrito() {
        return idDistrito;
    }

    /**
     * @param idDistrito the idDistrito to set
     */
    public void setIdDistrito(String idDistrito) {
        this.idDistrito = idDistrito;
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
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return the ubicaciones
     */
    public List<ZonalVentaUbicacionInput> getUbicaciones() {
        return ubicaciones;
    }

    /**
     * @param ubicaciones the ubicaciones to set
     */
    public void setUbicaciones(List<ZonalVentaUbicacionInput> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
    
    /**
     * @return the esPropio
     */
    public boolean isEsPropio() {
        return esPropio;
    }

    /**
     * @param esPropio the esPropio to set
     */
    public void setEsPropio(boolean esPropio) {
        this.esPropio = esPropio;
    }
}
