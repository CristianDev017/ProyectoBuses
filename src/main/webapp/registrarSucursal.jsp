<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 13/9/2026
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Sucursal</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>

<div class="container mt-4">
    <h1>Registrar Sucursal</h1>

    <c:if test="${not empty error and error != ''}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/sucursal">
        <c:if test="${not empty sucursal}">
            <input type="hidden" name="idSucursal" value="${sucursal.idSucursal}">
        </c:if>

        <div class="mb-3">
            <label class="form-label">Nombre</label>
            <input type="text" name="nombre" class="form-control" value="${sucursal.nombre}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input type="text" name="direccion" class="form-control" value="${sucursal.direccion}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input type="text" name="telefono" class="form-control" value="${sucursal.telefono}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Estado</label>
            <select name="estado" class="form-select">
                <option value="ACTIVA" ${sucursal.estado == 'ACTIVA' ? 'selected' : ''}>ACTIVA</option>
                <option value="INACTIVA" ${sucursal.estado == 'INACTIVA' ? 'selected' : ''}>INACTIVA</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
