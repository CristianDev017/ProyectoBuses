<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Viaje</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Registrar Viaje (REGULAR)</h1>

    <form method="post" action="${pageContext.servletContext.contextPath}/viaje">
        <div class="mb-3">
            <label class="form-label">Bus</label>
            <select name="idBus" class="form-select" required>
                <option value="">Selecciona un bus</option>
                <c:forEach var="b" items="${buses}">
                    <option value="${b.idBus}">${b.placa} - ${b.marca} ${b.modelo}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Chofer</label>
            <select name="idChofer" class="form-select" required>
                <option value="">Selecciona un chofer</option>
                <c:forEach var="c" items="${choferes}">
                    <option value="${c.idChofer}">${c.nombreCompleto} - ${c.numeroLicencia}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Ruta</label>
            <select name="idRuta" class="form-select" required>
                <option value="">Selecciona una ruta</option>
                <c:forEach var="r" items="${rutas}">
                    <option value="${r.idRuta}">Ruta ${r.idRuta} - ${r.distanciaKm} km - Q${r.precioBoleto}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Fecha y hora de salida</label>
            <input type="datetime-local" name="fechaSalida" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Fecha y hora estimada de llegada</label>
            <input type="datetime-local" name="fechaLlegadaEstimada" class="form-control" required>
        </div>

        <input type="hidden" name="tipoViaje" value="REGULAR">
        <input type="hidden" name="estado" value="PROGRAMADO">

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>
</body>
</html>
