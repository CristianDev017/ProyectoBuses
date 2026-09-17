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
    <title>Usuarios</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Usuarios</h1>
    <a href="${pageContext.servletContext.contextPath}/usuario?accion=nuevo" class="btn btn-success mb-3">+ Nuevo Admin de Sucursal</a>

    <table class="table table-striped">
        <thead>
        <tr><th>ID</th><th>Nombre</th><th>Correo</th><th>Rol</th><th>Sucursal</th><th>Estado</th><th>Acciones</th></tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${usuarios}">
            <tr>
                <td>${u.idUsuario}</td>
                <td>${u.nombreCompleto}</td>
                <td>${u.correo}</td>
                <td>${u.rol}</td>
                <td>${u.idSucursal}</td>
                <td>${u.estado}</td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/usuario?accion=editar&id=${u.idUsuario}" class="btn btn-sm btn-warning">Editar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>