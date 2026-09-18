<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 16/9/2026
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<footer class="theme-footer">
    <div class="container py-5">
        <div class="row g-4 align-items-center">
            <div class="col-lg-4">
                <div class="mb-3">
                    <h5 class="brand-highlight mb-0">Code 'n Bugs</h5>
                </div>
                <p class="text-secondary mb-0">
                    Viajes seguros, rutas confiables y atención al cliente que acompaña cada trayecto.
                </p>
            </div>

            <div class="col-sm-6 col-lg-3">
                <h6 class="text-uppercase fw-semibold mb-3 text-warning">Navegación</h6>
                <ul class="list-unstyled mb-0">
                    <li class="mb-2"><a href="${pageContext.servletContext.contextPath}/">Inicio</a></li>
                    <li class="mb-2"><a href="${pageContext.servletContext.contextPath}/boleto">Viajes</a></li>
                    <li class="mb-2"><a href="${pageContext.servletContext.contextPath}/cartera">Mi cartera</a></li>
                </ul>
            </div>

            <div class="col-sm-6 col-lg-3">
                <h6 class="text-uppercase fw-semibold mb-3 text-warning">Atención</h6>
                <ul class="list-unstyled mb-0 text-secondary">
                    <li class="mb-2">+502 3121-1761</li>
                    <li class="mb-2">soporte@codenbugs.com</li>
                    <li>Guatemala, GT</li>
                </ul>
            </div>

            <div class="col-lg-2 text-lg-end">
                <h6 class="text-uppercase fw-semibold mb-3 text-warning">Síguenos</h6>
                <div class="d-flex gap-2 justify-content-lg-end">
                    <a href="#" class="social-btn" aria-label="Facebook"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="social-btn" aria-label="Instagram"><i class="bi bi-instagram"></i></a>
                    <a href="#" class="social-btn" aria-label="X"><i class="bi bi-twitter-x"></i></a>
                    <a href="#" class="social-btn" aria-label="WhatsApp"><i class="bi bi-whatsapp"></i></a>
                </div>
            </div>
        </div>
    </div>

    <div class="border-top border-secondary">
        <div class="container py-3 text-center text-secondary small mb-0">
            © 2026 Code 'n Bugs. Todos los derechos reservados.
        </div>
    </div>
</footer>
