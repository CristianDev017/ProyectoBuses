package dao;

import modelo.Boleto;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        String sql = "SELECT b.*, v.fecha_salida, r.precio_boleto, " +
                "so.nombre AS nombre_origen, sd.nombre AS nombre_destino, bus.placa AS placa_bus " +
                "FROM Boleto b " +
                "LEFT JOIN Viaje v ON v.id_viaje = b.id_viaje " +
                "LEFT JOIN Ruta r ON r.id_ruta = v.id_ruta " +
                "LEFT JOIN Sucursal so ON so.id_sucursal = r.id_sucursal_origen " +
                "LEFT JOIN Sucursal sd ON sd.id_sucursal = r.id_sucursal_destino " +
                "LEFT JOIN Bus bus ON bus.id_bus = v.id_bus " +
                "WHERE b.id_usuario = ? ORDER BY b.fecha_pago DESC, b.id_boleto DESC";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Boleto boleto = new Boleto(
                            rs.getInt("id_boleto"), rs.getInt("id_viaje"), rs.getInt("id_usuario"),
                            rs.getInt("numero_asiento"), rs.getDouble("precio"),
                            rs.getDate("fecha_pago"), rs.getString("estado")
                    );
                    String origen = rs.getString("nombre_origen");
                    String destino = rs.getString("nombre_destino");
                    boleto.setRutaDescripcion((origen != null ? origen : "-") + " → " + (destino != null ? destino : "-"));
                    Timestamp fechaSalida = rs.getTimestamp("fecha_salida");
                    if (fechaSalida != null) {
                        boleto.setFechaSalidaTexto(new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaSalida));
                        boleto.setHoraSalidaTexto(new java.text.SimpleDateFormat("HH:mm").format(fechaSalida));
                    }
                    boleto.setPlacaBus(rs.getString("placa_bus"));
                    lista.add(boleto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contarAsientosVendidos(int idViaje) {
        String sql = "SELECT COUNT(*) FROM Boleto WHERE id_viaje = ? AND estado IN ('PAGADO', 'RESERVADO', 'OCUPADO')";
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

    public List<Integer> obtenerAsientosOcupados(int idViaje) {
        List<Integer> ocupados = new ArrayList<>();
        String sql = "SELECT numero_asiento FROM Boleto WHERE id_viaje = ? AND estado IN ('PAGADO', 'RESERVADO', 'OCUPADO')";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idViaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ocupados.add(rs.getInt("numero_asiento"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ocupados;
    }

    public List<Integer> obtenerAsientosDisponibles(int idViaje, int capacidad) {
        List<Integer> disponibles = new ArrayList<>();
        Set<Integer> ocupados = new HashSet<>(obtenerAsientosOcupados(idViaje));
        if (capacidad <= 0) {
            return disponibles;
        }
        for (int asiento = 1; asiento <= capacidad; asiento++) {
            if (!ocupados.contains(asiento)) {
                disponibles.add(asiento);
            }
        }
        return disponibles;
    }

    public int registrarCompraMultiple(int idViaje, int idUsuario, List<Integer> asientos, double precio, Date fechaPago, int idCartera, double nuevoSaldo) {
        if (asientos == null || asientos.isEmpty()) {
            return 0;
        }

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            String sqlCapacidad = "SELECT b.capacidad FROM Viaje v JOIN Bus b ON b.id_bus = v.id_bus WHERE v.id_viaje = ?";
            int capacidad = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlCapacidad)) {
                ps.setInt(1, idViaje);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        capacidad = rs.getInt("capacidad");
                    }
                }
            }
            if (capacidad <= 0) {
                throw new SQLException("No se pudo validar la capacidad del bus.");
            }

            Set<Integer> ocupados = new HashSet<>();
            String sqlOcupados = "SELECT numero_asiento FROM Boleto WHERE id_viaje = ? AND estado IN ('PAGADO', 'RESERVADO', 'OCUPADO')";
            try (PreparedStatement ps = con.prepareStatement(sqlOcupados)) {
                ps.setInt(1, idViaje);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ocupados.add(rs.getInt("numero_asiento"));
                    }
                }
            }

            Set<Integer> seleccion = new HashSet<>();
            for (Integer asiento : asientos) {
                if (asiento == null || asiento < 1 || asiento > capacidad || ocupados.contains(asiento) || !seleccion.add(asiento)) {
                    throw new SQLException("Uno o varios asientos no son válidos para esta compra.");
                }
            }

            String sqlInsert = "INSERT INTO Boleto (id_viaje, id_usuario, numero_asiento, precio, fecha_pago, estado) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                for (Integer asiento : asientos) {
                    psInsert.setInt(1, idViaje);
                    psInsert.setInt(2, idUsuario);
                    psInsert.setInt(3, asiento);
                    psInsert.setDouble(4, precio);
                    psInsert.setDate(5, fechaPago);
                    psInsert.setString(6, "PAGADO");
                    psInsert.executeUpdate();
                }
            }

            String sqlCartera = "UPDATE Cartera SET saldo = ? WHERE id_cartera = ?";
            try (PreparedStatement psCartera = con.prepareStatement(sqlCartera)) {
                psCartera.setDouble(1, nuevoSaldo);
                psCartera.setInt(2, idCartera);
                psCartera.executeUpdate();
            }

            con.commit();
            return asientos.size();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int comprarBoletoTransaccional(Boleto b, int idCartera, double nuevoSaldo) {
        return registrarCompraMultiple(b.getIdViaje(), b.getIdUsuario(), List.of(b.getNumeroAsiento()), b.getPrecio(), b.getFechaPago(), idCartera, nuevoSaldo);
    }
}