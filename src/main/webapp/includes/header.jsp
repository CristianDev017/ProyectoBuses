<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 13/9/2026
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.servletContext.contextPath}/">Code 'n Bugs</a>
        <div>
            <c:if test="${sessionScope.usuario.rol == 'ADMINISTRADOR'}">
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/sucursal">Sucursales</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/usuario">Usuarios</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/configuracion">Configuración</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/reporte">Reportes</a>
            </c:if>

            <c:if test="${sessionScope.usuario.rol == 'ADMINISTRADOR_DE_SUCURSAL'}">
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/bus">Buses</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/chofer">Choferes</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/ruta">Rutas</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/viaje">Viajes</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/reporte-depreciacion">Reporte Depreciación</a>
            </c:if>

            <c:if test="${sessionScope.usuario.rol == 'CLIENTE'}">
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/cartera">Mi Cartera</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/boleto">Viajes Disponibles</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/boleto?accion=misBoletos">Mis Boletos</a>
            </c:if>

            <c:if test="${not empty sessionScope.usuario}">
                <a class="btn btn-outline-danger btn-sm" href="${pageContext.servletContext.contextPath}/logout">Cerrar sesión</a>
            </c:if>
        </div>
    </div>
</nav>