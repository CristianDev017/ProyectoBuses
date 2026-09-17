<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 14/9/2026
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Bus</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>

<div class="container mt-4">
    <h1>${not empty busEditar ? 'Editar' : 'Registrar'} Bus</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/bus">
        <c:if test="${not empty busEditar}">
            <input type="hidden" name="idBus" value="${busEditar.idBus}">
        </c:if>

        <div class="mb-3">
            <label for="foto" class="form-label">URL Foto</label>
            <input type="text" id="foto" name="foto" class="form-control" value="${busEditar.foto}">
        </div>

        <div class="mb-3">
            <label for="placa" class="form-label">Placa</label>
            <input type="text" id="placa" name="placa" class="form-control" value="${busEditar.placa}" required>
        </div>

        <div class="mb-3">
            <label for="marca" class="form-label">Marca</label>
            <input type="text" id="marca" name="marca" class="form-control" value="${busEditar.marca}">
        </div>

        <div class="mb-3">
            <label for="modelo" class="form-label">Modelo</label>
            <input type="text" id="modelo" name="modelo" class="form-control" value="${busEditar.modelo}">
        </div>

        <div class="mb-3">
            <label for="anioFabricacion" class="form-label">Año de fabricación</label>
            <input type="number" id="anioFabricacion" name="anioFabricacion" class="form-control" value="${busEditar.anioFabricacion}">
        </div>

        <div class="mb-3">
            <label for="capacidad" class="form-label">Capacidad</label>
            <input type="number" id="capacidad" name="capacidad" class="form-control" value="${busEditar.capacidad}">
        </div>

        <div class="mb-3">
            <label for="estadoOperativo" class="form-label">Estado operativo</label>
            <select id="estadoOperativo" name="estadoOperativo" class="form-control" required>
                <option value="DISPONIBLE" ${busEditar.estadoOperativo == 'DISPONIBLE' ? 'selected' : ''}>DISPONIBLE</option>
                <option value="EN_VIAJE" ${busEditar.estadoOperativo == 'EN_VIAJE' ? 'selected' : ''}>EN_VIAJE</option>
                <option value="EN_MANTENIMIENTO" ${busEditar.estadoOperativo == 'EN_MANTENIMIENTO' ? 'selected' : ''}>EN_MANTENIMIENTO</option>
                <option value="INACTIVO" ${busEditar.estadoOperativo == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="kilometrajeActual" class="form-label">Kilometraje actual</label>
            <input type="number" id="kilometrajeActual" name="kilometrajeActual" class="form-control" value="${busEditar.kilometrajeActual}">
        </div>

        <button type="submit" class="btn btn-primary mt-3">Guardar</button>
    </form>
</div>
</body>
</html>
