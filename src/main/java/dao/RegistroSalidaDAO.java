package dao;

import modelo.RegistroSalida;
import util.ConexionBD;

import java.sql.*;

public class RegistroSalidaDAO {

    public int insertar(RegistroSalida registro) {
        String sql = "INSERT INTO RegistroSalida (id_viaje, id_bus, id_chofer, fecha_hora_salida_real, kilometraje_inicial) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, registro.getIdViaje());

            if (registro.getIdBus() != null) {
                ps.setInt(2, registro.getIdBus());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (registro.getIdChofer() != null) {
                ps.setInt(3, registro.getIdChofer());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (registro.getFechaHoraSalidaReal() != null) {
                ps.setTimestamp(4, registro.getFechaHoraSalidaReal());
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            if (registro.getKilometrajeInicial() != null) {
                ps.setInt(5, registro.getKilometrajeInicial());
            } else {
                ps.setNull(5, Types.INTEGER);
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

    public RegistroSalida buscarPorIdViaje(int idViaje) {
        String sql = "SELECT * FROM RegistroSalida WHERE id_viaje = ? ORDER BY id_salida DESC LIMIT 1";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idViaje);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idSalida = rs.getInt("id_salida");

                    int idBusVal = rs.getInt("id_bus");
                    Integer idBus = rs.wasNull() ? null : idBusVal;

                    int idChoferVal = rs.getInt("id_chofer");
                    Integer idChofer = rs.wasNull() ? null : idChoferVal;

                    Timestamp fechaHora = rs.getTimestamp("fecha_hora_salida_real");

                    int kmVal = rs.getInt("kilometraje_inicial");
                    Integer kilometrajeInicial = rs.wasNull() ? null : kmVal;

                    RegistroSalida r = new RegistroSalida();
                    r.setIdSalida(idSalida);
                    r.setIdViaje(idViaje);
                    r.setIdBus(idBus);
                    r.setIdChofer(idChofer);
                    r.setFechaHoraSalidaReal(fechaHora);
                    r.setKilometrajeInicial(kilometrajeInicial);

                    return r;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
