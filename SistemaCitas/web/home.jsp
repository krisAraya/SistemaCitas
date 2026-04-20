<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.time.LocalTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sistemacitas.modelo.Usuario" %>
<%@ page import="com.sistemacitas.modelo.Horario" %>

<%
Usuario u = (Usuario) session.getAttribute("usuario");

if (u == null) {
    response.sendRedirect("login.jsp");
    return;
}

String especialidadSeleccionada = request.getParameter("especialidad");
String medicoSeleccionado = request.getParameter("medico");

List<String> especialidades = (List<String>) request.getAttribute("especialidades");
List<Usuario> medicos = (List<Usuario>) request.getAttribute("medicos");
List<Horario> horarios = (List<Horario>) request.getAttribute("horarios");
Map<String, List<String>> horasOcupadas = (Map<String, List<String>>) request.getAttribute("horasOcupadas");

String ok = request.getParameter("ok");
String error = request.getParameter("error");

DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Sistema de Citas</title>
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

        h1, h2, h3 {
            text-align: center;
            color: #1f2d3d;
        }

        label {
            font-weight: bold;
        }

        select, button {
            padding: 10px;
            margin-top: 8px;
            width: 100%;
            border-radius: 8px;
            border: 1px solid #ccc;
        }

        button {
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 15px;
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

        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 18px;
            margin-top: 18px;
        }

        .day-card {
            background: #f8fafc;
            border: 1px solid #dbe4ee;
            border-radius: 12px;
            padding: 15px;
        }

        .day-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 12px;
            color: #1a4b84;
            text-align: center;
        }

        .slots {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            justify-content: center;
        }

        .slot-btn {
            padding: 8px 12px;
            border: 1px solid #007bff;
            background: white;
            color: #007bff;
            border-radius: 20px;
            cursor: pointer;
            font-size: 14px;
            width: auto;
        }

        .slot-btn.selected {
            background: #28a745;
            border-color: #28a745;
            color: white;
        }

        .resumen {
            margin-top: 20px;
            padding: 15px;
            background: #eef6ff;
            border: 1px solid #cfe2ff;
            border-radius: 10px;
        }

        .acciones {
            margin-top: 20px;
        }

        .enlaces {
            text-align: center;
            margin-top: 20px;
        }

        .enlaces a {
            margin: 0 10px;
            text-decoration: none;
            color: #007bff;
        }
    </style>

    <script>
        function seleccionarHorario(fecha, hora, boton) {
            document.getElementById("fechaSeleccionada").value = fecha;
            document.getElementById("horaSeleccionada").value = hora;
            document.getElementById("textoFecha").innerText = fecha;
            document.getElementById("textoHora").innerText = hora;

            let botones = document.querySelectorAll(".slot-btn");
            botones.forEach(function(b) {
                b.classList.remove("selected");
            });

            boton.classList.add("selected");
        }

        function validarEnvio() {
            const medico = document.getElementById("idMedico").value;
            const fecha = document.getElementById("fechaSeleccionada").value;
            const hora = document.getElementById("horaSeleccionada").value;

            if (!medico) {
                alert("Seleccione un medico.");
                return false;
            }

            if (!fecha || !hora) {
                alert("Seleccione un horario disponible.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body>

<div class="container">
    <h1>Sistema de Citas Medicas</h1>

    <% if ("1".equals(ok)) { %>
        <div class="mensaje-ok">
            La cita fue confirmada en linea y registrada correctamente.
        </div>
    <% } %>

    <% if ("1".equals(error)) { %>
        <div class="mensaje-error">Ocurrio un error al registrar la cita.</div>
    <% } %>

    <% if ("fecha".equals(error)) { %>
        <div class="mensaje-error">No puedes agendar una cita en una fecha pasada.</div>
    <% } %>

    <% if ("hora".equals(error)) { %>
        <div class="mensaje-error">No puedes agendar una cita en una hora pasada.</div>
    <% } %>

    <% if ("horario".equals(error)) { %>
        <div class="mensaje-error">La hora seleccionada no pertenece al horario disponible del medico.</div>
    <% } %>

    <% if ("ocupada".equals(error)) { %>
        <div class="mensaje-error">Esa hora ya fue tomada por otro paciente. Selecciona otro horario.</div>
    <% } %>

    <% if ("datos".equals(error)) { %>
        <div class="mensaje-error">Debes seleccionar especialidad, medico y horario antes de confirmar.</div>
    <% } %>

    <% if (u.getRol().equals("paciente")) { %>

        <h2>Agendar Cita</h2>

        <form method="get" action="home">
            <label>Especialidad</label>
            <select name="especialidad" onchange="this.form.submit()">
                <option value="">Seleccione una especialidad</option>
                <%
                if (especialidades != null) {
                    for (String esp : especialidades) {
                        String selected = "";
                        if (especialidadSeleccionada != null && especialidadSeleccionada.equals(esp)) {
                            selected = "selected";
                        }
                %>
                    <option value="<%= esp %>" <%= selected %>><%= esp %></option>
                <%
                    }
                }
                %>
            </select>

            <label>Medico</label>
            <select name="medico" onchange="this.form.submit()">
                <option value="">Seleccione un medico</option>
                <%
                if (medicos != null) {
                    for (Usuario m : medicos) {
                        String selected = "";
                        if (medicoSeleccionado != null && medicoSeleccionado.equals(String.valueOf(m.getId_usuario()))) {
                            selected = "selected";
                        }
                %>
                    <option value="<%= m.getId_usuario() %>" <%= selected %>>
                        Dr(a). <%= m.getNombre() %><%= (m.getEspecialidad() != null && !m.getEspecialidad().trim().isEmpty()) ? " - " + m.getEspecialidad() : "" %>
                    </option>
                <%
                    }
                }
                %>
            </select>
        </form>

        <form action="agendarCita" method="post" onsubmit="return validarEnvio();">
            <input type="hidden" name="especialidad" value="<%= especialidadSeleccionada != null ? especialidadSeleccionada : "" %>">
            <input type="hidden" name="id_medico" id="idMedico" value="<%= medicoSeleccionado != null ? medicoSeleccionado : "" %>">
            <input type="hidden" name="fecha" id="fechaSeleccionada">
            <input type="hidden" name="hora" id="horaSeleccionada">

            <h3>Calendario de Horarios Disponibles</h3>

            <%
            if (horarios != null && !horarios.isEmpty()) {
            %>
                <div class="calendar-grid">
                    <%
                    for (Horario h : horarios) {
                        String fechaClave = h.getFecha().toString();
                        LocalTime inicio = h.getHora_inicio().toLocalTime();
                        LocalTime fin = h.getHora_fin().toLocalTime();
                    %>
                        <div class="day-card">
                            <div class="day-title"><%= h.getFecha() %></div>
                            <div class="slots">
                                <%
                                LocalTime actual = inicio;
                                while (actual.isBefore(fin)) {
                                    String horaTexto = actual.format(formatoHora);
                                    boolean ocupada = false;

                                    if (horasOcupadas != null && horasOcupadas.containsKey(fechaClave)) {
                                        ocupada = horasOcupadas.get(fechaClave).contains(horaTexto);
                                    }

                                    if (!ocupada) {
                                %>
                                    <button type="button" class="slot-btn"
                                            onclick="seleccionarHorario('<%= h.getFecha() %>', '<%= horaTexto %>', this)">
                                        <%= horaTexto %>
                                    </button>
                                <%
                                    }
                                    actual = actual.plusMinutes(30);
                                }
                                %>
                            </div>
                        </div>
                    <%
                    }
                    %>
                </div>
            <%
            } else if (medicoSeleccionado != null && !medicoSeleccionado.isEmpty()) {
            %>
                <p style="text-align:center;">Este medico no tiene horarios disponibles.</p>
            <%
            } else {
            %>
                <p style="text-align:center;">Seleccione una especialidad y un medico para ver disponibilidad.</p>
            <%
            }
            %>

            <div class="resumen">
                <strong>Horario seleccionado:</strong><br>
                Fecha: <span id="textoFecha">---</span><br>
                Hora: <span id="textoHora">---</span>
            </div>

            <div class="acciones">
                <button type="submit">Confirmar Cita</button>
            </div>
        </form>

        <div class="enlaces">
            <a href="historial">Ver mis citas</a>
            <a href="notificaciones">Ver notificaciones</a>
        </div>

    <% } else if (u.getRol().equals("medico")) { %>

        <h2>Panel del Medico</h2>
        <div class="enlaces">
            <a href="historialMedico">Ver citas de pacientes</a>
            <a href="guardarHorario">Ver y gestionar horario</a>
            <a href="notificaciones">Ver notificaciones</a>
        </div>

    <% } %>

    <div class="enlaces">
        <a href="logout">Cerrar sesion</a>
    </div>
</div>

</body>
</html>
