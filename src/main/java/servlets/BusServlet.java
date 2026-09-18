package servlets;

import dao.BusDAO;
import dao.ViajeDAO;
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
    private ViajeDAO viajeDAO;

    @Override
    public void init() {
        busDAO = new BusDAO();
        viajeDAO = new ViajeDAO();
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

        String accion = req.getParameter("accion");
        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Bus busEditar = busDAO.buscarPorId(Integer.parseInt(idParam));
                    if (busEditar == null || busEditar.getIdSucursal() == null || !busEditar.getIdSucursal().equals(usuarioLogueado.getIdSucursal())) {
                        resp.sendRedirect(req.getContextPath() + "/bus");
                        return;
                    }
                    req.setAttribute("busEditar", busEditar);
                    req.getRequestDispatcher("/registrarBus.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        List<Bus> lista = busDAO.listarPorSucursal(usuarioLogueado.getIdSucursal());
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

        String idParam = req.getParameter("idBus");
        if (idParam != null && !idParam.trim().isEmpty()) {
            bus.setIdBus(Integer.parseInt(idParam));

            // Verificar que el bus que se intenta actualizar pertenezca a la sucursal del usuario
            Bus existente = busDAO.buscarPorId(bus.getIdBus());
            if (existente == null || existente.getIdSucursal() == null || !existente.getIdSucursal().equals(usuarioLogueado.getIdSucursal())) {
                resp.sendRedirect(req.getContextPath() + "/bus");
                return;
            }

            if ("INACTIVO".equalsIgnoreCase(bus.getEstadoOperativo()) && viajeDAO.tieneViajesActivos(bus.getIdBus())) {
                req.setAttribute("error", "No se puede desactivar el bus: tiene viajes programados o en tránsito.");
                req.setAttribute("busEditar", bus);
                req.getRequestDispatcher("/registrarBus.jsp").forward(req, resp);
                return;
            }

            boolean exito = busDAO.actualizar(bus);

            if (!exito && busDAO.getUltimoError() != null) {
                req.setAttribute("error", busDAO.getUltimoError());
                req.setAttribute("busEditar", bus);
                req.getRequestDispatcher("/registrarBus.jsp").forward(req, resp);
                return;
            }
        } else {
            busDAO.insertarBus(bus);
        }

        resp.sendRedirect("bus");
    }
}