package util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;

public class HtmlExportUtil {

    public static void exportarTabla(HttpServletResponse resp, String titulo,
                                     String[] encabezados, List<Object[]> filas) throws IOException {

        resp.setContentType("text/html");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + titulo.replace(" ", "_") + ".html\"");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>" + titulo + "</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 30px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #333; padding: 8px; text-align: left; }");
        out.println("th { background-color: #343a40; color: white; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>" + titulo + "</h1>");
        out.println("<table>");
        out.println("<thead><tr>");
        for (String h : encabezados) {
            out.println("<th>" + h + "</th>");
        }
        out.println("</tr></thead><tbody>");

        for (Object[] fila : filas) {
            out.println("<tr>");
            for (Object valor : fila) {
                out.println("<td>" + (valor != null ? valor.toString() : "") + "</td>");
            }
            out.println("</tr>");
        }

        out.println("</tbody></table>");
        out.println("</body></html>");
        out.close();
    }
}