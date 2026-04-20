package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.dao.HorarioDAO;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/reprogramarCita")
public class ReprogramarCitaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario == null || !"medico".equals(usuario.getRol())) {
                response.sendRedirect("login.jsp");
                return;
            }

            int idCita = Integer.parseInt(request.getParameter("id_cita"));
            int idMedico = usuario.getId_usuario();
            Date fechaNueva = Date.valueOf(request.getParameter("fecha_nueva"));
            Time horaNueva = Time.valueOf(request.getParameter("hora_nueva") + ":00");

            HorarioDAO horarioDAO = new HorarioDAO();
            if (!horarioDAO.horarioValido(idMedico, fechaNueva, horaNueva)) {
                response.sendRedirect("historialMedico?error=horario");
                return;
            }

            CitaDAO citaDAO = new CitaDAO();
            boolean ok = citaDAO.reprogramarCita(idCita, idMedico, fechaNueva, horaNueva);

            if (ok) {
                response.sendRedirect("historialMedico?ok=reprogramada");
            } else {
                response.sendRedirect("historialMedico?error=ocupada");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("historialMedico?error=1");
        }
    }
}
