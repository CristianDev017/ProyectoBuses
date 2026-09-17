package servlets;

import dao.SucursalDAO;
import jakarta.servlet.http.HttpSession;
import modelo.Sucursal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/sucursal")
public class SucursalServlet extends HttpServlet {

    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
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

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR");
        if (usuarioLogueado == null) return;

        String accion = req.getParameter("accion");
        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Sucursal s = sucursalDAO.buscarPorId(Integer.parseInt(idParam));
                    req.setAttribute("sucursal", s);
                    req.getRequestDispatcher("/registrarSucursal.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        List<Sucursal> lista = sucursalDAO.listarTodas();
        req.setAttribute("sucursales", lista);
        req.getRequestDispatcher("/listadoSucursales.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR");
        if (usuarioLogueado == null) return;

        String idParam = req.getParameter("idSucursal");
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(req.getParameter("nombre"));
        sucursal.setDireccion(req.getParameter("direccion"));
        sucursal.setTelefono(req.getParameter("telefono"));
        sucursal.setEstado(req.getParameter("estado"));

        if (idParam != null && !idParam.trim().isEmpty()) {
            sucursal.setIdSucursal(Integer.parseInt(idParam));
            boolean exito = sucursalDAO.actualizar(sucursal);

            if (!exito && sucursalDAO.getUltimoError() != null) {
                req.setAttribute("error", sucursalDAO.getUltimoError());
                req.setAttribute("sucursal", sucursal); // para que el form no pierda los datos ya escritos
                req.getRequestDispatcher("/registrarSucursal.jsp").forward(req, resp);
                return;
            }
        } else {
            int id = sucursalDAO.insertarSucursal(sucursal);

            if (id == 0 && sucursalDAO.getUltimoError() != null) {
                req.setAttribute("error", sucursalDAO.getUltimoError());
                req.getRequestDispatcher("/registrarSucursal.jsp").forward(req, resp);
                return;
            }
        }

        resp.sendRedirect("sucursal");
    }
}

