package servlets;

import dao.RegistroSalidaDAO;
import dao.ViajeDAO;
import modelo.RegistroSalida;
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

@WebServlet(urlPatterns = {"/registro-salida", "/registroSalida"})
public class RegistroSalidaServlet extends HttpServlet {

    private RegistroSalidaDAO registroSalidaDAO;
    private ViajeDAO viajeDAO;

    @Override
    public void init() {
        registroSalidaDAO = new RegistroSalidaDAO();
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
        if (usuarioLogueado == null) {
            return;
        }

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

            req.setAttribute("viaje", viaje);
            req.getRequestDispatcher("/registrarSalida.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR_DE_SUCURSAL");
        if (usuarioLogueado == null) {
            return;
        }

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

            RegistroSalida registro = new RegistroSalida();
            registro.setIdViaje(idViaje);
            registro.setIdBus(viaje.getIdBus());
            registro.setIdChofer(viaje.getIdChofer());
            registro.setFechaHoraSalidaReal(parsearFechaHora(req.getParameter("fechaHoraSalidaReal")));

            String kilometrajeInicial = req.getParameter("kilometrajeInicial");
            if (kilometrajeInicial != null && !kilometrajeInicial.trim().isEmpty()) {
                try {
                    registro.setKilometrajeInicial(Integer.parseInt(kilometrajeInicial));
                } catch (NumberFormatException ignored) {
                    registro.setKilometrajeInicial(null);
                }
            } else {
                registro.setKilometrajeInicial(null);
            }

            int idRegistro = registroSalidaDAO.insertar(registro);
            if (idRegistro > 0) {
                viajeDAO.actualizarEstado(idViaje, "EN_TRANSITO");
            }

            resp.sendRedirect(req.getContextPath() + "/viaje");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/viaje");
        }
    }
}
