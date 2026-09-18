<%--
  Created by IntelliJ IDEA.
  User: crist
  Date: 13/9/2026
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>
    :root {
        --primary-900: #0f172a;
        --primary-800: #14213d;
        --primary-700: #1d3557;
        --primary-500: #2563eb;
        --accent: #f59e0b;
        --soft: #f8fafc;
        --muted: #6b7280;
        --card-border: rgba(148, 163, 184, 0.3);
    }

    html, body {
        height: 100%;
        background: #f8fafc;
    }

    body {
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        color: var(--primary-900);
        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    }

    .container,
    .container-fluid {
        flex: 1 0 auto;
    }

    .theme-header {
        background: #0f172a;
        box-shadow: 0 4px 16px rgba(15, 23, 42, 0.18);
    }

    .brand-name {
        font-size: 1.1rem;
        font-weight: 700;
        color: #fff;
        letter-spacing: 0.02em;
    }

    .brand-subtitle {
        display: block;
        font-size: 0.68rem;
        color: rgba(255,255,255,0.75);
        letter-spacing: 0.08em;
        text-transform: uppercase;
    }

    .theme-header .nav-link {
        color: rgba(255,255,255,0.85) !important;
        font-weight: 500;
        padding: 0.55rem 0.8rem !important;
        border-radius: 8px;
    }

    .theme-header .nav-link:hover,
    .theme-header .nav-link:focus {
        color: #fff !important;
        background: rgba(255,255,255,0.08);
    }

    .theme-header .btn-outline-light,
    .theme-header .btn-outline-danger {
        border-radius: 8px;
        font-weight: 600;
        padding: 0.5rem 0.9rem;
    }

    .theme-footer {
        background: #0f172a;
        color: #e5e7eb;
        margin-top: 4rem;
        border-top: 1px solid rgba(255,255,255,0.08);
    }

    .theme-footer .brand-highlight {
        color: #fbbf24;
        font-weight: 700;
        letter-spacing: 0.03em;
    }

    .theme-footer a {
        color: #dbeafe;
        text-decoration: none;
    }

    .theme-footer a:hover {
        color: #fff;
    }

    .theme-footer .social-btn {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border: 1px solid rgba(255,255,255,0.18);
        color: #fff;
    }

    .theme-footer .social-btn:hover {
        background: rgba(255,255,255,0.04);
    }

    .page-card {
        background: rgba(255,255,255,0.78);
        border: 1px solid var(--card-border);
        border-radius: 16px;
        box-shadow: 0 8px 22px rgba(15, 23, 42, 0.06);
    }
</style>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
