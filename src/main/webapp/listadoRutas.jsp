<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Rutas</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Rutas</h1>
    <a href="${pageContext.servletContext.contextPath}/ruta?accion=nuevo" class="btn btn-success mb-3">+ Nueva Ruta</a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Origen</th>
            <th>Destino</th>
            <th>Distancia (km)</th>
            <th>Precio</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${rutas}">
            <tr>
                <td>${r.idRuta}</td>
                <td>${r.idSucursalOrigen}</td>
                <td>${r.idSucursalDestino}</td>
                <td>${r.distanciaKm}</td>
                <td>${r.precioBoleto}</td>
                <td>${r.estado}</td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/ruta?accion=editar&id=${r.idRuta}" class="btn btn-sm btn-warning">Editar</a>
                    <a href="${pageContext.servletContext.contextPath}/ruta?accion=eliminar&id=${r.idRuta}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('¿Seguro que quieres eliminar esta ruta?')">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>