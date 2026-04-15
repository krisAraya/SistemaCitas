package com.sistemacitas.dao;

import com.sistemacitas.conexion.Conexion;
import com.sistemacitas.modelo.Cita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // 🔹 REGISTRAR CITA + NOTIFICACIÓN
    public boolean registrarCita(Cita cita) {

        String sql = "INSERT INTO Citas (id_paciente, id_medico, fecha, hora, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cita.getId_paciente());
            ps.setInt(2, cita.getId_medico());
            ps.setDate(3, cita.getFecha());
            ps.setTime(4, cita.getHora());
            ps.setString(5, cita.getEstado());

            ps.executeUpdate();

            // 🔥 OBTENER ID DE LA CITA
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id_cita = rs.getInt(1);

                // 🔔 REGISTRAR NOTIFICACIÓN
                registrarNotificacion(cita.getId_paciente(), id_cita, "Cita creada");
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 LISTAR CITAS DEL PACIENTE
    public List<Cita> listarCitasPorPaciente(int id_paciente) {

        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT * FROM Citas WHERE id_paciente = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_paciente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cita c = new Cita();
                c.setId_cita(rs.getInt("id_cita"));
                c.setId_paciente(rs.getInt("id_paciente"));
                c.setId_medico(rs.getInt("id_medico"));
                c.setFecha(rs.getDate("fecha"));
                c.setHora(rs.getTime("hora"));
                c.setEstado(rs.getString("estado"));

                lista.add(c);
            }

        } catch (Exception e) {
        }

        return lista;
    }

    // 🔹 REGISTRAR NOTIFICACIÓN
    public void registrarNotificacion(int id_usuario, int id_cita, String accion) {

        String sql = "INSERT INTO Notificaciones (id_usuario, id_cita, accion, fecha_hora) VALUES (?, ?, ?, NOW())";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);
            ps.setInt(2, id_cita);
            ps.setString(3, accion);

            ps.executeUpdate();

        } catch (Exception e) {
        }
    }

    // 🔹 LISTAR NOTIFICACIONES
    public List<String> listarNotificaciones(int id_usuario) {

        List<String> lista = new ArrayList<>();

        String sql = "SELECT accion, fecha_hora FROM Notificaciones WHERE id_usuario = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String mensaje = rs.getString("accion") + " - " + rs.getTimestamp("fecha_hora");
                lista.add(mensaje);
            }

        } catch (Exception e) {
        }

        return lista;
    }
    
    public List<Cita> listarCitasPorMedico(int id_medico) {

    List<Cita> lista = new ArrayList<>();

    String sql = "SELECT * FROM Citas WHERE id_medico = ?";

    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_medico);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Cita c = new Cita();
            c.setId_cita(rs.getInt("id_cita"));
            c.setId_paciente(rs.getInt("id_paciente"));
            c.setFecha(rs.getDate("fecha"));
            c.setHora(rs.getTime("hora"));
            c.setEstado(rs.getString("estado"));

            lista.add(c);
        }

    } catch (Exception e) {
    }

    return lista;

}
}