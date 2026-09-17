package dao;

import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionSistemaDAO {

    public Double obtenerMontoDepreciacion() {
        String sql = "SELECT monto_depreciacion_km FROM ConfiguracionSistema ORDER BY fecha_actualizacion DESC LIMIT 1";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double val = rs.getDouble("monto_depreciacion_km");
                return rs.wasNull() ? null : val;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarMonto(double nuevoMonto, java.sql.Date fecha) {
        String sql = "UPDATE ConfiguracionSistema SET monto_depreciacion_km = ?, fecha_actualizacion = ? ORDER BY fecha_actualizacion DESC LIMIT 1";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoMonto);
            ps.setDate(2, fecha);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
