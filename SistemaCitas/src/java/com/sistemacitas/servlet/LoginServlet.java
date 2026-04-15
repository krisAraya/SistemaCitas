package com.sistemacitas.servlet;

import com.sistemacitas.dao.UsuarioDAO;
import com.sistemacitas.modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(correo, password);

        if (usuario != null) {
            // login correcto
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);

            response.sendRedirect("home.jsp");

        } else {
            // login incorrecto
            response.sendRedirect("login.jsp?error=1");
        }
    }
}