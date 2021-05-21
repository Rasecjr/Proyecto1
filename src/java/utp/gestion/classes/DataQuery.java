/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author José
 */
public class DataQuery {
    private String apiEstado;
    private String apiMensaje;
    private List<Map<String, Object>> data;
    private int total;
    
    public DataQuery()
    {
        data = new ArrayList<>();
        apiEstado = Status.Ok;
        total = 0;
        apiMensaje = "";
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

    /**
     * @return the data
     */
    public List<Map<String, Object>> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
