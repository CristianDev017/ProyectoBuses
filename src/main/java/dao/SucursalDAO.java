package dao;

import modelo.Sucursal;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;

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

        } catch (SQLIntegrityConstraintViolationException e) {
            ultimoError = "Ya existe una sucursal con ese nombre.";
        } catch (SQLException e) {
            e.printStackTrace();
            ultimoError = "Error al guardar la sucursal.";
        }

        return 0;
    }

    private String ultimoError;

    public String getUltimoError() {
        return ultimoError;
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

    public Sucursal buscarPorId(int idSucursal) {
        String sql = "SELECT * FROM Sucursal WHERE id_sucursal = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sucursal(rs.getInt("id_sucursal"), rs.getString("nombre"),
                            rs.getString("direccion"), rs.getString("telefono"), rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizar(Sucursal s) {
        String sql = "UPDATE Sucursal SET nombre=?, direccion=?, telefono=?, estado=? WHERE id_sucursal=?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getNombre());
            ps.setString(2, s.getDireccion());
            ps.setString(3, s.getTelefono());
            ps.setString(4, s.getEstado());
            ps.setInt(5, s.getIdSucursal());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            ultimoError = "Ya existe una sucursal con ese nombre.";
        } catch (SQLException e) {
            e.printStackTrace();
            ultimoError = "Error al actualizar la sucursal.";
        }
        return false;
    }
}
