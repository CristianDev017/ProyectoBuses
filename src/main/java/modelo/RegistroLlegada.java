package modelo;

import java.sql.Timestamp;

public class RegistroLlegada {

    private int idLlegada;
    private int idViaje;
    private Timestamp fechaHoraLlegadaReal;
    private Integer kilometrajeFinal;
    private Double gastoCombustible;
    private Double montoDepreciacion;

    public RegistroLlegada() {
    }

    public RegistroLlegada(int idLlegada, int idViaje, Timestamp fechaHoraLlegadaReal, Integer kilometrajeFinal, Double gastoCombustible, Double montoDepreciacion) {
        this.idLlegada = idLlegada;
        this.idViaje = idViaje;
        this.fechaHoraLlegadaReal = fechaHoraLlegadaReal;
        this.kilometrajeFinal = kilometrajeFinal;
        this.gastoCombustible = gastoCombustible;
        this.montoDepreciacion = montoDepreciacion;
    }

    public int getIdLlegada() {
        return idLlegada;
    }

    public void setIdLlegada(int idLlegada) {
        this.idLlegada = idLlegada;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(int idViaje) {
        this.idViaje = idViaje;
    }

    public Timestamp getFechaHoraLlegadaReal() {
        return fechaHoraLlegadaReal;
    }

    public void setFechaHoraLlegadaReal(Timestamp fechaHoraLlegadaReal) {
        this.fechaHoraLlegadaReal = fechaHoraLlegadaReal;
    }

    public Integer getKilometrajeFinal() {
        return kilometrajeFinal;
    }

    public void setKilometrajeFinal(Integer kilometrajeFinal) {
        this.kilometrajeFinal = kilometrajeFinal;
    }

    public Double getGastoCombustible() {
        return gastoCombustible;
    }

    public void setGastoCombustible(Double gastoCombustible) {
        this.gastoCombustible = gastoCombustible;
    }

    public Double getMontoDepreciacion() {
        return montoDepreciacion;
    }

    public void setMontoDepreciacion(Double montoDepreciacion) {
        this.montoDepreciacion = montoDepreciacion;
    }
}
