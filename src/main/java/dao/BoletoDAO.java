package dao;

import modelo.Boleto;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoletoDAO {

    public int insertar(Boleto b) {
        String sql = "INSERT INTO Boleto (id_viaje, id_usuario, numero_asiento, precio, fecha_pago, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getIdViaje());
            ps.setInt(2, b.getIdUsuario());
            ps.setInt(3, b.getNumeroAsiento());
            ps.setDouble(4, b.getPrecio());
            ps.setDate(5, b.getFechaPago());
            ps.setString(6, b.getEstado());
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

    public List<Boleto> listarPorUsuario(int idUsuario) {
        List<Boleto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Boleto WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Boleto(
                            rs.getInt("id_boleto"), rs.getInt("id_viaje"), rs.getInt("id_usuario"),
                            rs.getInt("numero_asiento"), rs.getDouble("precio"),
                            rs.getDate("fecha_pago"), rs.getString("estado")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contarAsientosVendidos(int idViaje) {
        String sql = "SELECT COUNT(*) FROM Boleto WHERE id_viaje = ? AND estado = 'PAGADO'";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}