package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Cita;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"paciente".equals(usuario.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        CitaDAO dao = new CitaDAO();
        List<Cita> lista = dao.listarCitasPorPaciente(usuario.getId_usuario());

        request.setAttribute("citas", lista);
        request.getRequestDispatcher("historial.jsp").forward(request, response);
    }
}
