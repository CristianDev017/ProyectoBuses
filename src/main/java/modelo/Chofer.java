package modelo;

import java.sql.Date;

public class Chofer {

    private int idChofer;
    private Integer idSucursal;
    private String foto;
    private String nombreCompleto;
    private String numeroLicencia;
    private String tipoLicencia;
    private Date fechaVencimiento;
    private String telefono;
    private Double salarioBaseViaje;
    private String estado;

    public Chofer() {
    }

    public Chofer(int idChofer, Integer idSucursal, String foto, String nombreCompleto, String numeroLicencia,
                  String tipoLicencia, Date fechaVencimiento, String telefono, Double salarioBaseViaje, String estado) {
        this.idChofer = idChofer;
        this.idSucursal = idSucursal;
        this.foto = foto;
        this.nombreCompleto = nombreCompleto;
        this.numeroLicencia = numeroLicencia;
        this.tipoLicencia = tipoLicencia;
        this.fechaVencimiento = fechaVencimiento;
        this.telefono = telefono;
        this.salarioBaseViaje = salarioBaseViaje;
        this.estado = estado;
    }

    public int getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(int idChofer) {
        this.idChofer = idChofer;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getSalarioBaseViaje() {
        return salarioBaseViaje;
    }

    public void setSalarioBaseViaje(Double salarioBaseViaje) {
        this.salarioBaseViaje = salarioBaseViaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
