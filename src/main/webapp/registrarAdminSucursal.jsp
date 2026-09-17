<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 19:12
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administrador de Sucursal</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>${not empty usuarioEditar ? 'Editar' : 'Registrar'} Administrador de Sucursal</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/usuario">
        <c:if test="${not empty usuarioEditar}">
            <input type="hidden" name="idUsuario" value="${usuarioEditar.idUsuario}">
        </c:if>

        <div class="mb-3">
            <label class="form-label">Sucursal</label>
            <select name="idSucursal" class="form-select" required>
                <option value="">Selecciona una sucursal</option>
                <c:forEach var="s" items="${sucursales}">
                    <option value="${s.idSucursal}" ${usuarioEditar.idSucursal == s.idSucursal ? 'selected' : ''}>${s.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">DPI</label>
            <input type="text" name="dpi" class="form-control" value="${usuarioEditar.dpi}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Nombre completo</label>
            <input type="text" name="nombreCompleto" class="form-control" value="${usuarioEditar.nombreCompleto}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">NIT</label>
            <input type="text" name="nit" class="form-control" value="${usuarioEditar.nit}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input type="text" name="telefono" class="form-control" value="${usuarioEditar.telefono}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input type="text" name="direccion" class="form-control" value="${usuarioEditar.direccion}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Correo</label>
            <input type="email" name="correo" class="form-control" value="${usuarioEditar.correo}" required>
        </div>

        <c:if test="${empty usuarioEditar}">
            <div class="mb-3">
                <label class="form-label">Contraseña</label>
                <input type="password" name="password" class="form-control" required>
            </div>
        </c:if>

        <c:if test="${not empty usuarioEditar}">
            <div class="mb-3">
                <label class="form-label">Estado</label>
                <select name="estado" class="form-select">
                    <option value="ACTIVO" ${usuarioEditar.estado == 'ACTIVO' ? 'selected' : ''}>ACTIVO</option>
                    <option value="INACTIVO" ${usuarioEditar.estado == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
                </select>
            </div>
        </c:if>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
</body>
</html>