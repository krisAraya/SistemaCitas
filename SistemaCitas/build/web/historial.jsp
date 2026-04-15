<%@ page import="java.util.List" %>
<%@ page import="com.sistemacitas.modelo.Cita" %>

<!DOCTYPE html>
<html>
<head>
    <title>Historial de Citas</title>
</head>
<body>

<h2>Historial de Citas</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Médico</th>
        <th>Fecha</th>
        <th>Hora</th>
        <th>Estado</th>
    </tr>

<%
    List<Cita> lista = (List<Cita>) request.getAttribute("citas");

    if (lista != null) {
        for (Cita c : lista) {
%>
    <tr>
        <td><%= c.getId_cita() %></td>
        <td><%= c.getId_medico() %></td>
        <td><%= c.getFecha() %></td>
        <td><%= c.getHora() %></td>
        <td><%= c.getEstado() %></td>
    </tr>
<%
        }
    }
%>

</table>

</body>
</html>