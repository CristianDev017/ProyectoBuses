<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 13/9/2026
  Time: 22:11
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Listado de Sucursales</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Sucursales</h1>

  <a href="${pageContext.servletContext.contextPath}/registrarSucursal.jsp" class="btn btn-success mb-3">
    + Nueva Sucursal
  </a>

  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Nombre</th>
      <th>Dirección</th>
      <th>Teléfono</th>
      <th>Estado</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="s" items="${sucursales}">
      <tr>
        <td>${s.idSucursal}</td>
        <td>${s.nombre}</td>
        <td>${s.direccion}</td>
        <td>${s.telefono}</td>
        <td>${s.estado}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
