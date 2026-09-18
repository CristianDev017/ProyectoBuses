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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet("/boleto")
public class BoletoServlet extends HttpServlet {

    private BoletoDAO boletoDAO;
    private ViajeDAO viajeDAO;
    private RutaDAO rutaDAO;
    private CarteraDAO carteraDAO;
    private MovimientoCarteraDAO movimientoDAO;
    private BusDAO busDAO;
    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
        boletoDAO = new BoletoDAO();
        viajeDAO = new ViajeDAO();
        rutaDAO = new RutaDAO();
        carteraDAO = new CarteraDAO();
        movimientoDAO = new MovimientoCarteraDAO();
        busDAO = new BusDAO();
        sucursalDAO = new SucursalDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
        return u;
    }

    private Cartera obtenerOCrearCartera(Usuario u) {
        Cartera cartera = carteraDAO.buscarPorIdUsuario(u.getIdUsuario());
        if (cartera == null) {
            int idCartera = carteraDAO.crearCartera(u.getIdUsuario());
            cartera = new Cartera(idCartera, u.getIdUsuario(), 0.0);
        }
        return cartera;
    }

    private List<Integer> parsearAsientos(String valor) {
        List<Integer> asientos = new ArrayList<>();
        if (valor == null || valor.trim().isEmpty()) {
            return asientos;
        }
        String[] partes = valor.split(",");
        for (String parte : partes) {
            String limpio = parte.trim();
            if (!limpio.isEmpty()) {
                try {
                    asientos.add(Integer.parseInt(limpio));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return asientos;
    }

    private void cargarVistaCompra(HttpServletRequest req, HttpServletResponse resp, Usuario u, int idViaje) throws ServletException, IOException {
        Viaje viaje = viajeDAO.buscarPorId(idViaje);
        if (viaje == null || viaje.getIdRuta() == null || !"REGULAR".equalsIgnoreCase(viaje.getTipoViaje())) {
            req.setAttribute("error", "El viaje seleccionado no está disponible para compra regular.");
            req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
            return;
        }

        Ruta ruta = rutaDAO.buscarPorId(viaje.getIdRuta());
        Bus bus = (viaje.getIdBus() != null) ? busDAO.buscarPorId(viaje.getIdBus()) : null;
        Sucursal origen = (ruta != null && ruta.getIdSucursalOrigen() != null) ? sucursalDAO.buscarPorId(ruta.getIdSucursalOrigen()) : null;
        Sucursal destino = (ruta != null && ruta.getIdSucursalDestino() != null) ? sucursalDAO.buscarPorId(ruta.getIdSucursalDestino()) : null;

        int capacidad = (bus != null && bus.getCapacidad() != null) ? bus.getCapacidad() : 0;
        List<Integer> asientosDisponibles = boletoDAO.obtenerAsientosDisponibles(idViaje, capacidad);
        List<Integer> asientosOcupados = boletoDAO.obtenerAsientosOcupados(idViaje);
        Cartera cartera = obtenerOCrearCartera(u);
        List<Integer> seleccionPrevia = parsearAsientos(req.getParameter("asientos"));

        if (viaje.getFechaSalida() != null) {
            SimpleDateFormat fechaFmt = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "GT"));
            SimpleDateFormat horaFmt = new SimpleDateFormat("HH:mm", new Locale("es", "GT"));
            req.setAttribute("fechaSalidaTexto", fechaFmt.format(viaje.getFechaSalida()));
            req.setAttribute("horaSalidaTexto", horaFmt.format(viaje.getFechaSalida()));
        }

        req.setAttribute("viaje", viaje);
        req.setAttribute("ruta", ruta);
        req.setAttribute("bus", bus);
        req.setAttribute("origen", origen);
        req.setAttribute("destino", destino);
        req.setAttribute("asientosDisponibles", asientosDisponibles);
        req.setAttribute("asientosOcupados", asientosOcupados);
        req.setAttribute("asientosSeleccionados", seleccionPrevia);
        req.setAttribute("cartera", cartera);
        req.setAttribute("saldoActual", cartera.getSaldo());
        req.setAttribute("precioBoleto", ruta != null ? ruta.getPrecioBoleto() : 0.0);
        req.getRequestDispatcher("/comprarBoleto.jsp").forward(req, resp);
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
                    cargarVistaCompra(req, resp, u, idViaje);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
            req.setAttribute("error", "Viaje no válido.");
            req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
            return;
        }

        if ("misBoletos".equalsIgnoreCase(accion)) {
            List<Boleto> misBoletos = boletoDAO.listarPorUsuario(u.getIdUsuario());
            req.setAttribute("boletos", misBoletos);
            req.getRequestDispatcher("/misBoletos.jsp").forward(req, resp);
            return;
        }

        List<ViajeDetalle> viajes = viajeDAO.listarRegularesDisponibles();
        req.setAttribute("viajes", viajes);
        req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        String idViajeStr = req.getParameter("idViaje");
        String asientosParam = req.getParameter("asientos");
        if (idViajeStr == null || asientosParam == null || asientosParam.trim().isEmpty()) {
            req.setAttribute("error", "Debes seleccionar al menos un asiento.");
            req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
            return;
        }

        try {
            int idViaje = Integer.parseInt(idViajeStr);
            List<Integer> asientosSeleccionados = parsearAsientos(asientosParam);
            if (asientosSeleccionados.isEmpty()) {
                req.setAttribute("error", "Debes seleccionar al menos un asiento disponible.");
                cargarVistaCompra(req, resp, u, idViaje);
                return;
            }

            Viaje viaje = viajeDAO.buscarPorId(idViaje);
            if (viaje == null || viaje.getIdRuta() == null || !"REGULAR".equalsIgnoreCase(viaje.getTipoViaje())) {
                req.setAttribute("error", "El viaje seleccionado no es válido para compra regular.");
                req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
                return;
            }

            Ruta ruta = rutaDAO.buscarPorId(viaje.getIdRuta());
            if (ruta == null || ruta.getPrecioBoleto() == null) {
                req.setAttribute("error", "La ruta del viaje no tiene precio registrado.");
                cargarVistaCompra(req, resp, u, idViaje);
                return;
            }

            Bus bus = (viaje.getIdBus() != null) ? busDAO.buscarPorId(viaje.getIdBus()) : null;
            int capacidad = (bus != null && bus.getCapacidad() != null) ? bus.getCapacidad() : 0;
            List<Integer> ocupados = boletoDAO.obtenerAsientosOcupados(idViaje);
            for (Integer asiento : asientosSeleccionados) {
                if (asiento < 1 || asiento > capacidad || ocupados.contains(asiento)) {
                    req.setAttribute("error", "Uno o varios asientos seleccionados ya están ocupados o no existen en este bus.");
                    cargarVistaCompra(req, resp, u, idViaje);
                    return;
                }
            }

            double precioUnitario = ruta.getPrecioBoleto();
            double totalCompra = asientosSeleccionados.size() * precioUnitario;
            String fechaPagoStr = req.getParameter("fechaPago");
            if (fechaPagoStr == null || fechaPagoStr.trim().isEmpty()) {
                req.setAttribute("error", "Debes indicar la fecha de pago manualmente.");
                cargarVistaCompra(req, resp, u, idViaje);
                return;
            }

            try {
                Date fechaPago = Date.valueOf(fechaPagoStr);
                Cartera cartera = obtenerOCrearCartera(u);
                if (cartera.getSaldo() == null || cartera.getSaldo() < totalCompra) {
                    req.setAttribute("error", "Saldo insuficiente en tu cartera digital. No se realizó la compra.");
                    cargarVistaCompra(req, resp, u, idViaje);
                    return;
                }

                double nuevoSaldo = cartera.getSaldo() - totalCompra;
                int comprasRegistradas = boletoDAO.registrarCompraMultiple(idViaje, u.getIdUsuario(), asientosSeleccionados, precioUnitario, fechaPago, cartera.getIdCartera(), nuevoSaldo);
                if (comprasRegistradas <= 0) {
                    req.setAttribute("error", "La compra no pudo completarse. Verifica los asientos seleccionados y el saldo disponible.");
                    cargarVistaCompra(req, resp, u, idViaje);
                    return;
                }

                movimientoDAO.insertar(cartera.getIdCartera(), "PAGO", totalCompra, fechaPago, "Compra boleto viaje " + idViaje + " asientos " + asientosSeleccionados);
                resp.sendRedirect(req.getContextPath() + "/boleto?accion=misBoletos");
                return;
            } catch (IllegalArgumentException e) {
                req.setAttribute("error", "La fecha de pago es inválida.");
                cargarVistaCompra(req, resp, u, idViaje);
                return;
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Datos de compra inválidos.");
            req.getRequestDispatcher("/viajesDisponibles.jsp").forward(req, resp);
        }
    }
}
