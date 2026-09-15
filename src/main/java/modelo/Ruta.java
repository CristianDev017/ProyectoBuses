package modelo;

public class Ruta {

    private int idRuta;
    private Integer idSucursalOrigen;
    private Integer idSucursalDestino;
    private Double distanciaKm;
    private Double precioBoleto;
    private String estado;

    public Ruta() {
    }

    public Ruta(int idRuta, Integer idSucursalOrigen, Integer idSucursalDestino,
                Double distanciaKm, Double precioBoleto, String estado) {
        this.idRuta = idRuta;
        this.idSucursalOrigen = idSucursalOrigen;
        this.idSucursalDestino = idSucursalDestino;
        this.distanciaKm = distanciaKm;
        this.precioBoleto = precioBoleto;
        this.estado = estado;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public void setIdSucursalOrigen(Integer idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public Integer getIdSucursalDestino() {
        return idSucursalDestino;
    }

    public void setIdSucursalDestino(Integer idSucursalDestino) {
        this.idSucursalDestino = idSucursalDestino;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Double getPrecioBoleto() {
        return precioBoleto;
    }

    public void setPrecioBoleto(Double precioBoleto) {
        this.precioBoleto = precioBoleto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
