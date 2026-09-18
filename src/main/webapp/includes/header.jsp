<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 13/9/2026
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark theme-header sticky-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.servletContext.contextPath}/">
            <span>
                <span class="brand-name">Code 'n Bugs</span>
                <small class="brand-subtitle">Transportes</small>
            </span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                <c:if test="${sessionScope.usuario.rol == 'ADMINISTRADOR'}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/sucursal">Sucursales</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/usuario">Usuarios</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/configuracion">Configuración</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/reporte">Reportes</a></li>
                </c:if>

                <c:if test="${sessionScope.usuario.rol == 'ADMINISTRADOR_DE_SUCURSAL'}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/bus">Buses</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/chofer">Choferes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/ruta">Rutas</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/viaje">Viajes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/reporte-sucursal">Reportes</a></li>
                </c:if>

                <c:if test="${sessionScope.usuario.rol == 'CLIENTE'}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/cartera">Mi Cartera</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/boleto">Viajes Disponibles</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.servletContext.contextPath}/boleto?accion=misBoletos">Mis Boletos</a></li>
                </c:if>

                <c:if test="${not empty sessionScope.usuario}">
                    <li class="nav-item ms-lg-2">
                        <a class="btn btn-outline-danger btn-sm" href="${pageContext.servletContext.contextPath}/logout">
                            <i class="bi bi-box-arrow-right me-1"></i>Cerrar sesión
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>