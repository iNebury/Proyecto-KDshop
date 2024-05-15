package org.durias.bean;

public class Compras {

    private int numeroDocumento;
    private String fechaDocumento;
    private String descripcionCompra;
    private Double totalDocumento;

    public Compras() {
    }

    public Compras(int numeroDocumento, String fechaDocumento, String descripcionCompra, Double totalDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.fechaDocumento = fechaDocumento;
        this.descripcionCompra = descripcionCompra;
        this.totalDocumento = totalDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(String fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getDescripcionCompra() {
        return descripcionCompra;
    }

    public void setDescripcionCompra(String descripcionCompra) {
        this.descripcionCompra = descripcionCompra;
    }

    public Double getTotalDocumento() {
        return totalDocumento;
    }

    public void setTotalDocumento(Double totalDocumento) {
        this.totalDocumento = totalDocumento;
    }

}
