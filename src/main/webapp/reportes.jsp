<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 17/9/2026
  Time: 00:25
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Reportes</title><jsp:include page="/includes/resources.jsp"/></head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Reportes del Sistema</h1>

  <form method="get" action="${pageContext.servletContext.contextPath}/reporte" class="row g-3">
    <div class="col-md-3">
      <label class="form-label">Tipo de reporte</label>
      <select name="tipo" class="form-select" required>
        <option value="ganancias">Ganancias</option>
        <option value="rutas">Rutas más demandadas</option>
        <option value="costos">Costos operativos</option>
      </select>
    </div>
    <div class="col-md-2">
      <label class="form-label">Desde</label>
      <input type="date" name="fechaInicio" class="form-control">
    </div>
    <div class="col-md-2">
      <label class="form-label">Hasta</label>
      <input type="date" name="fechaFin" class="form-control">
    </div>
    <div class="col-md-3">
      <label class="form-label">Sucursal (opcional)</label>
      <select name="idSucursal" class="form-select">
        <option value="">Todas</option>
        <c:forEach var="s" items="${sucursales}">
          <option value="${s.idSucursal}">${s.nombre}</option>
        </c:forEach>
      </select>
    </div>
    <div class="col-md-2 d-flex align-items-end">
      <button type="submit" class="btn btn-primary">Generar</button>
    </div>
  </form>
</div>
</body>
</html>
