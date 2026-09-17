<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
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
    <h1>Viajes Disponibles</h1>
    <table class="table table-striped">
        <thead>
        <tr><th>ID</th><th>Ruta</th><th>Salida</th><th>Estado</th><th></th></tr>
        </thead>
        <tbody>
        <c:forEach var="v" items="${viajes}">
            <c:if test="${v.tipoViaje == 'REGULAR' and v.estado == 'PROGRAMADO'}">
                <tr>
                    <td>${v.idViaje}</td>
                    <td>${v.idRuta}</td>
                    <td><c:out value="${v.fechaSalida}"/></td>
                    <td>${v.estado}</td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/boleto?accion=comprar&idViaje=${v.idViaje}" class="btn btn-sm btn-primary">Comprar boleto</a>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
