package servlets;

import dao.RutaDAO;
import dao.SucursalDAO;
import dao.ViajeDAO;
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
    private ViajeDAO viajeDAO;

    @Override
    public void init() {
        rutaDAO = new RutaDAO();
        sucursalDAO = new SucursalDAO();
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
        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Ruta rutaEditar = rutaDAO.buscarPorId(Integer.parseInt(idParam));
                    // Verificar pertenencia: solo puede editar rutas cuyo origen es la sucursal del usuario
                    if (rutaEditar == null || rutaEditar.getIdSucursalOrigen() == null || !rutaEditar.getIdSucursalOrigen().equals(usuarioLogueado.getIdSucursal())) {
                        resp.sendRedirect(req.getContextPath() + "/ruta");
                        return;
                    }
                    req.setAttribute("rutaEditar", rutaEditar);
                    req.getRequestDispatcher("/registrarRuta.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if ("nuevo".equalsIgnoreCase(accion)) {
            req.getRequestDispatcher("/registrarRuta.jsp").forward(req, resp);
            return;
        }

        if ("eliminar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    int idRuta = Integer.parseInt(idParam);
                    // Verificar que la ruta pertenezca a esta sucursal
                    Ruta rutaAEliminar = rutaDAO.buscarPorId(idRuta);
                    if (rutaAEliminar == null || rutaAEliminar.getIdSucursalOrigen() == null || !rutaAEliminar.getIdSucursalOrigen().equals(usuarioLogueado.getIdSucursal())) {
                        resp.sendRedirect(req.getContextPath() + "/ruta");
                        return;
                    }
                    if (viajeDAO.tieneViajesActivosParaRuta(idRuta)) {
                        req.setAttribute("error", "No se puede eliminar la ruta: tiene viajes programados o en tránsito.");
                        List<Ruta> rutas = rutaDAO.listarPorOrigenSucursal(usuarioLogueado.getIdSucursal());
                        req.setAttribute("rutas", rutas);
                        req.getRequestDispatcher("/listadoRutas.jsp").forward(req, resp);
                        return;
                    }
                    rutaDAO.eliminar(idRuta);
                } catch (NumberFormatException ignored) {
                }
            }
            resp.sendRedirect(req.getContextPath() + "/ruta");
            return;
        }

        List<Ruta> rutas = rutaDAO.listarPorOrigenSucursal(usuarioLogueado.getIdSucursal());
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

        String idParam = req.getParameter("idRuta");
        if (idParam != null && !idParam.trim().isEmpty()) {
            ruta.setIdRuta(Integer.parseInt(idParam));

            if ("INACTIVA".equalsIgnoreCase(ruta.getEstado()) && viajeDAO.tieneViajesActivosParaRuta(ruta.getIdRuta())) {
                req.setAttribute("error", "No se puede desactivar la ruta: tiene viajes programados o en tránsito.");
                req.setAttribute("rutaEditar", ruta);
                req.setAttribute("sucursales", sucursalDAO.listarTodas());
                req.getRequestDispatcher("/registrarRuta.jsp").forward(req, resp);
                return;
            }

            boolean exito = rutaDAO.actualizar(ruta);

            if (!exito && rutaDAO.getUltimoError() != null) {
                req.setAttribute("error", rutaDAO.getUltimoError());
                req.setAttribute("rutaEditar", ruta);
                req.setAttribute("sucursales", sucursalDAO.listarTodas());
                req.getRequestDispatcher("/registrarRuta.jsp").forward(req, resp);
                return;
            }
        } else {
            rutaDAO.insertarRuta(ruta);
        }

        resp.sendRedirect("ruta");
    }
}
