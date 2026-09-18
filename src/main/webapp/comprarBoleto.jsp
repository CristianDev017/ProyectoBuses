<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Comprar Boleto</title>
    <jsp:include page="/includes/resources.jsp"/>
    <style>
        .seat-btn {
            width: 52px;
            height: 52px;
            margin: 4px;
            border-radius: 10px;
            font-weight: bold;
        }
        .seat-btn.selected {
            background-color: #198754;
            color: white;
            border-color: #198754;
        }
        .seat-btn:disabled {
            cursor: not-allowed;
            opacity: 0.7;
        }
    </style>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="container mt-4">
    <h1>Comprar boleto - Viaje #${viaje.idViaje}</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="mb-3">Selecciona tus asientos</h4>
                    <p class="mb-3">
                        <strong>Ruta:</strong> ${origen.nombre} → ${destino.nombre}<br>
                        <strong>Fecha:</strong> ${fechaSalidaTexto} &nbsp; <strong>Hora:</strong> ${horaSalidaTexto}<br>
                        <strong>Bus:</strong> ${bus.placa} &nbsp; <strong>Capacidad:</strong> ${bus.capacidad} pasajeros<br>
                        <strong>Precio por boleto:</strong> Q${ruta.precioBoleto}
                    </p>

                    <div class="text-center mb-3">
                        <div class="d-inline-block p-2 border rounded bg-light">
                            <span class="badge bg-success me-2">Disponible</span>
                            <span class="badge bg-secondary me-2">Ocupado</span>
                            <span class="badge bg-primary">Seleccionado</span>
                        </div>
                    </div>

                    <div class="seat-layout text-center">
                        <c:forEach var="numeroAsiento" begin="1" end="${bus.capacidad}">
                            <c:set var="ocupado" value="false" />
                            <c:forEach var="ocupadoAsiento" items="${asientosOcupados}">
                                <c:if test="${ocupadoAsiento == numeroAsiento}">
                                    <c:set var="ocupado" value="true" />
                                </c:if>
                            </c:forEach>
                            <button type="button"
                                    class="seat-btn btn ${ocupado ? 'btn-secondary' : 'btn-success'}"
                                    data-seat="${numeroAsiento}"
                                    ${ocupado ? 'disabled' : ''}>
                                ${numeroAsiento}
                            </button>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4>Resumen antes de confirmar</h4>
                    <ul class="list-group list-group-flush mb-3">
                        <li class="list-group-item"><strong>Viaje:</strong> #${viaje.idViaje}</li>
                        <li class="list-group-item"><strong>Ruta:</strong> ${origen.nombre} → ${destino.nombre}</li>
                        <li class="list-group-item"><strong>Fecha y hora:</strong> ${fechaSalidaTexto} ${horaSalidaTexto}</li>
                        <li class="list-group-item"><strong>Bus:</strong> ${bus.placa}</li>
                        <li class="list-group-item"><strong>Asientos:</strong> <span id="selectedSeatsText">Ninguno</span></li>
                        <li class="list-group-item"><strong>Precio por boleto:</strong> Q${ruta.precioBoleto}</li>
                        <li class="list-group-item"><strong>Total a pagar:</strong> <span id="totalCompraText">Q0.00</span></li>
                        <li class="list-group-item"><strong>Saldo actual:</strong> Q${saldoActual}</li>
                    </ul>

                    <form method="post" action="${pageContext.servletContext.contextPath}/boleto" id="compraForm">
                        <input type="hidden" name="idViaje" value="${viaje.idViaje}">
                        <input type="hidden" name="asientos" id="selectedSeatsInput" value="">

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
    const saldoActual = Number('${saldoActual}');
    const selectedSeats = new Set();
    const buttons = document.querySelectorAll('.seat-btn:not(:disabled)');

    buttons.forEach(function(button) {
        button.addEventListener('click', function() {
            const seat = Number(button.dataset.seat);
            if (selectedSeats.has(seat)) {
                selectedSeats.delete(seat);
                button.classList.remove('selected');
                button.classList.add('btn-success');
            } else {
                selectedSeats.add(seat);
                button.classList.add('selected');
                button.classList.remove('btn-success');
                button.classList.add('btn-primary');
            }
            updateSummary();
        });
    });

    function updateSummary() {
        const seats = Array.from(selectedSeats).sort((a, b) => a - b);
        const total = seats.length * precioBoleto;
        const saldoRestante = saldoActual - total;

        document.getElementById('selectedSeatsText').textContent = seats.length ? seats.join(', ') : 'Ninguno';
        document.getElementById('selectedSeatsInput').value = seats.join(',');
        document.getElementById('totalCompraText').textContent = 'Q' + total.toFixed(2);

        const confirmButton = document.getElementById('confirmButton');
        confirmButton.disabled = seats.length === 0;
        confirmButton.textContent = seats.length === 0 ? 'Confirmar compra' : 'Confirmar compra de ' + seats.length + ' asiento(s)';
    }
</script>

<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
