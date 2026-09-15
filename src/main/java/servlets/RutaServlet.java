package servlets;

import dao.RutaDAO;
import dao.SucursalDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.Ruta;
import modelo.Sucursal;
import modelo.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/ruta")
public class RutaServlet extends HttpServlet {

    private RutaDAO rutaDAO;
    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
        rutaDAO = new RutaDAO();
        sucursalDAO = new SucursalDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp, String... rolesPermitidos) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }

        boolean tienePermiso = false;
        for (String rol : rolesPermitidos) {
            if (usuarioLogueado.getRol() != null && usuarioLogueado.getRol().equalsIgnoreCase(rol)) {
                tienePermiso = true;
                break;
            }
        }

        if (!tienePermiso) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }

        return usuarioLogueado;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        List<Sucursal> sucursales = sucursalDAO.listarTodas();
        req.setAttribute("sucursales", sucursales);

        String accion = req.getParameter("accion");
        if ("nuevo".equalsIgnoreCase(accion)) {
            req.getRequestDispatcher("/registrarRuta.jsp").forward(req, resp);
            return;
        }

        List<Ruta> rutas = rutaDAO.listarTodas();
        req.setAttribute("rutas", rutas);
        req.getRequestDispatcher("/listadoRutas.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        Ruta ruta = new Ruta();
        ruta.setIdSucursalOrigen(usuarioLogueado.getIdSucursal());

        String idDestinoStr = req.getParameter("idSucursalDestino");
        if (idDestinoStr != null && !idDestinoStr.trim().isEmpty()) {
            try {
                ruta.setIdSucursalDestino(Integer.parseInt(idDestinoStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String distanciaStr = req.getParameter("distanciaKm");
        if (distanciaStr != null && !distanciaStr.trim().isEmpty()) {
            try {
                ruta.setDistanciaKm(Double.parseDouble(distanciaStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String precioStr = req.getParameter("precioBoleto");
        if (precioStr != null && !precioStr.trim().isEmpty()) {
            try {
                ruta.setPrecioBoleto(Double.parseDouble(precioStr));
            } catch (NumberFormatException ignored) {
            }
        }

        ruta.setEstado(req.getParameter("estado"));

        rutaDAO.insertarRuta(ruta);

        resp.sendRedirect("ruta");
    }
}
