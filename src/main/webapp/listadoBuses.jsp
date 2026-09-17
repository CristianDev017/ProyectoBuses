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
  <title>Listado de Buses</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Buses</h1>

  <a href="${pageContext.servletContext.contextPath}/registrarBus.jsp" class="btn btn-success mb-3">
    + Nuevo Bus
  </a>

  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Sucursal</th>
      <th>Placa</th>
      <th>Marca</th>
      <th>Modelo</th>
      <th>Año</th>
      <th>Capacidad</th>
      <th>Estado</th>
      <th>Kilometraje</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="b" items="${buses}">
      <tr>
        <td>${b.idBus}</td>
        <td>${b.idSucursal}</td>
        <td>${b.placa}</td>
        <td>${b.marca}</td>
        <td>${b.modelo}</td>
        <td>${b.anioFabricacion}</td>
        <td>${b.capacidad}</td>
        <td>${b.estadoOperativo}</td>
        <td>${b.kilometrajeActual}</td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/bus?accion=editar&id=${b.idBus}" class="btn btn-sm btn-warning">Editar</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
