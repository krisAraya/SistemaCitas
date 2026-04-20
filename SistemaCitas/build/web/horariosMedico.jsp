<%@page import="java.util.List"%>
<%@ page import="com.sistemacitas.modelo.Usuario" %>
<%@ page import="com.sistemacitas.modelo.Horario" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
Usuario u = (Usuario) session.getAttribute("usuario");
if (u == null || !"medico".equals(u.getRol())) {
    response.sendRedirect("login.jsp");
    return;
}

String ok = request.getParameter("ok");
String error = request.getParameter("error");
List<Horario> lista = (List<Horario>) request.getAttribute("horarios");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Horarios del Medico</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 80%;
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.12);
        }

        h2, h3 {
            text-align: center;
        }

        input, button {
            width: 100%;
            padding: 10px;
            margin-top: 8px;
            box-sizing: border-box;
            border-radius: 8px;
            border: 1px solid #ccc;
        }

        button {
            background: #007bff;
            color: white;
            border: none;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 18px;
        }

        th, td {
            border-bottom: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }

        th {
            background: #007bff;
            color: white;
        }

        .mensaje-ok {
            background: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .mensaje-error {
            background: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 15px;
        }

        .acciones {
            text-align: center;
            margin-top: 20px;
        }

        a {
            text-decoration: none;
            color: #007bff;
            margin: 0 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Definir Horario de Atencion</h2>

    <% if ("1".equals(ok)) { %>
        <div class="mensaje-ok">Horario guardado correctamente.</div>
    <% } %>

    <% if ("hora".equals(error)) { %>
        <div class="mensaje-error">La hora de inicio no puede ser mayor que la hora final.</div>
    <% } %>

    <form action="guardarHorario" method="post">
        <label>Fecha</label>
        <input type="date" name="fecha" required>

        <label>Hora inicio</label>
        <input type="time" name="hora_inicio" required>

        <label>Hora fin</label>
        <input type="time" name="hora_fin" required>

        <button type="submit">Guardar horario</button>
    </form>

    <h3>Mis Horarios</h3>

    <table>
        <tr>
            <th>Fecha</th>
            <th>Inicio</th>
            <th>Fin</th>
        </tr>

        <%
        if (lista != null && !lista.isEmpty()) {
            for (Horario h : lista) {
        %>
        <tr>
            <td><%= h.getFecha() %></td>
            <td><%= h.getHora_inicio() %></td>
            <td><%= h.getHora_fin() %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="3">No has definido horarios todavia.</td>
        </tr>
        <%
        }
        %>
    </table>

    <div class="acciones">
        <a href="home">Volver al inicio</a>
        <a href="historialMedico">Ver citas</a>
    </div>
</div>

</body>
</html>
