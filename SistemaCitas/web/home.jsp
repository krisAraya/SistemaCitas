<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sistemacitas.modelo.Usuario" %>

<%
Usuario u = (Usuario) session.getAttribute("usuario");
if (u == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <title>Sistema de Citas</title>

    <style>
        body {
            font-family: Arial;
            background-color: #f4f6f9;
            text-align: center;
        }

        .container {
            width: 50%;
            margin: auto;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px gray;
        }

        input, button {
            padding: 10px;
            margin: 5px;
            width: 80%;
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        a {
            display: block;
            margin: 10px;
            text-decoration: none;
            color: #007bff;
        }

        .success { color: green; }
        .error { color: red; }
    </style>

</head>

<body>



<h1>🏥 Sistema de Citas Médicas</h1>

<% if (request.getParameter("ok") != null) { %>
    <p class="success">Cita registrada correctamente</p>
<% } %>

<% if (request.getParameter("error") != null) { %>
    <p class="error">Error al registrar la cita</p>
<% } %>

<!-- PACIENTE -->
<% if (u.getRol().equals("paciente")) { %>

    <h2>Agendar Cita</h2>

    <form action="agendarCita" method="post">
        <input type="number" name="id_medico" placeholder="ID Médico" required>
        <input type="date" name="fecha" required>
        <input type="time" name="hora" required>
        <button type="submit">Agendar</button>
    </form>

    <a href="historial">📋 Ver Historial</a>
    <a href="notificaciones">🔔 Notificaciones</a>

<!-- MÉDICO -->
<% } else { %>

    <h2>👨‍⚕️ Panel del Médico</h2>

    <a href="historialMedico">📋 Ver citas</a>
    <a href="notificaciones">🔔 Notificaciones</a>

<% } %>

<% if ("fecha".equals(request.getParameter("error"))) { %>
    <p style="color:red;">No puedes agendar en fechas pasadas</p>
<% } %>

<% if ("hora".equals(request.getParameter("error"))) { %>
    <p style="color:red;">No puedes agendar en horas pasadas</p>
<% } %>

</body>
</html>