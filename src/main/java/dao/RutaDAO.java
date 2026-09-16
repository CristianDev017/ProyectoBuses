package dao;

import modelo.Ruta;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

    public int insertarRuta(Ruta r) {
        String sql = "INSERT INTO Ruta (id_sucursal_origen, id_sucursal_destino, distancia_km, precio_boleto, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (r.getIdSucursalOrigen() != null) {
                ps.setInt(1, r.getIdSucursalOrigen());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (r.getIdSucursalDestino() != null) {
                ps.setInt(2, r.getIdSucursalDestino());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (r.getDistanciaKm() != null) {
                ps.setDouble(3, r.getDistanciaKm());
            } else {
                ps.setNull(3, Types.DOUBLE);
            }

            if (r.getPrecioBoleto() != null) {
                ps.setDouble(4, r.getPrecioBoleto());
            } else {
                ps.setNull(4, Types.DOUBLE);
            }

            ps.setString(5, r.getEstado());

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

    public List<Ruta> listarTodas() {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM Ruta";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idRuta = rs.getInt("id_ruta");

                int origenVal = rs.getInt("id_sucursal_origen");
                Integer idSucursalOrigen = rs.wasNull() ? null : origenVal;

                int destinoVal = rs.getInt("id_sucursal_destino");
                Integer idSucursalDestino = rs.wasNull() ? null : destinoVal;

                double distanciaVal = rs.getDouble("distancia_km");
                Double distanciaKm = rs.wasNull() ? null : distanciaVal;

                double precioVal = rs.getDouble("precio_boleto");
                Double precioBoleto = rs.wasNull() ? null : precioVal;

                String estado = rs.getString("estado");

                Ruta ruta = new Ruta(idRuta, idSucursalOrigen, idSucursalDestino, distanciaKm, precioBoleto, estado);
                rutas.add(ruta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rutas;
    }

    public Ruta buscarPorId(int idRuta) {
        String sql = "SELECT * FROM Ruta WHERE id_ruta = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRuta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int origenVal = rs.getInt("id_sucursal_origen");
                    Integer idSucursalOrigen = rs.wasNull() ? null : origenVal;

                    int destinoVal = rs.getInt("id_sucursal_destino");
                    Integer idSucursalDestino = rs.wasNull() ? null : destinoVal;

                    double distanciaVal = rs.getDouble("distancia_km");
                    Double distanciaKm = rs.wasNull() ? null : distanciaVal;

                    double precioVal = rs.getDouble("precio_boleto");
                    Double precioBoleto = rs.wasNull() ? null : precioVal;

                    String estado = rs.getString("estado");

                    return new Ruta(idRuta, idSucursalOrigen, idSucursalDestino, distanciaKm, precioBoleto, estado);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

