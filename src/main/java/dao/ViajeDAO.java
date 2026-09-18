package dao;

import modelo.Bus;
import modelo.Ruta;
import modelo.Sucursal;
import modelo.Viaje;
import modelo.ViajeDetalle;
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

    public List<ViajeDetalle> listarRegularesDisponibles() {
        List<ViajeDetalle> viajes = new ArrayList<>();
        String sql = "SELECT v.*, r.id_sucursal_origen, r.id_sucursal_destino, r.precio_boleto, " +
               "b.placa, b.capacidad, so.nombre AS nombre_origen, sd.nombre AS nombre_destino " +
               "FROM Viaje v " +
               "LEFT JOIN Ruta r ON r.id_ruta = v.id_ruta " +
               "LEFT JOIN Bus b ON b.id_bus = v.id_bus " +
               "LEFT JOIN Sucursal so ON so.id_sucursal = r.id_sucursal_origen " +
               "LEFT JOIN Sucursal sd ON sd.id_sucursal = r.id_sucursal_destino " +
               "WHERE v.tipo_viaje = 'REGULAR' AND v.estado = 'PROGRAMADO' " +
               "ORDER BY v.fecha_salida ASC";

        BoletoDAO boletoDAO = new BoletoDAO();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
               Viaje viaje = new Viaje(
                       rs.getInt("id_viaje"),
                       rs.getObject("id_bus") == null ? null : rs.getInt("id_bus"),
                       rs.getObject("id_chofer") == null ? null : rs.getInt("id_chofer"),
                       rs.getObject("id_ruta") == null ? null : rs.getInt("id_ruta"),
                       rs.getString("tipo_viaje"),
                       rs.getTimestamp("fecha_salida"),
                       rs.getTimestamp("fecha_llegada_estimada"),
                       rs.getString("estado")
               );

               Ruta ruta = new Ruta();
               if (rs.getObject("id_ruta") != null) {
                   ruta.setIdRuta(rs.getInt("id_ruta"));
                   ruta.setIdSucursalOrigen(rs.getObject("id_sucursal_origen") == null ? null : rs.getInt("id_sucursal_origen"));
                   ruta.setIdSucursalDestino(rs.getObject("id_sucursal_destino") == null ? null : rs.getInt("id_sucursal_destino"));
                   ruta.setPrecioBoleto(rs.getObject("precio_boleto") == null ? null : rs.getDouble("precio_boleto"));
               }

               Bus bus = new Bus();
               if (rs.getObject("id_bus") != null) {
                   bus.setIdBus(rs.getInt("id_bus"));
                   bus.setPlaca(rs.getString("placa"));
                   bus.setCapacidad(rs.getObject("capacidad") == null ? null : rs.getInt("capacidad"));
               }

               Sucursal origen = new Sucursal();
               if (rs.getObject("id_sucursal_origen") != null) {
                   origen.setIdSucursal(rs.getInt("id_sucursal_origen"));
                   origen.setNombre(rs.getString("nombre_origen"));
               }

               Sucursal destino = new Sucursal();
               if (rs.getObject("id_sucursal_destino") != null) {
                   destino.setIdSucursal(rs.getInt("id_sucursal_destino"));
                   destino.setNombre(rs.getString("nombre_destino"));
               }

               ViajeDetalle detalle = new ViajeDetalle();
               detalle.setViaje(viaje);
               detalle.setRuta(ruta);
               detalle.setBus(bus);
               detalle.setOrigen(origen);
               detalle.setDestino(destino);
               detalle.setRutaDescripcion((origen.getNombre() != null ? origen.getNombre() : "-") + " → " + (destino.getNombre() != null ? destino.getNombre() : "-"));
               int capacidad = bus.getCapacidad() != null ? bus.getCapacidad() : 0;
               detalle.setAsientosDisponibles(Math.max(0, capacidad - boletoDAO.contarAsientosVendidos(viaje.getIdViaje())));
               Timestamp fechaSalida = viaje.getFechaSalida();
               if (fechaSalida != null) {
                   detalle.setFechaSalidaTexto(new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaSalida));
                   detalle.setHoraSalidaTexto(new java.text.SimpleDateFormat("HH:mm").format(fechaSalida));
               }
               viajes.add(detalle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return viajes;
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
        String sql = "SELECT v.*, c.nombre_completo AS nombre_chofer " +
                    "FROM Viaje v LEFT JOIN Chofer c ON c.id_chofer = v.id_chofer";

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
               String nombreChofer = rs.getString("nombre_chofer");

                Viaje v = new Viaje(idViaje, idBus, idChofer, idRuta, tipo, fechaSalida, fechaLlegada, estado);
               v.setNombreChofer(nombreChofer);
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
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tieneViajesActivosParaChofer(int idChofer) {
        String sql = "SELECT COUNT(*) FROM Viaje WHERE id_chofer = ? AND estado IN ('PROGRAMADO', 'EN_TRANSITO')";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idChofer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tieneViajesActivosParaRuta(int idRuta) {
        String sql = "SELECT COUNT(*) FROM Viaje WHERE id_ruta = ? AND estado IN ('PROGRAMADO', 'EN_TRANSITO')";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
