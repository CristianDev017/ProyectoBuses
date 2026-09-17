package modelo;

public class Cartera {
    private int idCartera;
    private int idUsuario;
    private Double saldo;

    public Cartera() {}

    public Cartera(int idCartera, int idUsuario, Double saldo) {
        this.idCartera = idCartera;
        this.idUsuario = idUsuario;
        this.saldo = saldo;
    }

    public int getIdCartera() { return idCartera; }
    public void setIdCartera(int idCartera) { this.idCartera = idCartera; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }
}