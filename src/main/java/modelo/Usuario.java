package modelo;

public class Usuario {

    private int idUsuario;
    private Integer idSucursal;
    private String dpi;
    private String nombreCompleto;
    private String nit;
    private String telefono;
    private String direccion;
    private String rol;
    private String estado;
    private String correo;
    private String password;

    public Usuario() {
    }

    public Usuario(int idUsuario, Integer idSucursal, String dpi,
                   String nombreCompleto, String nit, String telefono,
                   String direccion, String rol, String estado,
                   String correo, String password) {

        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.nit = nit;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.estado = estado;
        this.correo = correo;
        this.password = password;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String ultimoError;

    public String getUltimoError() {
        return ultimoError;
    }
}