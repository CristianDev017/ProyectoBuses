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
    <title>Comprar Boleto</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Comprar Boleto - Viaje #${viaje.idViaje}</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <p>Precio del boleto: <strong>Q${ruta.precioBoleto}</strong></p>

    <form method="post" action="${pageContext.servletContext.contextPath}/boleto">
        <input type="hidden" name="idViaje" value="${viaje.idViaje}">

        <div class="mb-3">
            <label class="form-label">Número de asiento</label>
            <input type="number" name="numeroAsiento" class="form-control" min="1" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Fecha de pago</label>
            <input type="date" name="fechaPago" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-primary">Pagar con cartera digital</button>
    </form>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
