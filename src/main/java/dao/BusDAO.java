package dao;

import modelo.Bus;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    public int insertarBus(Bus b) {
        String sql = "INSERT INTO Bus (id_sucursal, foto, placa, marca, modelo, anio_fabricacion, capacidad, estado_operativo, kilometraje_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (b.getIdSucursal() != null) {
                ps.setInt(1, b.getIdSucursal());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, b.getFoto());
            ps.setString(3, b.getPlaca());
            ps.setString(4, b.getMarca());
            ps.setString(5, b.getModelo());

            if (b.getAnioFabricacion() != null) {
                ps.setInt(6, b.getAnioFabricacion());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (b.getCapacidad() != null) {
                ps.setInt(7, b.getCapacidad());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setString(8, b.getEstadoOperativo());

            if (b.getKilometrajeActual() != null) {
                ps.setInt(9, b.getKilometrajeActual());
            } else {
                ps.setNull(9, Types.INTEGER);
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

    public Bus buscarPorId(int idBus) {
        String sql = "SELECT * FROM Bus WHERE id_bus = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBus);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idSucursalVal = rs.getInt("id_sucursal");
                    Integer idSucursal = rs.wasNull() ? null : idSucursalVal;

                    String foto = rs.getString("foto");
                    String placa = rs.getString("placa");
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");

                    int anioVal = rs.getInt("anio_fabricacion");
                    Integer anio = rs.wasNull() ? null : anioVal;

                    int capacidadVal = rs.getInt("capacidad");
                    Integer capacidad = rs.wasNull() ? null : capacidadVal;

                    String estadoOperativo = rs.getString("estado_operativo");

                    int kmVal = rs.getInt("kilometraje_actual");
                    Integer kilometrajeActual = rs.wasNull() ? null : kmVal;

                    return new Bus(idBus, idSucursal, foto, placa, marca, modelo, anio, capacidad, estadoOperativo, kilometrajeActual);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Bus> listarTodos() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM Bus";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idBus = rs.getInt("id_bus");

                int idSucursalVal = rs.getInt("id_sucursal");
                Integer idSucursal = rs.wasNull() ? null : idSucursalVal;

                String foto = rs.getString("foto");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");

                int anioVal = rs.getInt("anio_fabricacion");
                Integer anio = rs.wasNull() ? null : anioVal;

                int capacidadVal = rs.getInt("capacidad");
                Integer capacidad = rs.wasNull() ? null : capacidadVal;

                String estadoOperativo = rs.getString("estado_operativo");

                int kmVal = rs.getInt("kilometraje_actual");
                Integer kilometrajeActual = rs.wasNull() ? null : kmVal;

                Bus bus = new Bus(idBus, idSucursal, foto, placa, marca, modelo, anio, capacidad, estadoOperativo, kilometrajeActual);
                buses.add(bus);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buses;
    }

    public boolean actualizarKilometraje(int idBus, int nuevoKilometraje) {
        String sql = "UPDATE Bus SET kilometraje_actual = ? WHERE id_bus = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoKilometraje);
            ps.setInt(2, idBus);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
