package servlets;

import dao.ConfiguracionSistemaDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/configuracion")
public class ConfiguracionServlet extends HttpServlet {

    private ConfiguracionSistemaDAO configuracionDAO;

    @Override
    public void init() {
        configuracionDAO = new ConfiguracionSistemaDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null || !u.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        Double monto = configuracionDAO.obtenerMontoDepreciacion();
        req.setAttribute("monto", monto);
        req.getRequestDispatcher("/configuracion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        String montoStr = req.getParameter("monto");
        String fechaStr = req.getParameter("fecha");

        if (montoStr != null && !montoStr.trim().isEmpty() && fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                double monto = Double.parseDouble(montoStr);
                Date fecha = Date.valueOf(fechaStr);
                configuracionDAO.actualizarMonto(monto, fecha);
            } catch (IllegalArgumentException ignored) {
            }
        }

        resp.sendRedirect(req.getContextPath() + "/configuracion");
    }
}