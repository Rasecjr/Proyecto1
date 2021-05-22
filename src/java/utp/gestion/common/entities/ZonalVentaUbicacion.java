/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.common.entities;

import utp.gestion.classes.BaseEntity;


public class ZonalVentaUbicacion extends BaseEntity{
    private String id;
    private String idZonalVenta;
    private int orden;
    private String latitud;
    private String longitud;
    
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
     * @return the idZonalVenta
     */
    public String getIdZonalVenta() {
        return idZonalVenta;
    }

    /**
     * @param idZonalVenta the idZonalVenta to set
     */
    public void setIdZonalVenta(String idZonalVenta) {
        this.idZonalVenta = idZonalVenta;
    }

    /**
     * @return the orden
     */
    public int getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    /**
     * @return the latitud
     */
    public String getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public String getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }    
}
