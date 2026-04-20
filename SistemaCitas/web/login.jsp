<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>

    <style>
        body {
            font-family: Arial;
            background: linear-gradient(to right, #4facfe, #00f2fe);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            width: 300px;
            text-align: center;
            box-shadow: 0px 0px 15px rgba(0,0,0,0.2);
        }

        input {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 8px;
            border: 1px solid #ccc;
        }

        button {
            width: 100%;
            padding: 10px;
            background: #4facfe;
            border: none;
            color: white;
            border-radius: 8px;
            cursor: pointer;
        }

        button:hover {
            background: #007bff;
        }

        a {
            display: block;
            margin-top: 10px;
            color: #007bff;
            text-decoration: none;
        }

        .error {
            color: red;
        }
    </style>
</head>

<body>

<div class="card">
    <h2>Iniciar Sesión</h2>

    <form action="login" method="post">
        <input type="email" name="correo" placeholder="Correo" required>
        <input type="password" name="password" placeholder="Contraseña" required>
        <button type="submit">Ingresar</button>
    </form>

    <a href="registro.jsp">Crear cuenta</a>

    <% if (request.getParameter("error") != null) { %>
        <p class="error">Credenciales incorrectas</p>
    <% } %>
</div>

</body>
</html>