package servlets;

import dao.BusDAO;
import modelo.Bus;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/bus")
public class BusServlet extends HttpServlet {

    private BusDAO busDAO;

    @Override
    public void init() {
        busDAO = new BusDAO();
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
            if (usuarioLogueado.getRol().equalsIgnoreCase(rol)) {
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

        List<Bus> lista = busDAO.listarTodos();
        req.setAttribute("buses", lista);
        req.getRequestDispatcher("/listadoBuses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        Bus bus = new Bus();

        bus.setIdSucursal(usuarioLogueado.getIdSucursal());

        bus.setFoto(req.getParameter("foto"));
        bus.setPlaca(req.getParameter("placa"));
        bus.setMarca(req.getParameter("marca"));
        bus.setModelo(req.getParameter("modelo"));

        String anioStr = req.getParameter("anioFabricacion");
        if (anioStr != null && !anioStr.trim().isEmpty()) {
            try {
                bus.setAnioFabricacion(Integer.parseInt(anioStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String capacidadStr = req.getParameter("capacidad");
        if (capacidadStr != null && !capacidadStr.trim().isEmpty()) {
            try {
                bus.setCapacidad(Integer.parseInt(capacidadStr));
            } catch (NumberFormatException ignored) {
            }
        }

        bus.setEstadoOperativo(req.getParameter("estadoOperativo"));

        String kmStr = req.getParameter("kilometrajeActual");
        if (kmStr != null && !kmStr.trim().isEmpty()) {
            try {
                bus.setKilometrajeActual(Integer.parseInt(kmStr));
            } catch (NumberFormatException ignored) {
            }
        }

        busDAO.insertarBus(bus);

        resp.sendRedirect("bus");
    }
}