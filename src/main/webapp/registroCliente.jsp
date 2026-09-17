<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 19:12
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Crear Cuenta</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<div class="container mt-5" style="max-width: 500px;">
    <h1 class="mb-4">Crear Cuenta</h1>

    <form method="post" action="${pageContext.servletContext.contextPath}/registro-cliente">
        <c:if test="${not empty usuario}">
            <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
        </c:if>
        <div class="mb-3">
            <label class="form-label">DPI</label>
            <input type="text" name="dpi" class="form-control" value="${usuario.dpi}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Nombre completo</label>
            <input type="text" name="nombreCompleto" class="form-control" value="${usuario.nombreCompleto}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">NIT</label>
            <input type="text" name="nit" class="form-control" value="${usuario.nit}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input type="text" name="telefono" class="form-control" value="${usuario.telefono}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input type="text" name="direccion" class="form-control" value="${usuario.direccion}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Correo</label>
            <input type="email" name="correo" class="form-control" value="${usuario.correo}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Estado</label>
            <select name="estado" class="form-select" required>
                <option value="ACTIVO" ${empty usuario or usuario.estado == 'ACTIVO' ? 'selected' : ''}>ACTIVO</option>
                <option value="INACTIVO" ${not empty usuario and usuario.estado == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Contraseña</label>
            <input type="password" name="password" class="form-control" ${empty usuario ? 'required' : ''}>
            <c:if test="${not empty usuario}"><small class="text-muted">Deja este campo vacío para conservar la contraseña actual.</small></c:if>
        </div>
        <button type="submit" class="btn btn-primary w-100">${not empty usuario ? 'Actualizar cuenta' : 'Crear cuenta'}</button>
    </form>
    <p class="mt-3 text-center">
        <a href="${pageContext.servletContext.contextPath}/">Ya tengo cuenta, iniciar sesión</a>
    </p>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>