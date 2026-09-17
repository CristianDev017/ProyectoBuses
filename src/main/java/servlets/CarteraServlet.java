package servlets;

import dao.CarteraDAO;
import dao.MovimientoCarteraDAO;
import modelo.Cartera;
import modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/cartera")
public class CarteraServlet extends HttpServlet {

    private CarteraDAO carteraDAO;
    private MovimientoCarteraDAO movimientoDAO;

    @Override
    public void init() {
        carteraDAO = new CarteraDAO();
        movimientoDAO = new MovimientoCarteraDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        Cartera cartera = carteraDAO.buscarPorIdUsuario(u.getIdUsuario());
        if (cartera == null) {
            int id = carteraDAO.crearCartera(u.getIdUsuario());
            cartera = new Cartera(id, u.getIdUsuario(), 0.0);
        }

        req.setAttribute("cartera", cartera);
        req.getRequestDispatcher("/cartera.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        Cartera cartera = carteraDAO.buscarPorIdUsuario(u.getIdUsuario());
        if (cartera == null) {
            int id = carteraDAO.crearCartera(u.getIdUsuario());
            cartera = new Cartera(id, u.getIdUsuario(), 0.0);
        }

        String montoStr = req.getParameter("monto");
        String fechaStr = req.getParameter("fecha");

        if (montoStr != null && !montoStr.trim().isEmpty() && fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                double monto = Double.parseDouble(montoStr);
                Date fecha = Date.valueOf(fechaStr);

                double nuevoSaldo = (cartera.getSaldo() != null ? cartera.getSaldo() : 0.0) + monto;
                carteraDAO.actualizarSaldo(cartera.getIdCartera(), nuevoSaldo);
                movimientoDAO.insertar(cartera.getIdCartera(), "RECARGA", monto, fecha, "Recarga manual");

            } catch (IllegalArgumentException ignored) {
            }
        }

        resp.sendRedirect(req.getContextPath() + "/cartera");
    }
}