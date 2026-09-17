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
    <title>Mi Cartera</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Mi Cartera Digital</h1>
    <h3 class="text-success">Saldo actual: Q${cartera.saldo}</h3>

    <form method="post" action="${pageContext.servletContext.contextPath}/cartera" class="mt-4" style="max-width:400px;">
        <div class="mb-3">
            <label class="form-label">Monto a recargar</label>
            <input type="number" step="0.01" name="monto" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Fecha</label>
            <input type="date" name="fecha" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary">Recargar</button>
    </form>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
