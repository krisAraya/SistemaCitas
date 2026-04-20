package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Cita;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/historialMedico")
public class HistorialMedicoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"medico".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        CitaDAO dao = new CitaDAO();

        List<Cita> lista = dao.listarCitasPorMedico(usuario.getId_usuario());
        List<Cita> historialPacientes = dao.listarHistorialPacientesAtendidos(usuario.getId_usuario());

        request.setAttribute("citas", lista);
        request.setAttribute("historialPacientes", historialPacientes);
        request.getRequestDispatcher("historialMedico.jsp").forward(request, response);
    }
}
