package servlets;

import dao.SucursalDAO;
import modelo.Sucursal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/sucursal")
public class SucursalServlet extends HttpServlet {

    private SucursalDAO sucursalDAO;

    @Override
    public void init() {
        sucursalDAO = new SucursalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Sucursal> lista = sucursalDAO.listarTodas();
        req.setAttribute("sucursales", lista);
        req.getRequestDispatcher("/listadoSucursales.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(req.getParameter("nombre"));
        sucursal.setDireccion(req.getParameter("direccion"));
        sucursal.setTelefono(req.getParameter("telefono"));
        sucursal.setEstado(req.getParameter("estado"));

        sucursalDAO.insertarSucursal(sucursal);

        resp.sendRedirect("sucursal");
    }
}

