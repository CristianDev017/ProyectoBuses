package dao;

import modelo.Viaje;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViajeDAO {

    public Viaje buscarPorId(int idViaje) {
        String sql = "SELECT * FROM Viaje WHERE id_viaje = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idViaje);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idBusVal = rs.getInt("id_bus");
                    Integer idBus = rs.wasNull() ? null : idBusVal;

                    int idChoferVal = rs.getInt("id_chofer");
                    Integer idChofer = rs.wasNull() ? null : idChoferVal;

                    int idRutaVal = rs.getInt("id_ruta");
                    Integer idRuta = rs.wasNull() ? null : idRutaVal;

                    return new Viaje(
                            rs.getInt("id_viaje"),
                            idBus,
                            idChofer,
                            idRuta,
                            rs.getString("tipo_viaje"),
                            rs.getTimestamp("fecha_salida"),
                            rs.getTimestamp("fecha_llegada_estimada"),
                            rs.getString("estado")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizarEstado(int idViaje, String estado) {
        String sql = "UPDATE Viaje SET estado = ? WHERE id_viaje = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idViaje);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int insertarViaje(Viaje v) {
        String sql = "INSERT INTO Viaje (id_bus, id_chofer, id_ruta, tipo_viaje, fecha_salida, fecha_llegada_estimada, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (v.getIdBus() != null) {
                ps.setInt(1, v.getIdBus());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (v.getIdChofer() != null) {
                ps.setInt(2, v.getIdChofer());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (v.getIdRuta() != null) {
                ps.setInt(3, v.getIdRuta());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, v.getTipoViaje());

            if (v.getFechaSalida() != null) {
                ps.setTimestamp(5, v.getFechaSalida());
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            if (v.getFechaLlegadaEstimada() != null) {
                ps.setTimestamp(6, v.getFechaLlegadaEstimada());
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }

            ps.setString(7, v.getEstado());

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

    public List<Viaje> listarTodos() {
        List<Viaje> viajes = new ArrayList<>();
        String sql = "SELECT * FROM Viaje";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idViaje = rs.getInt("id_viaje");

                int idBusVal = rs.getInt("id_bus");
                Integer idBus = rs.wasNull() ? null : idBusVal;

                int idChoferVal = rs.getInt("id_chofer");
                Integer idChofer = rs.wasNull() ? null : idChoferVal;

                int idRutaVal = rs.getInt("id_ruta");
                Integer idRuta = rs.wasNull() ? null : idRutaVal;

                String tipo = rs.getString("tipo_viaje");

                Timestamp fechaSalida = rs.getTimestamp("fecha_salida");
                Timestamp fechaLlegada = rs.getTimestamp("fecha_llegada_estimada");

                String estado = rs.getString("estado");

                Viaje v = new Viaje(idViaje, idBus, idChofer, idRuta, tipo, fechaSalida, fechaLlegada, estado);
                viajes.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return viajes;
    }

    public boolean tieneViajesActivos(int idBus) {
        String sql = "SELECT COUNT(*) FROM Viaje WHERE id_bus = ? AND estado IN ('PROGRAMADO', 'EN_TRANSITO')";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
