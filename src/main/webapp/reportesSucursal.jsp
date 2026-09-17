<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 17/9/2026
  Time: 00:34
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Reportes</title><jsp:include page="/includes/resources.jsp"/></head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Reportes de Sucursal</h1>

    <form method="get" action="${pageContext.servletContext.contextPath}/reporte-sucursal" class="row g-3">
        <div class="col-md-4">
            <label class="form-label">Tipo de reporte</label>
            <select name="tipo" class="form-select" required>
                <option value="buses">Listado de Buses</option>
                <option value="choferes">Listado de Choferes</option>
                <option value="ingresosBoletos">Ingresos por Venta de Boletos</option>
                <option value="depreciacion">Depreciacion por Bus</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Desde (si aplica)</label>
            <input type="date" name="fechaInicio" class="form-control">
        </div>
        <div class="col-md-3">
            <label class="form-label">Hasta (si aplica)</label>
            <input type="date" name="fechaFin" class="form-control">
        </div>
        <div class="col-md-2 d-flex align-items-end">
            <button type="submit" class="btn btn-primary">Generar</button>
        </div>
    </form>
</div>
</body>
</html>