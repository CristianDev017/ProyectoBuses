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
    <h2>Viajes regulares</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
        <tr>
            <th>Ruta</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Bus</th>
            <th>Precio</th>
            <th>Disponibles</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="detalle" items="${viajes}">
            <tr>
                <td>${detalle.rutaDescripcion}</td>
                <td>${detalle.fechaSalidaTexto}</td>
                <td>${detalle.horaSalidaTexto}</td>
                <td>${detalle.bus.placa}</td>
                <td>Q${detalle.ruta.precioBoleto}</td>
                <td>${detalle.asientosDisponibles}</td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/boleto?accion=comprar&idViaje=${detalle.viaje.idViaje}" class="btn btn-sm btn-primary">Seleccionar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
