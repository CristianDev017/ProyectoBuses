<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 14/9/2026
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Chofer</title>
    <jsp:include page="/includes/resources.jsp"/>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>

<div class="container mt-4">
    <h1>Registrar Chofer</h1>

    <form method="post" action="${pageContext.servletContext.contextPath}/chofer">

        <div class="mb-3">
            <label for="foto" class="form-label">URL Foto</label>
            <input type="text" id="foto" name="foto" class="form-control">
        </div>

        <div class="mb-3">
            <label for="nombreCompleto" class="form-label">Nombre completo</label>
            <input type="text" id="nombreCompleto" name="nombreCompleto" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="numeroLicencia" class="form-label">Número de licencia</label>
            <input type="text" id="numeroLicencia" name="numeroLicencia" class="form-control" required>
        </div>

        <div class="mb-3">
            <label for="tipoLicencia" class="form-label">Tipo de licencia</label>
            <input type="text" id="tipoLicencia" name="tipoLicencia" class="form-control">
        </div>

        <div class="mb-3">
            <label for="fechaVencimiento" class="form-label">Fecha de vencimiento</label>
            <input type="date" id="fechaVencimiento" name="fechaVencimiento" class="form-control">
        </div>

        <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
            <input type="text" id="telefono" name="telefono" class="form-control">
        </div>

        <div class="mb-3">
            <label for="salarioBaseViaje" class="form-label">Salario base por viaje</label>
            <input type="number" step="0.01" id="salarioBaseViaje" name="salarioBaseViaje" class="form-control">
        </div>

        <button type="submit" class="btn btn-primary mt-3">Guardar</button>
    </form>
</div>
</body>
</html>
