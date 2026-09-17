package dao;

import modelo.RegistroLlegada;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Object[]> reporteDepreciacionPorBus() {
        List<Object[]> resultado = new ArrayList<>();

        String sql = "SELECT b.id_bus, b.placa, b.marca, b.modelo, " +
                "COALESCE(SUM(rl.kilometraje_final - rs.kilometraje_inicial), 0) AS km_totales, " +
                "COALESCE(SUM(rl.monto_depreciacion), 0) AS depreciacion_total " +
                "FROM Bus b " +
                "LEFT JOIN Viaje v ON v.id_bus = b.id_bus " +
                "LEFT JOIN RegistroSalida rs ON rs.id_viaje = v.id_viaje " +
                "LEFT JOIN RegistroLlegada rl ON rl.id_viaje = v.id_viaje " +
                "GROUP BY b.id_bus, b.placa, b.marca, b.modelo";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[] {
                        rs.getInt("id_bus"),
                        rs.getString("placa"),
                        rs.getString("marca") + " " + rs.getString("modelo"),
                        rs.getInt("km_totales"),
                        rs.getDouble("depreciacion_total")
                };
                resultado.add(fila);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }
}
