package dao;

import modelo.Sucursal;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SucursalDAO {

    public int insertarSucursal(Sucursal s) {
        String sql = "INSERT INTO Sucursal (nombre, direccion, telefono, estado) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            ps.setString(3, s.getTelefono());
            ps.setString(4, s.getEstado());

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

    public List<Sucursal> listarTodas() {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT * FROM Sucursal";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idSucursal = rs.getInt("id_sucursal");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                String estado = rs.getString("estado");

                Sucursal sucursal = new Sucursal(idSucursal, nombre, direccion, telefono, estado);
                sucursales.add(sucursal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sucursales;
    }
}
