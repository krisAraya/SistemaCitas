package com.sistemacitas.servlet;

import com.sistemacitas.dao.HorarioDAO;
import com.sistemacitas.modelo.Horario;
import com.sistemacitas.modelo.Usuario;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/guardarHorario")
public class HorarioServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuario");

        int id_medico = u.getId_usuario();

        Date fecha = Date.valueOf(request.getParameter("fecha"));
        Time inicio = Time.valueOf(request.getParameter("hora_inicio") + ":00");
        Time fin = Time.valueOf(request.getParameter("hora_fin") + ":00");

        // 🔥 validación básica
        if (inicio.after(fin)) {
            response.sendRedirect("horariosMedico.jsp?error=hora");
            return;
        }

        Horario h = new Horario();
        h.setId_medico(id_medico);
        h.setFecha(fecha);
        h.setHora_inicio(inicio);
        h.setHora_fin(fin);

        HorarioDAO dao = new HorarioDAO();
        dao.guardarHorario(h);

        response.sendRedirect("horariosMedico.jsp?ok=1");
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();
    Usuario u = (Usuario) session.getAttribute("usuario");

    int id_medico = u.getId_usuario();

    HorarioDAO dao = new HorarioDAO();
    List<Horario> lista = dao.listarHorariosPorMedico(id_medico);

    request.setAttribute("horarios", lista);
    request.getRequestDispatcher("horariosMedico.jsp").forward(request, response);
}
}