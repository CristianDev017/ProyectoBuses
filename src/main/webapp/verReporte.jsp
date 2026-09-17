<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 17/9/2026
  Time: 00:25
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>${titulo}</title><jsp:include page="/includes/resources.jsp"/></head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>${titulo}</h1>

    <a href="${pageContext.servletContext.contextPath}/reporte?tipo=${tipo}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}&export=html"
       class="btn btn-danger mb-3">Exportar a HTML</a>
    <a href="${pageContext.servletContext.contextPath}/reporte" class="btn btn-secondary mb-3">Volver</a>

    <table class="table table-striped">
        <thead>
        <tr>
            <c:forEach var="h" items="${encabezados}"><th>${h}</th></c:forEach>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="fila" items="${datos}">
            <tr>
                <c:forEach var="valor" items="${fila}"><td>${valor}</td></c:forEach>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>