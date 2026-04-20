package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/guardarNotaMedica")
public class GuardarNotaMedicaServlet extends HttpServlet {

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
            String notas = request.getParameter("notas");

            CitaDAO dao = new CitaDAO();
            boolean ok = dao.guardarNotaMedica(idCita, usuario.getId_usuario(), notas);

            if (ok) {
                response.sendRedirect("historialMedico?ok=nota");
            } else {
                response.sendRedirect("historialMedico?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("historialMedico?error=1");
        }
    }
}
