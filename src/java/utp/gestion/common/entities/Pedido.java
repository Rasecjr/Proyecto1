/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.gestion.common.entities;

import java.util.List;
import utp.gestion.classes.SingleQuery;


public class Pedido extends SingleQuery{
    private String id;
    private String codigo;
    private String fechaPedido;
    private int tipoComprobante;
    private int tipoPago;
    private double subTotal; 
    private double subTotalEnvase;
    private double total; 
    private String direccion;
    private String referencia;
    private String longitud;
    private String latitud;
    private String observacion;
    private String idUbigeo;
    private String idZonalVenta;
    private String nombre;    
    private String correo;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String movil;
    private String nombreCP;
    private String tipoDocumentoCP;
    private String numeroDocumentoCP;
    private String direccionCP;
    private int estado;
    
    private List<PedidoItem> items; 
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
     * @return the fechaPedido
     */
    public String getFechaPedido() {
        return fechaPedido;
    }

    /**
     * @param fechaPedido the fechaPedido to set
     */
    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * @return the tipoComprobante
     */
    public int getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * @param tipoComprobante the tipoComprobante to set
     */
    public void setTipoComprobante(int tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * @return the tipoPago
     */
    public int getTipoPago() {
        return tipoPago;
    }

    /**
     * @param tipoPago the tipoPago to set
     */
    public void setTipoPago(int tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the subTotalEnvase
     */
    public double getSubTotalEnvase() {
        return subTotalEnvase;
    }

    /**
     * @param subTotalEnvase the subTotalEnvase to set
     */
    public void setSubTotalEnvase(double subTotalEnvase) {
        this.subTotalEnvase = subTotalEnvase;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /**
     * @return the idUbigeo
     */
    public String getIdUbigeo() {
        return idUbigeo;
    }

    /**
     * @param idUbigeo the idUbigeo to set
     */
    public void setIdUbigeo(String idUbigeo) {
        this.idUbigeo = idUbigeo;
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
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
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
     * @return the movil
     */
    public String getMovil() {
        return movil;
    }

    /**
     * @param movil the movil to set
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * @return the NombreCP
     */
    public String getNombreCP() {
        return nombreCP;
    }

    /**
     * @param nombreCP the NombreCP to set
     */
    public void setNombreCP(String nombreCP) {
        this.nombreCP = nombreCP;
    }

    /**
     * @return the tipoDocumentoCP
     */
    public String getTipoDocumentoCP() {
        return tipoDocumentoCP;
    }

    /**
     * @param tipoDocumentoCP the tipoDocumentoCP to set
     */
    public void setTipoDocumentoCP(String tipoDocumentoCP) {
        this.tipoDocumentoCP = tipoDocumentoCP;
    }

    /**
     * @return the numeroDocumentoCP
     */
    public String getNumeroDocumentoCP() {
        return numeroDocumentoCP;
    }

    /**
     * @param numeroDocumentoCP the numeroDocumentoCP to set
     */
    public void setNumeroDocumentoCP(String numeroDocumentoCP) {
        this.numeroDocumentoCP = numeroDocumentoCP;
    }

    /**
     * @return the direccionCP
     */
    public String getDireccionCP() {
        return direccionCP;
    }

    /**
     * @param direccionCP the direccionCP to set
     */
    public void setDireccionCP(String direccionCP) {
        this.direccionCP = direccionCP;
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
     * @return the items
     */
    public List<PedidoItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }
}
