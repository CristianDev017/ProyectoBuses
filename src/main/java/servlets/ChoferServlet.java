package servlets;

import dao.ChoferDAO;
import dao.ViajeDAO;
import modelo.Chofer;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/chofer")
public class ChoferServlet extends HttpServlet {

    private ChoferDAO choferDAO;
    private ViajeDAO viajeDAO;

    @Override
    public void init() {
        choferDAO = new ChoferDAO();
        viajeDAO = new ViajeDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp, String requiredRole) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuarioLogueado == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        if (requiredRole != null && (usuarioLogueado.getRol() == null || !usuarioLogueado.getRol().equalsIgnoreCase(requiredRole))) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        return usuarioLogueado;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        String accion = req.getParameter("accion");
        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Chofer choferEditar = choferDAO.buscarPorId(Integer.parseInt(idParam));
                    if (choferEditar == null || choferEditar.getIdSucursal() == null || !choferEditar.getIdSucursal().equals(usuarioLogueado.getIdSucursal())) {
                        resp.sendRedirect(req.getContextPath() + "/chofer");
                        return;
                    }
                    req.setAttribute("choferEditar", choferEditar);
                    req.getRequestDispatcher("/registrarChofer.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        List<Chofer> lista = choferDAO.listarPorSucursal(usuarioLogueado.getIdSucursal());
        req.setAttribute("choferes", lista);
        req.getRequestDispatcher("/listadoChoferes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        Chofer ch = new Chofer();
        ch.setIdSucursal(usuarioLogueado.getIdSucursal());

        ch.setFoto(req.getParameter("foto"));
        ch.setNombreCompleto(req.getParameter("nombreCompleto"));
        ch.setNumeroLicencia(req.getParameter("numeroLicencia"));
        ch.setTipoLicencia(req.getParameter("tipoLicencia"));

        String fechaStr = req.getParameter("fechaVencimiento");
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                Date fecha = Date.valueOf(fechaStr);
                ch.setFechaVencimiento(fecha);
            } catch (IllegalArgumentException ignored) {
            }
        }

        ch.setTelefono(req.getParameter("telefono"));

        String salarioStr = req.getParameter("salarioBaseViaje");
        if (salarioStr != null && !salarioStr.trim().isEmpty()) {
            try {
                ch.setSalarioBaseViaje(Double.parseDouble(salarioStr));
            } catch (NumberFormatException ignored) {
            }
        }

        ch.setEstado(req.getParameter("estado"));

        String idParam = req.getParameter("idChofer");
        if (idParam != null && !idParam.trim().isEmpty()) {
            ch.setIdChofer(Integer.parseInt(idParam));

            // Verificar que el chofer a actualizar pertenezca a la sucursal del usuario
            Chofer existente = choferDAO.buscarPorId(ch.getIdChofer());
            if (existente == null || existente.getIdSucursal() == null || !existente.getIdSucursal().equals(usuarioLogueado.getIdSucursal())) {
                resp.sendRedirect(req.getContextPath() + "/chofer");
                return;
            }

            if ("INACTIVO".equalsIgnoreCase(ch.getEstado()) && viajeDAO.tieneViajesActivosParaChofer(ch.getIdChofer())) {
                req.setAttribute("error", "No se puede desactivar el chofer: tiene viajes programados o en tránsito.");
                req.setAttribute("choferEditar", ch);
                req.getRequestDispatcher("/registrarChofer.jsp").forward(req, resp);
                return;
            }

            boolean exito = choferDAO.actualizar(ch);

            if (!exito && choferDAO.getUltimoError() != null) {
                req.setAttribute("error", choferDAO.getUltimoError());
                req.setAttribute("choferEditar", ch);
                req.getRequestDispatcher("/registrarChofer.jsp").forward(req, resp);
                return;
            }
        } else {
            if (ch.getEstado() == null || ch.getEstado().trim().isEmpty()) {
                ch.setEstado("ACTIVO");
            }
            choferDAO.insertarChofer(ch);
        }

        resp.sendRedirect("chofer");
    }
}
