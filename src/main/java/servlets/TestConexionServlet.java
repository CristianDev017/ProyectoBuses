package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.ConexionBD;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "TestConexionServlet", urlPatterns = {"/test-conexion"})
public class TestConexionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection con = ConexionBD.obtenerConexion()) {
            out.println("<h1>Conexión exitosa</h1>");
        } catch (Exception e) {
            out.println("<h1>Error de conexión</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}