<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Sistema de Citas</title>
</head>
<body>

    <h2>Iniciar Sesión</h2>

    <form action="login" method="post">
        <label>Correo:</label><br>
        <input type="email" name="correo" required><br><br>

        <label>password:</label><br>
        <input type="password" name="password" required><br><br>

        <button type="submit">Ingresar</button>
    </form>

    <% if (request.getParameter("error") != null) { %>
        <p style="color:red;">Correo o password incorrectos</p>
    <% } %>

</body>
</html>
