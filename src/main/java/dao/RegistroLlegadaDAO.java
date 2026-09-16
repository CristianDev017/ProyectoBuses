package dao;

import modelo.RegistroLlegada;
import util.ConexionBD;

import java.sql.*;

public class RegistroLlegadaDAO {

    public int insertar(RegistroLlegada registro) {
        String sql = "INSERT INTO RegistroLlegada (id_viaje, fecha_hora_llegada_real, kilometraje_final, gasto_combustible, monto_depreciacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, registro.getIdViaje());

            if (registro.getFechaHoraLlegadaReal() != null) {
                ps.setTimestamp(2, registro.getFechaHoraLlegadaReal());
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            if (registro.getKilometrajeFinal() != null) {
                ps.setInt(3, registro.getKilometrajeFinal());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (registro.getGastoCombustible() != null) {
                ps.setDouble(4, registro.getGastoCombustible());
            } else {
                ps.setNull(4, Types.DOUBLE);
            }

            if (registro.getMontoDepreciacion() != null) {
                ps.setDouble(5, registro.getMontoDepreciacion());
            } else {
                ps.setNull(5, Types.DOUBLE);
            }

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
