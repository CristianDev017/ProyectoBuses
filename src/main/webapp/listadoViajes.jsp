<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Listado de Viajes</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
  <h1>Viajes</h1>

  <a href="${pageContext.servletContext.contextPath}/viaje?accion=nuevo" class="btn btn-success mb-3">
    + Nuevo Viaje
  </a>
  <table class="table table-striped">
    <thead>
    <tr>
      <th>ID</th>
      <th>Bus</th>
      <th>Chofer</th>
      <th>Ruta</th>
      <th>Tipo</th>
      <th>Fecha Salida</th>
      <th>Fecha Llegada Est.</th>
      <th>Estado</th>
      <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="v" items="${viajes}">
      <tr>
        <td>${v.idViaje}</td>
        <td>${v.idBus}</td>
        <td>${v.idChofer}</td>
        <td>${v.idRuta}</td>
        <td>${v.tipoViaje}</td>
        <td><c:out value="${v.fechaSalida}"/></td>
        <td><c:out value="${v.fechaLlegadaEstimada}"/></td>
        <td>${v.estado}</td>
        <td>
          <c:if test="${v.estado == 'EN_TRANSITO'}">
            <a href="${pageContext.servletContext.contextPath}/registro-llegada?idViaje=${v.idViaje}" class="btn btn-sm btn-info">
              Registrar Llegada
            </a>
          </c:if>
          <c:if test="${v.estado == 'PROGRAMADO'}">
            <a href="${pageContext.servletContext.contextPath}/registro-salida?idViaje=${v.idViaje}" class="btn btn-sm btn-warning">
              Registrar Salida
            </a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
