package modelo;

import java.sql.Date;

public class Boleto {
    private int idBoleto;
    private int idViaje;
    private int idUsuario;
    private Integer numeroAsiento;
    private Double precio;
    private Date fechaPago;
    private String estado;

    public Boleto() {}

    public Boleto(int idBoleto, int idViaje, int idUsuario, Integer numeroAsiento, Double precio, Date fechaPago, String estado) {
        this.idBoleto = idBoleto;
        this.idViaje = idViaje;
        this.idUsuario = idUsuario;
        this.numeroAsiento = numeroAsiento;
        this.precio = precio;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public int getIdBoleto() { return idBoleto; }
    public void setIdBoleto(int idBoleto) { this.idBoleto = idBoleto; }
    public int getIdViaje() { return idViaje; }
    public void setIdViaje(int idViaje) { this.idViaje = idViaje; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public Integer getNumeroAsiento() { return numeroAsiento; }
    public void setNumeroAsiento(Integer numeroAsiento) { this.numeroAsiento = numeroAsiento; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}