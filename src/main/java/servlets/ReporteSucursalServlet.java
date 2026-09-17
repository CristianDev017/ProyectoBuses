package servlets;

import dao.ReporteDAO;
import dao.RegistroLlegadaDAO;
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

@WebServlet("/reporte-sucursal")
public class ReporteSucursalServlet extends HttpServlet {

    private ReporteDAO reporteDAO;
    private RegistroLlegadaDAO registroLlegadaDAO;

    @Override
    public void init() {
        reporteDAO = new ReporteDAO();
        registroLlegadaDAO = new RegistroLlegadaDAO();
    }

    private Usuario verificarSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null || !u.getRol().equalsIgnoreCase("ADMINISTRADOR_DE_SUCURSAL")) {
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
        String exportar = req.getParameter("export");

        if (tipo == null) {
            req.getRequestDispatcher("/reportesSucursal.jsp").forward(req, resp);
            return;
        }

        List<Object[]> datos;
        String titulo;
        String[] encabezados;

        switch (tipo) {
            case "buses":
                datos = reporteDAO.listadoBuses(u.getIdSucursal());
                titulo = "Listado General de Buses";
                encabezados = new String[]{"Placa", "Bus", "Capacidad", "Estado", "Chofer Actual", "Kilometraje", "Total Viajes"};
                break;
            case "choferes":
                datos = reporteDAO.listadoChoferes(u.getIdSucursal());
                titulo = "Listado General de Choferes";
                encabezados = new String[]{"Licencia", "Nombre", "Tipo Licencia", "Vencimiento", "Estado", "Total Viajes"};
                break;
            case "ingresosBoletos":
                datos = reporteDAO.reporteIngresosBoletos(u.getIdSucursal(), fechaInicio, fechaFin);
                titulo = "Reporte de Ingresos por Venta de Boletos";
                encabezados = new String[]{"ID Viaje", "ID Ruta", "Fecha Pago", "Cantidad", "Ingreso"};
                break;
            case "depreciacion":
                datos = registroLlegadaDAO.reporteDepreciacionPorSucursal(u.getIdSucursal());
                titulo = "Reporte de Depreciacion por Bus";
                encabezados = new String[]{"Placa", "Bus", "Km Totales", "Depreciacion Acumulada"};
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/reporte-sucursal");
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