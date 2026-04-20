package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/actualizarEstadoCita")
public class ActualizarEstadoCitaServlet extends HttpServlet {

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

            int id_cita = Integer.parseInt(request.getParameter("id_cita"));
            String estado = request.getParameter("estado");

            CitaDAO dao = new CitaDAO();
            dao.actualizarEstado(id_cita, estado);

            response.sendRedirect("historialMedico?ok=estado");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("historialMedico?error=1");
        }
    }
}
