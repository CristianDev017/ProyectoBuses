package servlets;

import dao.BusDAO;
import dao.ChoferDAO;
import dao.RutaDAO;
import dao.ViajeDAO;
import modelo.Bus;
import modelo.Chofer;
import modelo.Ruta;
import modelo.Usuario;
import modelo.Viaje;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/viaje")
public class ViajeServlet extends HttpServlet {

    private ViajeDAO viajeDAO;
    private BusDAO busDAO;
    private ChoferDAO choferDAO;
    private RutaDAO rutaDAO;

    @Override
    public void init() {
        viajeDAO = new ViajeDAO();
        busDAO = new BusDAO();
        choferDAO = new ChoferDAO();
        rutaDAO = new RutaDAO();
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

    private Timestamp parsearFecha(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            String normalizado = valor.replace('T', ' ');
            if (normalizado.length() == 16) { // "yyyy-MM-dd HH:mm" sin segundos
                normalizado += ":00";
            }
            return Timestamp.valueOf(normalizado);
        } catch (IllegalArgumentException e) {
            System.out.println("Error parseando fecha '" + valor + "': " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        List<Bus> buses = busDAO.listarTodos();
        List<Chofer> choferes = choferDAO.listarTodos();
        List<Ruta> rutas = rutaDAO.listarTodas();

        req.setAttribute("buses", buses);
        req.setAttribute("choferes", choferes);
        req.setAttribute("rutas", rutas);

        String accion = req.getParameter("accion");
        if ("nuevo".equalsIgnoreCase(accion)) {
            req.getRequestDispatcher("/registrarViaje.jsp").forward(req, resp);
            return;
        }

        List<Viaje> viajes = viajeDAO.listarTodos();
        req.setAttribute("viajes", viajes);
        req.getRequestDispatcher("/listadoViajes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        Viaje v = new Viaje();

        String idBusStr = req.getParameter("idBus");
        if (idBusStr != null && !idBusStr.trim().isEmpty()) {
            try {
                v.setIdBus(Integer.parseInt(idBusStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String idChoferStr = req.getParameter("idChofer");
        if (idChoferStr != null && !idChoferStr.trim().isEmpty()) {
            try {
                v.setIdChofer(Integer.parseInt(idChoferStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String idRutaStr = req.getParameter("idRuta");
        if (idRutaStr != null && !idRutaStr.trim().isEmpty()) {
            try {
                v.setIdRuta(Integer.parseInt(idRutaStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String tipo = req.getParameter("tipoViaje");
        v.setTipoViaje(tipo != null ? tipo : "REGULAR");

        v.setFechaSalida(parsearFecha(req.getParameter("fechaSalida")));
        v.setFechaLlegadaEstimada(parsearFecha(req.getParameter("fechaLlegadaEstimada")));

        String estado = req.getParameter("estado");
        v.setEstado(estado != null ? estado : "PROGRAMADO");

        viajeDAO.insertarViaje(v);

        resp.sendRedirect("viaje");
    }
}