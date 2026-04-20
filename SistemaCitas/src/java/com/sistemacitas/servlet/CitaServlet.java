package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.dao.HorarioDAO;
import com.sistemacitas.modelo.Cita;
import com.sistemacitas.modelo.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/agendarCita")
public class CitaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String idMedicoStr = request.getParameter("id_medico");
            String fechaStr = request.getParameter("fecha");
            String horaStr = request.getParameter("hora");
            String especialidad = request.getParameter("especialidad");

            if (idMedicoStr == null || idMedicoStr.trim().isEmpty()
                    || fechaStr == null || fechaStr.trim().isEmpty()
                    || horaStr == null || horaStr.trim().isEmpty()) {
                response.sendRedirect("home?error=datos");
                return;
            }

            int id_paciente = usuario.getId_usuario();
            int id_medico = Integer.parseInt(idMedicoStr);
            Date fecha = Date.valueOf(fechaStr);
            Time hora = Time.valueOf(horaStr + ":00");

            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();
            LocalDate fechaCita = fecha.toLocalDate();
            LocalTime horaCita = hora.toLocalTime();

            if (fechaCita.isBefore(hoy)) {
                response.sendRedirect("home?error=fecha");
                return;
            }

            if (fechaCita.equals(hoy) && horaCita.isBefore(ahora)) {
                response.sendRedirect("home?error=hora");
                return;
            }

            HorarioDAO daoHorario = new HorarioDAO();
            boolean horarioValido = daoHorario.horarioValido(id_medico, fecha, hora);

            if (!horarioValido) {
                response.sendRedirect("home?error=horario&medico=" + id_medico);
                return;
            }

            CitaDAO dao = new CitaDAO();

            if (dao.horaOcupada(id_medico, fecha, hora)) {
                response.sendRedirect("home?error=ocupada&medico=" + id_medico);
                return;
            }

            Cita cita = new Cita();
            cita.setId_paciente(id_paciente);
            cita.setId_medico(id_medico);
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setEstado("pendiente");

            boolean resultado = dao.registrarCita(cita);

            String especialidadParam = "";
            if (especialidad != null && !especialidad.trim().isEmpty()) {
                especialidadParam = "&especialidad=" + URLEncoder.encode(especialidad, StandardCharsets.UTF_8.name());
            }

            if (resultado) {
                response.sendRedirect("home?ok=1&medico=" + id_medico + especialidadParam);
            } else {
                response.sendRedirect("home?error=1&medico=" + id_medico + especialidadParam);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home?error=1");
        }
    }
}
