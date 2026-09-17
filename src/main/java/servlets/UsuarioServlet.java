package servlets;

import dao.SucursalDAO;
import dao.UsuarioDAO;
import modelo.Sucursal;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        sucursalDAO = new SucursalDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp, String... roles) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        boolean ok = false;
        for (String r : roles) {
            if (u.getRol().equalsIgnoreCase(r)) { ok = true; break; }
        }
        if (!ok) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp, "ADMINISTRADOR");
        if (u == null) return;

        List<Sucursal> sucursales = sucursalDAO.listarTodas();
        req.setAttribute("sucursales", sucursales);

        String accion = req.getParameter("accion");

        if ("nuevo".equalsIgnoreCase(accion)) {
            req.getRequestDispatcher("/registrarAdminSucursal.jsp").forward(req, resp);
            return;
        }

        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Usuario usuarioEditar = usuarioDAO.buscarPorId(Integer.parseInt(idParam));
                    req.setAttribute("usuarioEditar", usuarioEditar);
                    req.getRequestDispatcher("/registrarAdminSucursal.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        req.setAttribute("usuarios", usuarios);
        req.getRequestDispatcher("/listadoUsuarios.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp, "ADMINISTRADOR");
        if (u == null) return;

        String idParam = req.getParameter("idUsuario");

        Usuario usuario = new Usuario();
        usuario.setDpi(req.getParameter("dpi"));
        usuario.setNombreCompleto(req.getParameter("nombreCompleto"));
        usuario.setNit(req.getParameter("nit"));
        usuario.setTelefono(req.getParameter("telefono"));
        usuario.setDireccion(req.getParameter("direccion"));
        usuario.setCorreo(req.getParameter("correo"));
        usuario.setRol("ADMINISTRADOR_DE_SUCURSAL");

        String estado = req.getParameter("estado");
        usuario.setEstado(estado != null ? estado : "ACTIVO");

        String idSucursalStr = req.getParameter("idSucursal");
        if (idSucursalStr != null && !idSucursalStr.trim().isEmpty()) {
            try {
                usuario.setIdSucursal(Integer.parseInt(idSucursalStr));
            } catch (NumberFormatException ignored) {
            }
        }

        if (idParam != null && !idParam.trim().isEmpty()) {
            usuario.setIdUsuario(Integer.parseInt(idParam));
            boolean exito = usuarioDAO.actualizar(usuario);

            if (!exito && usuarioDAO.getUltimoError() != null) {
                req.setAttribute("error", usuarioDAO.getUltimoError());
                req.setAttribute("usuarioEditar", usuario);
                req.setAttribute("sucursales", sucursalDAO.listarTodas());
                req.getRequestDispatcher("/registrarAdminSucursal.jsp").forward(req, resp);
                return;
            }
        } else {
            usuario.setPassword(req.getParameter("password"));
            int id = usuarioDAO.insertarUsuario(usuario);

            if (id == 0 && usuarioDAO.getUltimoError() != null) {
                req.setAttribute("error", usuarioDAO.getUltimoError());
                req.setAttribute("sucursales", sucursalDAO.listarTodas());
                req.getRequestDispatcher("/registrarAdminSucursal.jsp").forward(req, resp);
                return;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/usuario");
    }
}