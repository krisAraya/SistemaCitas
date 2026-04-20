<%@ page import="java.util.List" %>
<%@ page import="com.sistemacitas.modelo.Cita" %>
<%@ page import="com.sistemacitas.modelo.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
Usuario u = (Usuario) session.getAttribute("usuario");

if (u == null || !"medico".equals(u.getRol())) {
    response.sendRedirect("login.jsp");
    return;
}

List<Cita> lista = (List<Cita>) request.getAttribute("citas");
List<Cita> historialPacientes = (List<Cita>) request.getAttribute("historialPacientes");

String ok = request.getParameter("ok");
String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Panel del Medico</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 92%;
            max-width: 1200px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.12);
        }

        h1, h2, h3 {
            text-align: center;
            color: #1f2d3d;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 18px;
            margin-bottom: 30px;
        }

        th, td {
            border-bottom: 1px solid #ddd;
            padding: 10px;
            text-align: center;
            vertical-align: top;
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

        .estado {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            color: white;
            font-size: 13px;
            font-weight: bold;
            text-transform: capitalize;
        }

        .pendiente {
            background: #f0ad4e;
        }

        .confirmada {
            background: #28a745;
        }

        .cancelada {
            background: #dc3545;
        }

        .inline-form {
            margin-bottom: 8px;
        }

        input[type="date"],
        input[type="time"],
        textarea,
        button {
            width: 100%;
            box-sizing: border-box;
            padding: 8px;
            margin-top: 4px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }

        button {
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        .btn-danger {
            background: #dc3545;
        }

        .btn-secondary {
            background: #6c757d;
        }

        .small {
            font-size: 12px;
            color: #555;
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
    <h1>Sistema de Citas Medicas</h1>
    <h2>Panel del Medico</h2>

    <% if ("estado".equals(ok)) { %>
        <div class="mensaje-ok">El estado de la cita fue actualizado correctamente.</div>
    <% } %>

    <% if ("reprogramada".equals(ok)) { %>
        <div class="mensaje-ok">La cita fue reprogramada correctamente.</div>
    <% } %>

    <% if ("nota".equals(ok)) { %>
        <div class="mensaje-ok">La nota medica fue guardada correctamente.</div>
    <% } %>

    <% if ("ocupada".equals(error)) { %>
        <div class="mensaje-error">La nueva hora ya esta ocupada por otra cita.</div>
    <% } %>

    <% if ("horario".equals(error)) { %>
        <div class="mensaje-error">La nueva fecha y hora no estan dentro de tu horario de atencion.</div>
    <% } %>

    <% if ("1".equals(error)) { %>
        <div class="mensaje-error">Ocurrio un error al procesar la solicitud.</div>
    <% } %>

    <h3>Citas Solicitadas</h3>

    <table>
        <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Estado</th>
            <th>Acciones</th>
            <th>Reprogramar</th>
            <th>Notas Medicas</th>
        </tr>

        <%
        if (lista != null && !lista.isEmpty()) {
            for (Cita c : lista) {
                String claseEstado = c.getEstado() == null ? "" : c.getEstado();
        %>
        <tr>
            <td><%= c.getId_cita() %></td>
            <td><%= c.getNombrePaciente() %></td>
            <td><%= c.getFecha() %></td>
            <td><%= c.getHora() %></td>
            <td><span class="estado <%= claseEstado %>"><%= c.getEstado() %></span></td>

            <td>
                <% if ("pendiente".equals(c.getEstado())) { %>
                    <form action="<%= request.getContextPath() %>/actualizarEstadoCita" method="post" class="inline-form">
                        <input type="hidden" name="id_cita" value="<%= c.getId_cita() %>">
                        <input type="hidden" name="estado" value="confirmada">
                        <button type="submit">Aceptar</button>
                    </form>

                    <form action="<%= request.getContextPath() %>/actualizarEstadoCita" method="post" class="inline-form">
                        <input type="hidden" name="id_cita" value="<%= c.getId_cita() %>">
                        <input type="hidden" name="estado" value="cancelada">
                        <button type="submit" class="btn-danger">Rechazar</button>
                    </form>
                <% } else { %>
                    <span class="small">Procesada</span>
                <% } %>
            </td>

            <td>
                <form action="<%= request.getContextPath() %>/reprogramarCita" method="post">
                    <input type="hidden" name="id_cita" value="<%= c.getId_cita() %>">
                    <input type="date" name="fecha_nueva" required>
                    <input type="time" name="hora_nueva" required>
                    <button type="submit" class="btn-secondary">Reprogramar</button>
                </form>
            </td>

            <td>
                <form action="<%= request.getContextPath() %>/guardarNotaMedica" method="post">
                    <input type="hidden" name="id_cita" value="<%= c.getId_cita() %>">
                    <textarea name="notas" rows="4" placeholder="Diagnostico o recomendaciones"><%= c.getNotas() != null ? c.getNotas() : "" %></textarea>
                    <button type="submit">Guardar nota</button>
                </form>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="8">No hay citas registradas.</td>
        </tr>
        <%
        }
        %>
    </table>

    <h3>Historial de Pacientes Atendidos</h3>

    <table>
        <tr>
            <th>Paciente</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Estado</th>
            <th>Notas</th>
        </tr>

        <%
        if (historialPacientes != null && !historialPacientes.isEmpty()) {
            for (Cita c : historialPacientes) {
        %>
        <tr>
            <td><%= c.getNombrePaciente() %></td>
            <td><%= c.getFecha() %></td>
            <td><%= c.getHora() %></td>
            <td><%= c.getEstado() %></td>
            <td><%= c.getNotas() != null && !c.getNotas().trim().isEmpty() ? c.getNotas() : "Sin notas" %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5">Aun no hay pacientes atendidos.</td>
        </tr>
        <%
        }
        %>
    </table>

    <div class="acciones">
        <a href="home">Volver al inicio</a>
        <a href="guardarHorario">Ver y gestionar horario</a>
        <a href="notificaciones">Ver notificaciones</a>
    </div>
</div>

</body>
</html>
