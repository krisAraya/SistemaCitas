package com.sistemacitas.servlet;

import com.sistemacitas.dao.UsuarioDAO;
import com.sistemacitas.modelo.Usuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/registrar")
public class RegistroServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Usuario u = new Usuario();

            u.setNombre(request.getParameter("nombre"));

            String correo = request.getParameter("correo").trim().toLowerCase();
            u.setCorreo(correo);

            u.setPassword(request.getParameter("password"));

            String rol = request.getParameter("rol").toLowerCase(); 
            u.setRol(rol);

            String especialidad = request.getParameter("especialidad");
            u.setEspecialidad(especialidad);

            UsuarioDAO dao = new UsuarioDAO();
            boolean resultado = dao.registrarUsuario(u);

            System.out.println("Registro resultado: " + resultado);

            if (resultado) {
                response.sendRedirect("login.jsp?ok=1");
            } else {
                response.sendRedirect("registro.jsp?msg=error");
            }

        } catch (IOException e) {
            response.sendRedirect("registro.jsp?msg=error");
        }
    }
}