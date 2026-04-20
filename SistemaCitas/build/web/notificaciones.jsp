<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Notificaciones</title>
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
            border-radius: 12px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.12);
        }

        h2 {
            text-align: center;
        }

        ul {
            list-style: none;
            padding: 0;
        }

        li {
            background: #eef4ff;
            margin-bottom: 10px;
            padding: 12px;
            border-radius: 8px;
            border-left: 5px solid #2d6cdf;
        }

        .empty {
            text-align: center;
            color: #666;
        }

        .volver {
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Notificaciones</h2>

    <%
        List<String> lista = (List<String>) request.getAttribute("notificaciones");
        if (lista != null && !lista.isEmpty()) {
    %>
        <ul>
        <%
            for (String n : lista) {
        %>
            <li><%= n %></li>
        <%
            }
        %>
        </ul>
    <%
        } else {
    %>
        <p class="empty">No tienes notificaciones registradas.</p>
    <%
        }
    %>

    <a class="volver" href="home">Volver al inicio</a>
</div>

</body>
</html>
