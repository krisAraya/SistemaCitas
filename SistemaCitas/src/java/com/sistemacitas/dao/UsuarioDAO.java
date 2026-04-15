package com.sistemacitas.dao;

import com.sistemacitas.conexion.Conexion;
import com.sistemacitas.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario login(String correo, String password) {

        Usuario usuario = null;

        String sql = "SELECT * FROM Usuarios WHERE correo = ? AND password = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setpassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
            }

        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
        }

        return usuario;
    }
}
