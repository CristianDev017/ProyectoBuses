package servlets;

import dao.*;
import modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/boleto")
public class BoletoServlet extends HttpServlet {

    private BoletoDAO boletoDAO;
    private ViajeDAO viajeDAO;
    private RutaDAO rutaDAO;
    private CarteraDAO carteraDAO;
    private MovimientoCarteraDAO movimientoDAO;

    @Override
    public void init() {
        boletoDAO = new BoletoDAO();
        viajeDAO = new ViajeDAO();
        rutaDAO = new RutaDAO();
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

        String accion = req.getParameter("accion");

        if ("comprar".equalsIgnoreCase(accion)) {
            String idViajeStr = req.getParameter("idViaje");
            if (idViajeStr != null) {
                try {
                    int idViaje = Integer.parseInt(idViajeStr);
                    Viaje viaje = viajeDAO.buscarPorId(idViaje);
                    Ruta ruta = (viaje != null && viaje.getIdRuta() != null) ? rutaDAO.buscarPorId(viaje.getIdRuta()) : null;

                    req.setAttribute("viaje", viaje);
                    req.setAttribute("ruta", ruta);
                    req.getRequestDispatcher("/comprarBoleto.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if ("misBoletos".equalsIgnoreCase(accion)) {
            List<Boleto> misBoletos = boletoDAO.listarPorUsuario(u.getIdUsuario());
            req.setAttribute("boletos", misBoletos);
            req.getRequestDispatcher("/misBoletos.jsp").forward(req, resp);
            return;
        }


        List<Viaje> todos = viajeDAO.listarTodos();
        req.setAttribute("viajes", todos);
        req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        String idViajeStr = req.getParameter("idViaje");
        String asientoStr = req.getParameter("numeroAsiento");

        if (idViajeStr == null || asientoStr == null) {
            resp.sendRedirect(req.getContextPath() + "/boleto");
            return;
        }

        try {
            int idViaje = Integer.parseInt(idViajeStr);
            int asiento = Integer.parseInt(asientoStr);

            Viaje viaje = viajeDAO.buscarPorId(idViaje);
            if (viaje == null || viaje.getIdRuta() == null) {
                resp.sendRedirect(req.getContextPath() + "/boleto");
                return;
            }

            Ruta ruta = rutaDAO.buscarPorId(viaje.getIdRuta());
            if (ruta == null || ruta.getPrecioBoleto() == null) {
                resp.sendRedirect(req.getContextPath() + "/boleto");
                return;
            }

            double precio = ruta.getPrecioBoleto();

            Cartera cartera = carteraDAO.buscarPorIdUsuario(u.getIdUsuario());
            if (cartera == null || cartera.getSaldo() == null || cartera.getSaldo() < precio) {
                req.setAttribute("error", "Saldo insuficiente en tu cartera digital.");
                req.setAttribute("viaje", viaje);
                req.setAttribute("ruta", ruta);
                req.getRequestDispatcher("/comprarBoleto.jsp").forward(req, resp);
                return;
            }

            String fechaStr = req.getParameter("fechaPago");
            Date fechaPago = (fechaStr != null && !fechaStr.trim().isEmpty())
                    ? Date.valueOf(fechaStr) : new Date(System.currentTimeMillis());

            Boleto b = new Boleto();
            b.setIdViaje(idViaje);
            b.setIdUsuario(u.getIdUsuario());
            b.setNumeroAsiento(asiento);
            b.setPrecio(precio);
            b.setFechaPago(fechaPago);
            b.setEstado("PAGADO");

            double nuevoSaldo = cartera.getSaldo() - precio;
            int idBoleto = boletoDAO.comprarBoletoTransaccional(b, cartera.getIdCartera(), nuevoSaldo);

            if (idBoleto > 0) {
                movimientoDAO.insertar(cartera.getIdCartera(), "PAGO", precio, fechaPago, "Compra boleto viaje " + idViaje);
            }

            resp.sendRedirect(req.getContextPath() + "/boleto?accion=misBoletos");

        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/boleto");
        }
    }
}