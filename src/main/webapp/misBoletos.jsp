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
  <table class="table table-striped table-bordered align-middle">
    <thead class="table-dark">
    <tr>
      <th>ID</th>
      <th>Viaje</th>
      <th>Ruta</th>
      <th>Fecha salida</th>
      <th>Hora salida</th>
      <th>Bus</th>
      <th>Asiento</th>
      <th>Precio</th>
      <th>Fecha pago</th>
      <th>Estado</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="b" items="${boletos}">
      <tr>
        <td>${b.idBoleto}</td>
        <td>#${b.idViaje}</td>
        <td>${b.rutaDescripcion}</td>
        <td>${b.fechaSalidaTexto}</td>
        <td>${b.horaSalidaTexto}</td>
        <td>${b.placaBus}</td>
        <td>${b.numeroAsiento}</td>
        <td>Q${b.precio}</td>
        <td><c:out value="${b.fechaPago}"/></td>
        <td>${b.estado}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>