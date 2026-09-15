package modelo;

import java.sql.Timestamp;

public class RegistroSalida {

    private int idSalida;
    private int idViaje;
    private Integer idBus;
    private Integer idChofer;
    private Timestamp fechaHoraSalidaReal;
    private Integer kilometrajeInicial;

    public RegistroSalida() {
    }

    public RegistroSalida(int idSalida, int idViaje, Integer idBus, Integer idChofer, Timestamp fechaHoraSalidaReal, Integer kilometrajeInicial) {
        this.idSalida = idSalida;
        this.idViaje = idViaje;
        this.idBus = idBus;
        this.idChofer = idChofer;
        this.fechaHoraSalidaReal = fechaHoraSalidaReal;
        this.kilometrajeInicial = kilometrajeInicial;
    }

    public int getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(int idSalida) {
        this.idSalida = idSalida;
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

    public Timestamp getFechaHoraSalidaReal() {
        return fechaHoraSalidaReal;
    }

    public void setFechaHoraSalidaReal(Timestamp fechaHoraSalidaReal) {
        this.fechaHoraSalidaReal = fechaHoraSalidaReal;
    }

    public Integer getKilometrajeInicial() {
        return kilometrajeInicial;
    }

    public void setKilometrajeInicial(Integer kilometrajeInicial) {
        this.kilometrajeInicial = kilometrajeInicial;
    }
}
