<%@page import="java.util.List"%>
<%@page import="com.sistemacitas.modelo.Cita"%>
<%@page import="com.sistemacitas.modelo.Usuario"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
Usuario u = (Usuario) session.getAttribute("usuario");

if (u == null) {
    response.sendRedirect("login.jsp");
    return;
}

List<Cita> citas = (List<Cita>) request.getAttribute("citas");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Historial de Citas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 85%;
            max-width: 1000px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.12);
        }

        h1, h2 {
            text-align: center;
            color: #1f2d3d;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }

        th {
            background: #007bff;
            color: white;
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

        .medico-info {
            line-height: 1.4;
        }

        .medico-nombre {
            font-weight: bold;
            color: #1f2d3d;
        }

        .medico-especialidad {
            color: #6c757d;
            font-size: 13px;
        }

        .vacio {
            text-align: center;
            margin-top: 20px;
            color: #666;
            font-size: 16px;
        }

        .acciones {
            text-align: center;
            margin-top: 25px;
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
    <h2>Mi Historial de Citas</h2>

    <%
    if (citas != null && !citas.isEmpty()) {
    %>
        <table>
            <tr>
                <th>ID Cita</th>
                <th>Especialidad</th>
                <th>Medico</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Estado</th>
            </tr>

            <%
            for (Cita c : citas) {
                String claseEstado = c.getEstado() == null ? "" : c.getEstado();
            %>
            <tr>
                <td><%= c.getId_cita() %></td>
                <td><%= c.getEspecialidadMedico() != null ? c.getEspecialidadMedico() : "Sin especialidad" %></td>
                <td>
                    <div class="medico-info">
                        <div class="medico-nombre">Dr(a). <%= c.getNombreMedico() %></div>
                    </div>
                </td>
                <td><%= c.getFecha() %></td>
                <td><%= c.getHora() %></td>
                <td>
                    <span class="estado <%= claseEstado %>"><%= c.getEstado() %></span>
                </td>
            </tr>
            <%
            }
            %>
        </table>
    <%
    } else {
    %>
        <p class="vacio">Aun no tienes citas registradas.</p>
    <%
    }
    %>

    <div class="acciones">
        <a href="home">Volver al inicio</a>
        <a href="notificaciones">Ver notificaciones</a>
    </div>
</div>

</body>
</html>
