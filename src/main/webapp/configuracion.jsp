<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 23:44
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Configuración del Sistema</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4" style="max-width: 500px;">
    <h1>Configuración del Sistema</h1>

    <p>Monto de depreciación actual: <strong>Q${monto} por km</strong></p>

    <form method="post" action="${pageContext.servletContext.contextPath}/configuracion">
        <div class="mb-3">
            <label class="form-label">Nuevo monto por kilómetro</label>
            <input type="number" step="0.01" name="monto" class="form-control" value="${monto}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Fecha</label>
            <input type="date" name="fecha" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary">Actualizar</button>
    </form>
</div>
</body>
</html>