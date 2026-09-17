package dao;

import util.ConexionBD;

import java.sql.*;

public class MovimientoCarteraDAO {

    public int insertar(int idCartera, String tipo, double monto, Date fecha, String descripcion) {
        String sql = "INSERT INTO MovimientoCartera (id_cartera, tipo, monto, fecha, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCartera);
            ps.setString(2, tipo);
            ps.setDouble(3, monto);
            ps.setDate(4, fecha);
            ps.setString(5, descripcion);
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
}