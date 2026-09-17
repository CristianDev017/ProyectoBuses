package servlets;

import dao.UsuarioDAO;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro-cliente")
public class RegistroClienteServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("editar".equalsIgnoreCase(accion)) {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Usuario usuarioSeleccionado = usuarioDAO.buscarPorId(Integer.parseInt(idParam));
                    if (usuarioSeleccionado != null) {
                        req.setAttribute("usuario", usuarioSeleccionado);
                        req.getRequestDispatcher("/registroCliente.jsp").forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        req.getRequestDispatcher("/registroCliente.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("idUsuario");
        Usuario usuario = new Usuario();

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                usuario.setIdUsuario(Integer.parseInt(idParam));
            } catch (NumberFormatException ignored) {
            }
        }

        usuario.setDpi(req.getParameter("dpi"));
        usuario.setNombreCompleto(req.getParameter("nombreCompleto"));
        usuario.setNit(req.getParameter("nit"));
        usuario.setTelefono(req.getParameter("telefono"));
        usuario.setDireccion(req.getParameter("direccion"));
        usuario.setCorreo(req.getParameter("correo"));
        usuario.setRol("CLIENTE");
        usuario.setEstado(req.getParameter("estado") != null && !req.getParameter("estado").trim().isEmpty()
                ? req.getParameter("estado") : "ACTIVO");
        usuario.setIdSucursal(null);

        String password = req.getParameter("password");
        if (usuario.getIdUsuario() > 0 && (password == null || password.trim().isEmpty())) {
            Usuario actual = usuarioDAO.buscarPorId(usuario.getIdUsuario());
            if (actual != null) {
                usuario.setPassword(actual.getPassword());
            }
        } else {
            usuario.setPassword(password);
        }

        if (usuario.getIdUsuario() > 0) {
            usuarioDAO.actualizar(usuario);
            resp.sendRedirect(req.getContextPath() + "/usuario");
        } else {
            usuarioDAO.insertarUsuario(usuario);
            resp.sendRedirect(req.getContextPath() + "/?registrado=true");
        }
    }
}