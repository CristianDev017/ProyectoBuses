package servlets;

import dao.ReporteDAO;
import dao.SucursalDAO;
import modelo.Sucursal;
import modelo.Usuario;
import util.HtmlExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/reporte")
public class ReporteServlet extends HttpServlet {

    private ReporteDAO reporteDAO;
    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
        reporteDAO = new ReporteDAO();
        sucursalDAO = new SucursalDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null || !u.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
            resp.sendRedirect(req.getContextPath() + "/");
            return null;
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario u = verificarSesion(req, resp);
        if (u == null) return;

        String tipo = req.getParameter("tipo");
        String fechaInicio = req.getParameter("fechaInicio");
        String fechaFin = req.getParameter("fechaFin");
        String idSucursalStr = req.getParameter("idSucursal");
        String exportar = req.getParameter("export");

        Integer idSucursal = null;
        if (idSucursalStr != null && !idSucursalStr.trim().isEmpty()) {
            try { idSucursal = Integer.parseInt(idSucursalStr); } catch (NumberFormatException ignored) {}
        }

        if (tipo == null) {
            List<Sucursal> sucursales = sucursalDAO.listarTodas();
            req.setAttribute("sucursales", sucursales);
            req.getRequestDispatcher("/reportes.jsp").forward(req, resp);
            return;
        }

        List<Object[]> datos;
        String titulo;
        String[] encabezados;

        switch (tipo) {
            case "ganancias":
                datos = reporteDAO.reporteGanancias(fechaInicio, fechaFin, idSucursal);
                titulo = "Reporte de Ganancias";
                encabezados = new String[]{"Sucursal", "Ingresos", "Costos", "Ganancia Neta"};
                break;
            case "rutas":
                datos = reporteDAO.reporteRutasDemandadas(fechaInicio, fechaFin);
                titulo = "Reporte de Rutas Mas Demandadas";
                encabezados = new String[]{"ID Ruta", "Distancia", "Precio", "Boletos Vendidos"};
                break;
            case "costos":
                datos = reporteDAO.reporteCostosOperativos(fechaInicio, fechaFin, idSucursal);
                titulo = "Reporte de Costos Operativos";
                encabezados = new String[]{"Sucursal", "Combustible", "Taller", "Depreciacion", "Total"};
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/reporte");
                return;
        }

        if ("html".equalsIgnoreCase(exportar)) {
            HtmlExportUtil.exportarTabla(resp, titulo, encabezados, datos);
            return;
        }

        req.setAttribute("titulo", titulo);
        req.setAttribute("encabezados", encabezados);
        req.setAttribute("datos", datos);
        req.setAttribute("tipo", tipo);
        req.setAttribute("fechaInicio", fechaInicio);
        req.setAttribute("fechaFin", fechaFin);
        req.getRequestDispatcher("/verReporte.jsp").forward(req, resp);
    }
}