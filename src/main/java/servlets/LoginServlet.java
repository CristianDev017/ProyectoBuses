package servlets;

import dao.CarteraDAO;
import dao.UsuarioDAO;
import modelo.Cartera;
import modelo.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private CarteraDAO carteraDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        carteraDAO = new CarteraDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String correo = req.getParameter("correo");
        String password = req.getParameter("password");

        Usuario usuario = usuarioDAO.login(correo, password);

        if (usuario != null) {
            Cartera cartera = carteraDAO.buscarPorIdUsuario(usuario.getIdUsuario());
            if (cartera == null) {
                carteraDAO.crearCartera(usuario.getIdUsuario());
            }

            HttpSession session = req.getSession();
            session.setAttribute("usuario", usuario);

            String rol = usuario.getRol();
            String context = req.getContextPath();

            if (rol.equalsIgnoreCase("ADMINISTRADOR")) {
                resp.sendRedirect(context + "/sucursal");
                return;
            } else if (rol.equalsIgnoreCase("ADMINISTRADOR_DE_SUCURSAL")) {
                resp.sendRedirect(context + "/bus");
                return;

            } else if (rol.equalsIgnoreCase("CLIENTE")) {
            resp.sendRedirect(context + "/cartera");
                return;
        }

            resp.sendRedirect(context + "/");

        } else {
            // Login fallido
            req.setAttribute("error", "Correo o contraseña incorrectos");
            RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
            rd.forward(req, resp);
        }
    }
}

