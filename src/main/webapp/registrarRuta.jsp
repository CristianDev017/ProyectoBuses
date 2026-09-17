<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Ruta</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>${not empty rutaEditar ? 'Editar' : 'Registrar'} Ruta</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/ruta">
        <c:if test="${not empty rutaEditar}">
            <input type="hidden" name="idRuta" value="${rutaEditar.idRuta}">
        </c:if>

        <div class="mb-3">
            <label class="form-label">Sucursal Destino</label>
            <select name="idSucursalDestino" class="form-select" required>
                <option value="">Selecciona una sucursal</option>
                <c:forEach var="s" items="${sucursales}">
                    <option value="${s.idSucursal}" ${not empty rutaEditar and s.idSucursal == rutaEditar.idSucursalDestino ? 'selected' : ''}>${s.nombre}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Distancia (km)</label>
            <input type="number" step="0.01" name="distanciaKm" class="form-control" value="${rutaEditar.distanciaKm}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Precio del Boleto</label>
            <input type="number" step="0.01" name="precioBoleto" class="form-control" value="${rutaEditar.precioBoleto}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Estado</label>
            <select name="estado" class="form-select">
                <option value="ACTIVA" ${rutaEditar.estado == 'ACTIVA' ? 'selected' : ''}>ACTIVA</option>
                <option value="INACTIVA" ${rutaEditar.estado == 'INACTIVA' ? 'selected' : ''}>INACTIVA</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
</body>
</html>