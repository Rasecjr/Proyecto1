/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.classes;

/**
 *
 * @author José
 */
public class DataQueryInput {
    private String texto;
    private String ordenamiento;
    private int pagina;
    private int tamanio;
    private String idUsuario;
    
    public DataQueryInput()
    {
        texto = "";
        ordenamiento = "";
        pagina = 1;
        tamanio = 0;
        idUsuario = "";
    }

    /*public DataQueryInput(String texto, String ordenamiento, int pagina, int tamanio)
    {
        this.texto = texto;
        this.ordenamiento = ordenamiento;
        this.pagina = pagina;
        this.tamanio = tamanio;
    }*/
    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the ordenamiento
     */
    public String getOrdenamiento() {
        return ordenamiento;
    }

    /**
     * @param ordenamiento the ordenamiento to set
     */
    public void setOrdenamiento(String ordenamiento) {
        this.ordenamiento = ordenamiento;
    }

    /**
     * @return the pagina
     */
    public int getPagina() {
        return pagina;
    }

    /**
     * @param pagina the pagina to set
     */
    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    /**
     * @return the tamanio
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * @param tamanio the tamanio to set
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
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
