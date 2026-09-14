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
  <title>Listado de Choferes</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Choferes</h1>

  <a href="${pageContext.servletContext.contextPath}/registrarChofer.jsp" class="btn btn-success mb-3">
    + Nuevo Chofer
  </a>

  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Sucursal</th>
      <th>Nombre</th>
      <th>Licencia</th>
      <th>Tipo</th>
      <th>Vencimiento</th>
      <th>Teléfono</th>
      <th>Salario</th>
      <th>Estado</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${choferes}">
      <tr>
        <td>${c.idChofer}</td>
        <td>${c.idSucursal}</td>
        <td>${c.nombreCompleto}</td>
        <td>${c.numeroLicencia}</td>
        <td>${c.tipoLicencia}</td>
        <td><c:out value="${c.fechaVencimiento}"/></td>
        <td>${c.telefono}</td>
        <td>${c.salarioBaseViaje}</td>
        <td>${c.estado}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
