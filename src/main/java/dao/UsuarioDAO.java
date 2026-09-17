package dao;

import modelo.Usuario;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;

public class UsuarioDAO {

    public int insertarUsuario(Usuario u) {
        String sql = "INSERT INTO Usuario " +
                "(id_sucursal, dpi, nombre_completo, nit, telefono, direccion, rol, estado, correo, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (u.getIdSucursal() != null) {
                ps.setInt(1, u.getIdSucursal());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, u.getDpi());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getNit());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getDireccion());
            ps.setString(7, u.getRol());
            ps.setString(8, u.getEstado());
            ps.setString(9, u.getCorreo());
            ps.setString(10, u.getPassword());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            ultimoError = "Ya existe un usuario con ese DPI, NIT o correo.";
        } catch (SQLException e) {
            e.printStackTrace();
            ultimoError = "Error al guardar el usuario.";
        }

        return 0;
    }

    private String ultimoError;

    public String getUltimoError() {
        return ultimoError;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT * FROM Usuario";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idUsuario = rs.getInt("id_usuario");

                int idSucursalValor = rs.getInt("id_sucursal");
                Integer idSucursal = rs.wasNull() ? null : idSucursalValor;

                String dpi = rs.getString("dpi");
                String nombreCompleto = rs.getString("nombre_completo");
                String nit = rs.getString("nit");
                String telefono = rs.getString("telefono");
                String direccion = rs.getString("direccion");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");
                String correo = rs.getString("correo");
                String password = rs.getString("password");

                Usuario usuario = new Usuario(
                        idUsuario,
                        idSucursal,
                        dpi,
                        nombreCompleto,
                        nit,
                        telefono,
                        direccion,
                        rol,
                        estado,
                        correo,
                        password
                );

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public Usuario buscarPorId(int idUsuario) {

        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int idSucursalValor = rs.getInt("id_sucursal");
                    Integer idSucursal = rs.wasNull() ? null : idSucursalValor;

                    return new Usuario(
                            rs.getInt("id_usuario"),
                            idSucursal,
                            rs.getString("dpi"),
                            rs.getString("nombre_completo"),
                            rs.getString("nit"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("rol"),
                            rs.getString("estado"),
                            rs.getString("correo"),
                            rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE Usuario SET id_sucursal=?, dpi=?, nombre_completo=?, nit=?, telefono=?, direccion=?, rol=?, estado=?, correo=? WHERE id_usuario=?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (u.getIdSucursal() != null) {
                ps.setInt(1, u.getIdSucursal());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, u.getDpi());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getNit());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getDireccion());
            ps.setString(7, u.getRol());
            ps.setString(8, u.getEstado());
            ps.setString(9, u.getCorreo());
            ps.setInt(10, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            ultimoError = "Ya existe otro usuario con ese DPI, NIT o correo.";
        } catch (SQLException e) {
            e.printStackTrace();
            ultimoError = "Error al actualizar el usuario.";
        }
        return false;
    }

    public boolean cambiarEstado(int idUsuario, String estado) {

        String sql = "UPDATE Usuario SET estado = ? WHERE id_usuario = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Usuario login(String correo, String password) {
        String sql = "SELECT * FROM Usuario WHERE correo = ? AND password = ? AND estado = 'ACTIVO'";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idSucursalValor = rs.getInt("id_sucursal");
                    Integer idSucursal = rs.wasNull() ? null : idSucursalValor;

                    return new Usuario(
                            rs.getInt("id_usuario"),
                            idSucursal,
                            rs.getString("dpi"),
                            rs.getString("nombre_completo"),
                            rs.getString("nit"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("rol"),
                            rs.getString("estado"),
                            rs.getString("correo"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}