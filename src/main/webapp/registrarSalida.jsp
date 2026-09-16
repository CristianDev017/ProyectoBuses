<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Salida</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Registrar salida del viaje</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.servletContext.contextPath}/registro-salida">
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
            <label class="form-label">Fecha y hora de salida real</label>
            <input type="datetime-local" name="fechaHoraSalidaReal" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Kilometraje inicial (actual del bus)</label>
            <input type="number" name="kilometrajeInicial" class="form-control"
                   value="${bus.kilometrajeActual}" readonly required>
        </div>

        <button type="submit" class="btn btn-primary">Guardar registro de salida</button>
        <a href="${pageContext.servletContext.contextPath}/viaje" class="btn btn-secondary">Cancelar</a>
    </form>
</div>
</body>
</html>