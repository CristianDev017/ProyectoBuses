package servlets;

import dao.RegistroLlegadaDAO;
import dao.RegistroSalidaDAO;
import dao.ViajeDAO;
import dao.ConfiguracionSistemaDAO;
import dao.BusDAO;
import dao.RutaDAO;
import modelo.RegistroLlegada;
import modelo.RegistroSalida;
import modelo.Usuario;
import modelo.Viaje;
import modelo.Bus;
import modelo.Ruta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(urlPatterns = {"/registro-llegada", "/registroLlegada"})
public class RegistroLlegadaServlet extends HttpServlet {

    private RegistroLlegadaDAO registroLlegadaDAO;
    private RegistroSalidaDAO registroSalidaDAO;
    private ViajeDAO viajeDAO;
    private ConfiguracionSistemaDAO configuracionDAO;
    private BusDAO busDAO;
    private RutaDAO rutaDAO;

    @Override
    public void init() {
        registroLlegadaDAO = new RegistroLlegadaDAO();
        registroSalidaDAO = new RegistroSalidaDAO();
        viajeDAO = new ViajeDAO();
        configuracionDAO = new ConfiguracionSistemaDAO();
        busDAO = new BusDAO();
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

    private Timestamp parsearFechaHora(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }

        try {
            String normalizado = valor.replace('T', ' ');
            if (normalizado.length() == 16) {
                normalizado += ":00";
            }
            return Timestamp.valueOf(normalizado);
        } catch (IllegalArgumentException e) {
            System.out.println("Error parseando fecha/hora '" + valor + "': " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        String idViajeParam = req.getParameter("idViaje");
        if (idViajeParam == null || idViajeParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
            return;
        }

        try {
            int idViaje = Integer.parseInt(idViajeParam);
            Viaje viaje = viajeDAO.buscarPorId(idViaje);

            if (viaje == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Viaje no encontrado");
                return;
            }

            if (viaje.getIdRuta() != null) {
                Ruta ruta = rutaDAO.buscarPorId(viaje.getIdRuta());
                req.setAttribute("ruta", ruta);
            }

            if (viaje.getIdBus() != null) {
                Bus bus = busDAO.buscarPorId(viaje.getIdBus());
                req.setAttribute("bus", bus);
            }

            req.setAttribute("viaje", viaje);
            req.getRequestDispatcher("/registrarLlegada.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) return;

        String idViajeParam = req.getParameter("idViaje");
        if (idViajeParam == null || idViajeParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
            return;
        }

        try {
            int idViaje = Integer.parseInt(idViajeParam);
            Viaje viaje = viajeDAO.buscarPorId(idViaje);

            if (viaje == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Viaje no encontrado");
                return;
            }

            RegistroSalida registroSalida = registroSalidaDAO.buscarPorIdViaje(idViaje);
            Integer kmInicial = (registroSalida != null) ? registroSalida.getKilometrajeInicial() : null;

            Double montoDepKm = configuracionDAO.obtenerMontoDepreciacion();
            if (montoDepKm == null) montoDepKm = 0.0;

            RegistroLlegada registro = new RegistroLlegada();
            registro.setIdViaje(idViaje);
            registro.setFechaHoraLlegadaReal(parsearFechaHora(req.getParameter("fechaHoraLlegadaReal")));

            Integer kmFinal = null;

            if (viaje.getTipoViaje() != null && viaje.getTipoViaje().equalsIgnoreCase("REGULAR")
                    && viaje.getIdRuta() != null && viaje.getIdBus() != null) {

                Ruta ruta = rutaDAO.buscarPorId(viaje.getIdRuta());
                Bus bus = busDAO.buscarPorId(viaje.getIdBus());

                if (ruta != null && ruta.getDistanciaKm() != null && bus != null && bus.getKilometrajeActual() != null) {
                    // sumar y redondear la distancia de la ruta
                    kmFinal = bus.getKilometrajeActual() + (int) Math.round(ruta.getDistanciaKm());
                }
            }

            if (kmFinal == null) {
                String kmFinalStr = req.getParameter("kilometrajeFinal");
                if (kmFinalStr != null && !kmFinalStr.trim().isEmpty()) {
                    try { kmFinal = Integer.parseInt(kmFinalStr); } catch (NumberFormatException ignored) { kmFinal = null; }
                }
            }
            registro.setKilometrajeFinal(kmFinal);

            String gastoCombStr = req.getParameter("gastoCombustible");
            Double gastoComb = null;
            if (gastoCombStr != null && !gastoCombStr.trim().isEmpty()) {
                try { gastoComb = Double.parseDouble(gastoCombStr); } catch (NumberFormatException ignored) { gastoComb = null; }
            }
            registro.setGastoCombustible(gastoComb);

            Double montoDepreciacion = null;
            if (kmInicial != null && kmFinal != null && kmFinal >= kmInicial) {
                int kmRecorridos = kmFinal - kmInicial;
                montoDepreciacion = kmRecorridos * montoDepKm;
            } else {
                montoDepreciacion = 0.0;
            }
            registro.setMontoDepreciacion(montoDepreciacion);

            int idRegistro = registroLlegadaDAO.insertar(registro);
            if (idRegistro > 0) {
                viajeDAO.actualizarEstado(idViaje, "FINALIZADO");
                if (viaje.getIdBus() != null && kmFinal != null) {
                    busDAO.actualizarKilometraje(viaje.getIdBus(), kmFinal);
                }
            }

            resp.sendRedirect(req.getContextPath() + "/viaje");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
        }
    }
}
