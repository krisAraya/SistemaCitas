package com.sistemacitas.dao;

import com.sistemacitas.conexion.Conexion;
import com.sistemacitas.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String correo, String password) {

        Usuario usuario = null;
        String sql = "SELECT * FROM Usuarios WHERE LOWER(TRIM(correo)) = ? AND password = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo.trim().toLowerCase());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setEspecialidad(rs.getString("especialidad"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public boolean registrarUsuario(Usuario u) {

        String check = "SELECT COUNT(*) FROM Usuarios WHERE LOWER(TRIM(correo)) = ?";
        String insert = "INSERT INTO Usuarios (nombre, correo, password, rol, especialidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps1 = con.prepareStatement(check);
             PreparedStatement ps2 = con.prepareStatement(insert)) {

            String correo = u.getCorreo().trim().toLowerCase();

            ps1.setString(1, correo);

            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            String especialidad = u.getEspecialidad();
            if (especialidad != null && especialidad.trim().isEmpty()) {
                especialidad = null;
            }

            ps2.setString(1, u.getNombre());
            ps2.setString(2, correo);
            ps2.setString(3, u.getPassword());
            ps2.setString(4, u.getRol());
            ps2.setString(5, especialidad);

            ps2.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> listarMedico() {

        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE rol = 'medico' ORDER BY nombre";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setEspecialidad(rs.getString("especialidad"));
                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Usuario> listarMedicoPorEspecialidad(String especialidad) {

        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE rol = 'medico' AND especialidad = ? ORDER BY nombre";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, especialidad);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId_usuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setEspecialidad(rs.getString("especialidad"));
                    lista.add(u);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<String> listarEspecialidades() {

        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT especialidad FROM Usuarios "
                   + "WHERE rol = 'medico' AND especialidad IS NOT NULL AND TRIM(especialidad) <> '' "
                   + "ORDER BY especialidad";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("especialidad"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
