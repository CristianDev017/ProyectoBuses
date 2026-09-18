<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Comprar Boleto</title>
    <jsp:include page="/includes/resources.jsp"/>
    <style>
        .seat-btn {
            width: 46px;
            height: 46px;
            margin: 4px;
            font-weight: bold;
        }
        .seat-btn.selected {
            background: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h2>Compra de boleto</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="row">
        <div class="col-md-7">
            <div class="card">
                <div class="card-body">
                    <p><strong>Ruta:</strong> ${origen.nombre} → ${destino.nombre}</p>
                    <p><strong>Fecha:</strong> ${fechaSalidaTexto} &nbsp; <strong>Hora:</strong> ${horaSalidaTexto}</p>
                    <p><strong>Bus:</strong> ${bus.placa} &nbsp; <strong>Precio:</strong> Q${ruta.precioBoleto}</p>

                    <div class="mb-3">
                        <span class="badge bg-success">Disponible</span>
                        <span class="badge bg-secondary">Ocupado</span>
                        <span class="badge bg-primary">Seleccionado</span>
                    </div>

                    <div class="text-center">
                        <c:forEach var="numeroAsiento" begin="1" end="${bus.capacidad}">
                            <c:set var="ocupado" value="false" />
                            <c:forEach var="ocupadoAsiento" items="${asientosOcupados}">
                                <c:if test="${ocupadoAsiento == numeroAsiento}">
                                    <c:set var="ocupado" value="true" />
                                </c:if>
                            </c:forEach>
                            <button type="button" class="btn seat-btn ${ocupado ? 'btn-secondary' : 'btn-success'}"
                                    data-seat="${numeroAsiento}"
                                    ${ocupado ? 'disabled' : ''}>${numeroAsiento}</button>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-5">
            <div class="card">
                <div class="card-body">
                    <h5>Resumen</h5>
                    <p><strong>Viaje:</strong> #${viaje.idViaje}</p>
                    <p><strong>Ruta:</strong> ${origen.nombre} → ${destino.nombre}</p>
                    <p><strong>Fecha/hora:</strong> ${fechaSalidaTexto} ${horaSalidaTexto}</p>
                    <p><strong>Bus:</strong> ${bus.placa}</p>
                    <p><strong>Asientos:</strong> <span id="selectedSeatsText">Ninguno</span></p>
                    <p><strong>Precio por boleto:</strong> Q${ruta.precioBoleto}</p>
                    <p><strong>Total:</strong> <span id="totalCompraText">Q0.00</span></p>
                    <p><strong>Saldo actual:</strong> Q${saldoActual}</p>

                    <form method="post" action="${pageContext.servletContext.contextPath}/boleto">
                        <input type="hidden" name="idViaje" value="${viaje.idViaje}">
                        <input type="hidden" name="asientos" id="selectedSeatsInput" value="${asientosSeleccionadosTexto}">

                        <div class="mb-3">
                            <label class="form-label">Fecha de pago</label>
                            <input type="date" name="fechaPago" class="form-control" required>
                        </div>

                        <button type="submit" class="btn btn-primary w-100" id="confirmButton" disabled>Confirmar compra</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const precioBoleto = Number('${ruta.precioBoleto}');
    const selectedSeats = new Set();
    const initialSeats = document.getElementById('selectedSeatsInput').value;
    if (initialSeats) {
        initialSeats.split(',').forEach(function(value) {
            const seat = Number(value.trim());
            if (!Number.isNaN(seat)) selectedSeats.add(seat);
        });
    }

    document.querySelectorAll('.seat-btn:not(:disabled)').forEach(function(button) {
        const seat = Number(button.dataset.seat);
        if (selectedSeats.has(seat)) {
            button.classList.remove('btn-success');
            button.classList.add('btn-primary');
            button.classList.add('selected');
        }

        button.addEventListener('click', function() {
            if (selectedSeats.has(seat)) {
                selectedSeats.delete(seat);
                button.classList.remove('selected');
                button.classList.remove('btn-primary');
                button.classList.add('btn-success');
            } else {
                selectedSeats.add(seat);
                button.classList.add('selected');
                button.classList.remove('btn-success');
                button.classList.add('btn-primary');
            }
            actualizarResumen();
        });
    });

    function actualizarResumen() {
        const seats = Array.from(selectedSeats).sort((a, b) => a - b);
        const total = seats.length * precioBoleto;
        document.getElementById('selectedSeatsText').textContent = seats.length ? seats.join(', ') : 'Ninguno';
        document.getElementById('selectedSeatsInput').value = seats.join(',');
        document.getElementById('totalCompraText').textContent = 'Q' + total.toFixed(2);
        const boton = document.getElementById('confirmButton');
        boton.disabled = seats.length === 0;
    }

    actualizarResumen();
</script>

<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
