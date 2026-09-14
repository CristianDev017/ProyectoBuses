package dao;

import modelo.Chofer;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoferDAO {

    public int insertarChofer(Chofer c) {
        String sql = "INSERT INTO Chofer (id_sucursal, foto, nombre_completo, numero_licencia, tipo_licencia, fecha_vencimiento, telefono, salario_base_viaje, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (c.getIdSucursal() != null) {
                ps.setInt(1, c.getIdSucursal());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, c.getFoto());
            ps.setString(3, c.getNombreCompleto());
            ps.setString(4, c.getNumeroLicencia());
            ps.setString(5, c.getTipoLicencia());

            if (c.getFechaVencimiento() != null) {
                ps.setDate(6, c.getFechaVencimiento());
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, c.getTelefono());

            if (c.getSalarioBaseViaje() != null) {
                ps.setDouble(8, c.getSalarioBaseViaje());
            } else {
                ps.setNull(8, Types.DOUBLE);
            }

            ps.setString(9, c.getEstado());

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

    public List<Chofer> listarTodos() {
        List<Chofer> choferes = new ArrayList<>();
        String sql = "SELECT * FROM Chofer";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idChofer = rs.getInt("id_chofer");

                int idSucursalVal = rs.getInt("id_sucursal");
                Integer idSucursal = rs.wasNull() ? null : idSucursalVal;

                String foto = rs.getString("foto");
                String nombreCompleto = rs.getString("nombre_completo");
                String numeroLicencia = rs.getString("numero_licencia");
                String tipoLicencia = rs.getString("tipo_licencia");

                Date fecha = rs.getDate("fecha_vencimiento");

                String telefono = rs.getString("telefono");

                double salarioVal = rs.getDouble("salario_base_viaje");
                Double salario = rs.wasNull() ? null : salarioVal;

                String estado = rs.getString("estado");

                Chofer ch = new Chofer(idChofer, idSucursal, foto, nombreCompleto, numeroLicencia, tipoLicencia, fecha, telefono, salario, estado);
                choferes.add(ch);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choferes;
    }
}
