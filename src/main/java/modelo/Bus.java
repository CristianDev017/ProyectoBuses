package modelo;

public class Bus {

    private int idBus;
    private Integer idSucursal;
    private String foto;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anioFabricacion;
    private Integer capacidad;
    private String estadoOperativo;
    private Integer kilometrajeActual;

    public Bus() {
    }

    public Bus(int idBus, Integer idSucursal, String foto, String placa, String marca, String modelo,
               Integer anioFabricacion, Integer capacidad, String estadoOperativo, Integer kilometrajeActual) {
        this.idBus = idBus;
        this.idSucursal = idSucursal;
        this.foto = foto;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anioFabricacion = anioFabricacion;
        this.capacidad = capacidad;
        this.estadoOperativo = estadoOperativo;
        this.kilometrajeActual = kilometrajeActual;
    }

    public int getIdBus() {
        return idBus;
    }

    public void setIdBus(int idBus) {
        this.idBus = idBus;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(Integer anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

    public Integer getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(Integer kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }

}
