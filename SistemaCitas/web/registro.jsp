<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro</title>

    <style>
        body {
            font-family: Arial;
            background: linear-gradient(to right, #43e97b, #38f9d7);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            width: 320px;
            text-align: center;
            box-shadow: 0px 0px 15px rgba(0,0,0,0.2);
        }

        input, select {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border-radius: 8px;
            border: 1px solid #ccc;
        }

        button {
            width: 100%;
            padding: 10px;
            background: #43e97b;
            border: none;
            color: white;
            border-radius: 8px;
            cursor: pointer;
        }

        button:hover {
            background: #28a745;
        }

        a {
            display: block;
            margin-top: 10px;
            text-decoration: none;
            color: #007bff;
        }

        .error {
            color: red;
            font-size: 14px;
        }
    </style>
</head>

<body>

<div class="card">

    <h2>Crear Cuenta</h2>

    <form action="registrar" method="post">

        <input type="text" name="nombre" placeholder="Nombre" required>

        <input type="email" name="correo" placeholder="Correo" required>

        <input type="password" name="password" placeholder="Contraseña" required 
        pattern="^(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{4,}$"
        title="Debe contener al menos 4 caracteres, una mayúscula, un número y un símbolo">

        <select name="rol" id="rol" required onchange="mostrarEspecialidad()">
            <option value="paciente">Paciente</option>
            <option value="medico">Médico</option>
        </select>

        <div id="especialidadDiv" style="display:none;">
            <select name="especialidad">
                <option value="Medicina General">Medicina General</option>
                <option value="Odontologia">Odontología</option>
                <option value="Dermatologia">Dermatología</option>
            </select>
        </div>

        <button type="submit">Registrarse</button>

    </form>

    <a href="login.jsp">Volver al login</a>

    <!-- 🔴 MENSAJES -->
    <% String msg = request.getParameter("msg"); %>

    <% if ("error".equals(msg)) { %>
        <p class="error">El correo ya está reg istrado o hubo un error</p>
    <% } %>

    <% if ("seguridad".equals(msg)) { %>
        <p class="error">Contraseña insegura</p>
    <% } %>

</div>

<script>
function mostrarEspecialidad() {
    var rol = document.getElementById("rol").value;
    var div = document.getElementById("especialidadDiv");

    div.style.display = (rol === "medico") ? "block" : "none";
}
</script>

</body>
</html>