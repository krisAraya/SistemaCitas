package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Cita;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.time.LocalDate;

@WebServlet("/agendarCita")
public class CitaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // obtener usuario logueado
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            int id_paciente = usuario.getId_usuario();

            // datos del formulario
            int id_medico = Integer.parseInt(request.getParameter("id_medico"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            LocalDate hoy = LocalDate.now();
            LocalDate fechaCita = fecha.toLocalDate();

                if (fechaCita.isBefore(hoy)) {
                    response.sendRedirect("home.jsp?error=fecha");
                    return;
                }
            String horaStr = request.getParameter("hora");
            Time hora = Time.valueOf(horaStr + ":00");

            // crear objeto cita
            Cita cita = new Cita();
            cita.setId_paciente(id_paciente);
            cita.setId_medico(id_medico);
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setEstado("pendiente");

            // guardar
            CitaDAO dao = new CitaDAO();
            boolean resultado = dao.registrarCita(cita);

            if (resultado) {
                response.sendRedirect("home.jsp?ok=1");
            } else {
                response.sendRedirect("home.jsp?error=1");
            }

        } catch (Exception e) {
            System.out.println("Error en servlet: " + e.getMessage());
            response.sendRedirect("home.jsp?error=1");
        }
    }
}