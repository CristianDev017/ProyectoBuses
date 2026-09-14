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
            </c:if>

            <c:if test="${sessionScope.usuario.rol == 'ADMINISTRADOR_DE_SUCURSAL'}">
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/bus">Buses</a>
                <a class="btn btn-outline-light btn-sm" href="${pageContext.servletContext.contextPath}/chofer">Choferes</a>
            </c:if>
        </div>
    </div>
</nav>