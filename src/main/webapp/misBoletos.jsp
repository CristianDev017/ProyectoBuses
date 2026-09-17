<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Mis Boletos</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Mis Boletos</h1>
  <table class="table table-striped">
    <thead>
    <tr><th>ID</th><th>Viaje</th><th>Asiento</th><th>Precio</th><th>Fecha Pago</th><th>Estado</th></tr>
    </thead>
    <tbody>
    <c:forEach var="b" items="${boletos}">
      <tr>
        <td>${b.idBoleto}</td>
        <td>${b.idViaje}</td>
        <td>${b.numeroAsiento}</td>
        <td>Q${b.precio}</td>
        <td><c:out value="${b.fechaPago}"/></td>
        <td>${b.estado}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>