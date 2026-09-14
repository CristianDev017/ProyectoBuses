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

        List<Sucursal> lista = sucursalDAO.listarTodas();
        req.setAttribute("sucursales", lista);
        req.getRequestDispatcher("/listadoSucursales.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuarioLogueado = verificarSesion(req, resp, "ADMINISTRADOR");
        if (usuarioLogueado == null) return;

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(req.getParameter("nombre"));
        sucursal.setDireccion(req.getParameter("direccion"));
        sucursal.setTelefono(req.getParameter("telefono"));
        sucursal.setEstado(req.getParameter("estado"));

        sucursalDAO.insertarSucursal(sucursal);

        resp.sendRedirect("sucursal");
    }
}

