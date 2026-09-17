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
    <title>Registrar Chofer</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>

<div class="container mt-4">
    <h1>${not empty choferEditar ? 'Editar' : 'Registrar'} Chofer</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/chofer">
        <c:if test="${not empty choferEditar}">
            <input type="hidden" name="idChofer" value="${choferEditar.idChofer}">
        </c:if>

        <div class="mb-3">
            <label for="foto" class="form-label">URL Foto</label>
            <input type="text" id="foto" name="foto" class="form-control" value="${choferEditar.foto}">
        </div>

        <div class="mb-3">
            <label for="nombreCompleto" class="form-label">Nombre completo</label>
            <input type="text" id="nombreCompleto" name="nombreCompleto" class="form-control" value="${choferEditar.nombreCompleto}" required>
        </div>

        <div class="mb-3">
            <label for="numeroLicencia" class="form-label">Número de licencia</label>
            <input type="text" id="numeroLicencia" name="numeroLicencia" class="form-control" value="${choferEditar.numeroLicencia}" required>
        </div>

        <div class="mb-3">
            <label for="tipoLicencia" class="form-label">Tipo de licencia</label>
            <input type="text" id="tipoLicencia" name="tipoLicencia" class="form-control" value="${choferEditar.tipoLicencia}">
        </div>

        <div class="mb-3">
            <label for="fechaVencimiento" class="form-label">Fecha de vencimiento</label>
            <input type="date" id="fechaVencimiento" name="fechaVencimiento" class="form-control" value="${choferEditar.fechaVencimiento}">
        </div>

        <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input type="text" id="telefono" name="telefono" class="form-control" value="${choferEditar.telefono}">
        </div>

        <div class="mb-3">
            <label for="salarioBaseViaje" class="form-label">Salario base por viaje</label>
            <input type="number" step="0.01" id="salarioBaseViaje" name="salarioBaseViaje" class="form-control" value="${choferEditar.salarioBaseViaje}">
        </div>

        <div class="mb-3">
            <label for="estado" class="form-label">Estado</label>
            <select id="estado" name="estado" class="form-select">
                <option value="ACTIVO" ${choferEditar.estado == 'ACTIVO' ? 'selected' : ''}>ACTIVO</option>
                <option value="INACTIVO" ${choferEditar.estado == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary mt-3">Guardar</button>
    </form>
</div>
</body>
</html>
