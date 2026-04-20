package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.dao.HorarioDAO;
import com.sistemacitas.dao.UsuarioDAO;
import com.sistemacitas.modelo.Horario;
import com.sistemacitas.modelo.Usuario;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        String especialidad = request.getParameter("especialidad");
        String medicoStr = request.getParameter("medico");

        request.setAttribute("especialidades", usuarioDAO.listarEspecialidades());

        List<Usuario> medicos;
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            medicos = usuarioDAO.listarMedicoPorEspecialidad(especialidad);
        } else {
            medicos = usuarioDAO.listarMedico();
        }
        request.setAttribute("medicos", medicos);

        if (medicoStr != null && !medicoStr.isEmpty()) {
            int idMedico = Integer.parseInt(medicoStr);

            HorarioDAO horarioDAO = new HorarioDAO();
            List<Horario> horarios = horarioDAO.listarHorariosDisponibles(idMedico);
            request.setAttribute("horarios", horarios);

            CitaDAO citaDAO = new CitaDAO();
            Map<String, List<String>> horasOcupadas = citaDAO.listarHorasOcupadasPorMedico(idMedico);
            request.setAttribute("horasOcupadas", horasOcupadas);
        } else {
            request.setAttribute("horarios", Collections.emptyList());
        }

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}
