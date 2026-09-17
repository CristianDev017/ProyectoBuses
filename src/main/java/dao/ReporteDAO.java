package dao;

import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<Object[]> reporteGanancias(String fechaInicio, String fechaFin, Integer idSucursal) {
        List<Object[]> resultado = new ArrayList<>();
        Date desde = Date.valueOf(fechaInicio != null && !fechaInicio.isEmpty() ? fechaInicio : "1970-01-01");
        Date hasta = Date.valueOf(fechaFin != null && !fechaFin.isEmpty() ? fechaFin : "2100-01-01");

        String sql = "SELECT s.id_sucursal, s.nombre, " +
                "COALESCE(ib.total,0) AS ingresos_boletos, " +
                "COALESCE(ic.total_comb,0) AS costo_combustible, " +
                "COALESCE(im.total_mano,0) + COALESCE(im.total_rep,0) AS costo_taller, " +
                "COALESCE(idp.total_dep,0) AS costo_depreciacion " +
                "FROM Sucursal s " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(bo.precio) AS total FROM Boleto bo " +
                "  JOIN Viaje v ON v.id_viaje=bo.id_viaje JOIN Bus b ON b.id_bus=v.id_bus " +
                "  WHERE bo.fecha_pago BETWEEN ? AND ? GROUP BY b.id_sucursal) ib ON ib.id_sucursal=s.id_sucursal " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(rl.gasto_combustible) AS total_comb FROM RegistroLlegada rl " +
                "  JOIN Viaje v ON v.id_viaje=rl.id_viaje JOIN Bus b ON b.id_bus=v.id_bus " +
                "  WHERE rl.fecha_hora_llegada_real BETWEEN ? AND ? GROUP BY b.id_sucursal) ic ON ic.id_sucursal=s.id_sucursal " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(m.monto_mano_obra) AS total_mano, SUM(m.monto_repuestos) AS total_rep FROM Mantenimiento m " +
                "  JOIN Bus b ON b.id_bus=m.id_bus WHERE m.fecha_mantenimiento BETWEEN ? AND ? GROUP BY b.id_sucursal) im ON im.id_sucursal=s.id_sucursal " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(rl.monto_depreciacion) AS total_dep FROM RegistroLlegada rl " +
                "  JOIN Viaje v ON v.id_viaje=rl.id_viaje JOIN Bus b ON b.id_bus=v.id_bus " +
                "  WHERE rl.fecha_hora_llegada_real BETWEEN ? AND ? GROUP BY b.id_sucursal) idp ON idp.id_sucursal=s.id_sucursal " +
                (idSucursal != null ? "WHERE s.id_sucursal = ? " : "") +
                "ORDER BY s.nombre";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            for (int j = 0; j < 4; j++) {
                ps.setDate(i++, desde);
                ps.setDate(i++, hasta);
            }
            if (idSucursal != null) ps.setInt(i, idSucursal);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double ingresos = rs.getDouble("ingresos_boletos");
                    double costos = rs.getDouble("costo_combustible") + rs.getDouble("costo_taller") + rs.getDouble("costo_depreciacion");
                    double ganancia = ingresos - costos;

                    resultado.add(new Object[]{
                            rs.getString("nombre"),
                            String.format("Q%.2f", ingresos),
                            String.format("Q%.2f", costos),
                            String.format("Q%.2f", ganancia)
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public List<Object[]> reporteRutasDemandadas(String fechaInicio, String fechaFin) {
        List<Object[]> resultado = new ArrayList<>();
        Date desde = Date.valueOf(fechaInicio != null && !fechaInicio.isEmpty() ? fechaInicio : "1970-01-01");
        Date hasta = Date.valueOf(fechaFin != null && !fechaFin.isEmpty() ? fechaFin : "2100-01-01");

        String sql = "SELECT r.id_ruta, r.distancia_km, r.precio_boleto, COUNT(bo.id_boleto) AS total_vendidos " +
                "FROM Ruta r " +
                "JOIN Viaje v ON v.id_ruta = r.id_ruta " +
                "JOIN Boleto bo ON bo.id_viaje = v.id_viaje " +
                "WHERE bo.fecha_pago BETWEEN ? AND ? " +
                "GROUP BY r.id_ruta, r.distancia_km, r.precio_boleto " +
                "ORDER BY total_vendidos DESC";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, desde);
            ps.setDate(2, hasta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[]{
                            rs.getInt("id_ruta"),
                            rs.getDouble("distancia_km") + " km",
                            String.format("Q%.2f", rs.getDouble("precio_boleto")),
                            rs.getInt("total_vendidos")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Object[]> reporteCostosOperativos(String fechaInicio, String fechaFin, Integer idSucursal) {
        List<Object[]> resultado = new ArrayList<>();
        Date desde = Date.valueOf(fechaInicio != null && !fechaInicio.isEmpty() ? fechaInicio : "1970-01-01");
        Date hasta = Date.valueOf(fechaFin != null && !fechaFin.isEmpty() ? fechaFin : "2100-01-01");

        String sql = "SELECT s.nombre, " +
                "COALESCE(ic.total_comb,0) AS combustible, " +
                "COALESCE(im.total_mano,0) AS mano_obra, " +
                "COALESCE(im.total_rep,0) AS repuestos, " +
                "COALESCE(idp.total_dep,0) AS depreciacion " +
                "FROM Sucursal s " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(rl.gasto_combustible) AS total_comb FROM RegistroLlegada rl " +
                "  JOIN Viaje v ON v.id_viaje=rl.id_viaje JOIN Bus b ON b.id_bus=v.id_bus " +
                "  WHERE rl.fecha_hora_llegada_real BETWEEN ? AND ? GROUP BY b.id_sucursal) ic ON ic.id_sucursal=s.id_sucursal " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(m.monto_mano_obra) AS total_mano, SUM(m.monto_repuestos) AS total_rep FROM Mantenimiento m " +
                "  JOIN Bus b ON b.id_bus=m.id_bus WHERE m.fecha_mantenimiento BETWEEN ? AND ? GROUP BY b.id_sucursal) im ON im.id_sucursal=s.id_sucursal " +
                "LEFT JOIN (SELECT b.id_sucursal, SUM(rl.monto_depreciacion) AS total_dep FROM RegistroLlegada rl " +
                "  JOIN Viaje v ON v.id_viaje=rl.id_viaje JOIN Bus b ON b.id_bus=v.id_bus " +
                "  WHERE rl.fecha_hora_llegada_real BETWEEN ? AND ? GROUP BY b.id_sucursal) idp ON idp.id_sucursal=s.id_sucursal " +
                (idSucursal != null ? "WHERE s.id_sucursal = ? " : "") +
                "ORDER BY s.nombre";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            for (int j = 0; j < 3; j++) {
                ps.setDate(i++, desde);
                ps.setDate(i++, hasta);
            }
            if (idSucursal != null) ps.setInt(i, idSucursal);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double comb = rs.getDouble("combustible");
                    double mano = rs.getDouble("mano_obra");
                    double rep = rs.getDouble("repuestos");
                    double dep = rs.getDouble("depreciacion");
                    double total = comb + mano + rep + dep;

                    resultado.add(new Object[]{
                            rs.getString("nombre"),
                            String.format("Q%.2f", comb),
                            String.format("Q%.2f", mano + rep),
                            String.format("Q%.2f", dep),
                            String.format("Q%.2f", total)
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Object[]> listadoBuses(int idSucursal) {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT b.placa, b.marca, b.modelo, b.capacidad, b.estado_operativo, " +
                "(SELECT c.nombre_completo FROM Chofer c JOIN Viaje v ON v.id_chofer=c.id_chofer " +
                " WHERE v.id_bus=b.id_bus AND v.estado='EN_TRANSITO' LIMIT 1) AS chofer_actual, " +
                "b.kilometraje_actual, " +
                "(SELECT COUNT(*) FROM Viaje v WHERE v.id_bus=b.id_bus) AS total_viajes " +
                "FROM Bus b WHERE b.id_sucursal = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[]{
                            rs.getString("placa"),
                            rs.getString("marca") + " " + rs.getString("modelo"),
                            rs.getInt("capacidad"),
                            rs.getString("estado_operativo"),
                            rs.getString("chofer_actual") != null ? rs.getString("chofer_actual") : "N/A",
                            rs.getInt("kilometraje_actual"),
                            rs.getInt("total_viajes")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Object[]> listadoChoferes(int idSucursal) {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT c.numero_licencia, c.nombre_completo, c.tipo_licencia, c.fecha_vencimiento, c.estado, " +
                "(SELECT COUNT(*) FROM Viaje v WHERE v.id_chofer=c.id_chofer) AS total_viajes " +
                "FROM Chofer c WHERE c.id_sucursal = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[]{
                            rs.getString("numero_licencia"),
                            rs.getString("nombre_completo"),
                            rs.getString("tipo_licencia"),
                            rs.getDate("fecha_vencimiento"),
                            rs.getString("estado"),
                            rs.getInt("total_viajes")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Object[]> reporteIngresosBoletos(int idSucursal, String fechaInicio, String fechaFin) {
        List<Object[]> resultado = new ArrayList<>();
        Date desde = Date.valueOf(fechaInicio != null && !fechaInicio.isEmpty() ? fechaInicio : "1970-01-01");
        Date hasta = Date.valueOf(fechaFin != null && !fechaFin.isEmpty() ? fechaFin : "2100-01-01");

        String sql = "SELECT v.id_viaje, r.id_ruta, bo.fecha_pago, COUNT(bo.id_boleto) AS cantidad, SUM(bo.precio) AS ingreso " +
                "FROM Boleto bo " +
                "JOIN Viaje v ON v.id_viaje = bo.id_viaje " +
                "JOIN Ruta r ON r.id_ruta = v.id_ruta " +
                "JOIN Bus b ON b.id_bus = v.id_bus " +
                "WHERE b.id_sucursal = ? AND bo.fecha_pago BETWEEN ? AND ? " +
                "GROUP BY v.id_viaje, r.id_ruta, bo.fecha_pago";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            ps.setDate(2, desde);
            ps.setDate(3, hasta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[]{
                            rs.getInt("id_viaje"),
                            rs.getInt("id_ruta"),
                            rs.getDate("fecha_pago"),
                            rs.getInt("cantidad"),
                            String.format("Q%.2f", rs.getDouble("ingreso"))
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}