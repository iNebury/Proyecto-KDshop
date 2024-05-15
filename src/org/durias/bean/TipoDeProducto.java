package org.durias.bean;

public class TipoDeProducto {

    private int codigoTipoProducto;
    private String descripcionProducto;

    public TipoDeProducto() {
    }

    public TipoDeProducto(int codigoTipoProducto, String descripcionProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
        this.descripcionProducto = descripcionProducto;
    }

    public int getCodigoTipoProducto() {
        return codigoTipoProducto;
    }

    public void setCodigoTipoProducto(int codigoTipoProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

}
