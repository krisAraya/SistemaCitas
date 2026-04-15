<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Notificaciones</title>
</head>
<body>

<h2>Notificaciones</h2>

<ul>
<%
    List<String> lista = (List<String>) request.getAttribute("notificaciones");

    if (lista != null) {
        for (String n : lista) {
%>
    <li><%= n %></li>
<%
        }
    }
%>
</ul>

</body>
</html>