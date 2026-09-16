<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Llegada</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Registrar llegada del viaje</h1>

    <form method="post" action="${pageContext.servletContext.contextPath}/registro-llegada">
        <input type="hidden" name="idViaje" value="${viaje.idViaje}">

        <div class="mb-3">
            <label class="form-label">ID del Viaje</label>
            <input class="form-control" value="${viaje.idViaje}" readonly>
        </div>

        <div class="mb-3">
            <label class="form-label">Bus</label>
            <input class="form-control" value="${viaje.idBus}" readonly>
        </div>

        <div class="mb-3">
            <label class="form-label">Chofer</label>
            <input class="form-control" value="${viaje.idChofer}" readonly>
        </div>

        <div class="mb-3">
            <label class="form-label">Fecha y hora de llegada real</label>
            <input type="datetime-local" name="fechaHoraLlegadaReal" class="form-control" required>
        </div>

        <c:choose>
            <c:when test="${viaje.tipoViaje == 'REGULAR' and not empty ruta and not empty bus}">
                <div class="mb-3">
                    <label class="form-label">Kilometraje final (calculado automáticamente)</label>
                    <input type="number" class="form-control"
                           value="${bus.kilometrajeActual + ruta.distanciaKm}" readonly>
                    <div class="form-text">
                        Kilometraje actual del bus (${bus.kilometrajeActual} km) + distancia de la ruta (${ruta.distanciaKm} km)
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="mb-3">
                    <label class="form-label">Kilometraje final</label>
                    <input type="number" name="kilometrajeFinal" class="form-control" min="0" required>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="mb-3">
            <label class="form-label">Gasto de combustible</label>
            <input type="number" step="0.01" name="gastoCombustible" class="form-control" min="0">
        </div>

        <button type="submit" class="btn btn-primary">Guardar registro de llegada</button>
        <a href="${pageContext.servletContext.contextPath}/viaje" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
</body>
</html>