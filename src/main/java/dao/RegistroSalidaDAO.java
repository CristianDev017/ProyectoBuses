package dao;

import modelo.RegistroSalida;
import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
}
