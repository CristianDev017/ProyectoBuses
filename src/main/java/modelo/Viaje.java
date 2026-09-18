package modelo;

import java.sql.Timestamp;

public class Viaje {

    private int idViaje;
    private Integer idBus;
    private Integer idChofer;
    private String nombreChofer;
    private Integer idRuta;
    private String tipoViaje;
    private Timestamp fechaSalida;
    private Timestamp fechaLlegadaEstimada;
    private String estado;

    public Viaje() {
    }

    public Viaje(int idViaje, Integer idBus, Integer idChofer, Integer idRuta, String tipoViaje, Timestamp fechaSalida, Timestamp fechaLlegadaEstimada, String estado) {
        this.idViaje = idViaje;
        this.idBus = idBus;
        this.idChofer = idChofer;
        this.idRuta = idRuta;
        this.tipoViaje = tipoViaje;
        this.fechaSalida = fechaSalida;
        this.fechaLlegadaEstimada = fechaLlegadaEstimada;
        this.estado = estado;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public Integer getIdBus() {
        return idBus;
    }

    public void setIdBus(Integer idBus) {
        this.idBus = idBus;
    }

    public Integer getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(Integer idChofer) {
        this.idChofer = idChofer;
    }

    public String getNombreChofer() {
        return nombreChofer;
    }

    public void setNombreChofer(String nombreChofer) {
        this.nombreChofer = nombreChofer;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public String getTipoViaje() {
        return tipoViaje;
    }

    public void setTipoViaje(String tipoViaje) {
        this.tipoViaje = tipoViaje;
    }

    public Timestamp getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Timestamp fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Timestamp getFechaLlegadaEstimada() {
        return fechaLlegadaEstimada;
    }

    public void setFechaLlegadaEstimada(Timestamp fechaLlegadaEstimada) {
        this.fechaLlegadaEstimada = fechaLlegadaEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
