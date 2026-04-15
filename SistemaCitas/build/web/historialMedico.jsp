<%@ page import="java.util.List" %>
<%@ page import="com.sistemacitas.modelo.Cita" %>

<h2>Panel del Médico ????</h2>

<table border="1">
<tr>
    <th>ID</th>
    <th>Paciente</th>
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
    <td><%= c.getId_paciente() %></td>
    <td><%= c.getFecha() %></td>
    <td><%= c.getHora() %></td>
    <td><%= c.getEstado() %></td>
</tr>
<%
    }
}
%>

</table>