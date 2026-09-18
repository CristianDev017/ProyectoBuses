package modelo;

public class ViajeDetalle {
    private Viaje viaje;
    private Ruta ruta;
    private Bus bus;
    private Sucursal origen;
    private Sucursal destino;
    private Integer asientosDisponibles;
    private String rutaDescripcion;
    private String fechaSalidaTexto;
    private String horaSalidaTexto;

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Sucursal getOrigen() {
        return origen;
    }

    public void setOrigen(Sucursal origen) {
        this.origen = origen;
    }

    public Sucursal getDestino() {
        return destino;
    }

    public void setDestino(Sucursal destino) {
        this.destino = destino;
    }

    public Integer getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(Integer asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public String getRutaDescripcion() {
        return rutaDescripcion;
    }

    public void setRutaDescripcion(String rutaDescripcion) {
        this.rutaDescripcion = rutaDescripcion;
    }

    public String getFechaSalidaTexto() {
        return fechaSalidaTexto;
    }

    public void setFechaSalidaTexto(String fechaSalidaTexto) {
        this.fechaSalidaTexto = fechaSalidaTexto;
    }

    public String getHoraSalidaTexto() {
        return horaSalidaTexto;
    }

    public void setHoraSalidaTexto(String horaSalidaTexto) {
        this.horaSalidaTexto = horaSalidaTexto;
    }
}
