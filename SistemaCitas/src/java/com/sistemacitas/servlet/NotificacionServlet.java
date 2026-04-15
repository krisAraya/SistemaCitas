package com.sistemacitas.servlet;

import com.sistemacitas.dao.CitaDAO;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/notificaciones")
public class NotificacionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        CitaDAO dao = new CitaDAO();
        List<String> lista = dao.listarNotificaciones(usuario.getId_usuario());

        request.setAttribute("notificaciones", lista);
        request.getRequestDispatcher("notificaciones.jsp").forward(request, response);
    }
}
