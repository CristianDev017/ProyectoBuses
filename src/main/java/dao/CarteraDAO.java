package dao;

import modelo.Cartera;
import util.ConexionBD;

import java.sql.*;

public class CarteraDAO {

    public Cartera buscarPorIdUsuario(int idUsuario) {
        String sql = "SELECT * FROM Cartera WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cartera(rs.getInt("id_cartera"), rs.getInt("id_usuario"), rs.getDouble("saldo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int crearCartera(int idUsuario) {
        String sql = "INSERT INTO Cartera (id_usuario, saldo) VALUES (?, 0)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean actualizarSaldo(int idCartera, double nuevoSaldo) {
        String sql = "UPDATE Cartera SET saldo = ? WHERE id_cartera = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, idCartera);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}