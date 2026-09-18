<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Viajes Disponibles</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Viajes Regulares Disponibles</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="table table-striped table-bordered align-middle">
        <thead class="table-dark">
        <tr>
            <th>Viaje</th>
            <th>Ruta</th>
            <th>Fecha salida</th>
            <th>Hora salida</th>
            <th>Precio</th>
            <th>Bus</th>
            <th>Asientos disponibles</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="detalle" items="${viajes}">
            <tr>
                <td>#${detalle.viaje.idViaje}</td>
                <td>${detalle.rutaDescripcion}</td>
                <td>${detalle.fechaSalidaTexto}</td>
                <td>${detalle.horaSalidaTexto}</td>
                <td>Q${detalle.ruta.precioBoleto}</td>
                <td>${detalle.bus.placa}</td>
                <td>${detalle.asientosDisponibles}</td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/boleto?accion=comprar&idViaje=${detalle.viaje.idViaje}" class="btn btn-sm btn-primary">Comprar boleto</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
