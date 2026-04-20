package com.sistemacitas.dao;

import com.sistemacitas.conexion.Conexion;
import com.sistemacitas.modelo.Horario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {

    public boolean guardarHorario(Horario h) {

        String sql = "INSERT INTO Horarios (id_medico, fecha, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, h.getId_medico());
            ps.setDate(2, h.getFecha());
            ps.setTime(3, h.getHora_inicio());
            ps.setTime(4, h.getHora_fin());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean horarioValido(int id_medico, Date fecha, Time hora) {
        String sql = "SELECT * FROM Horarios WHERE id_medico = ? AND fecha = ? AND ? BETWEEN hora_inicio AND hora_fin";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medico);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Horario> listarHorariosPorMedico(int id_medico) {

        List<Horario> lista = new ArrayList<>();

        String sql = "SELECT * FROM Horarios WHERE id_medico = ? ORDER BY fecha, hora_inicio";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Horario h = new Horario();
                    h.setId_horario(rs.getInt("id_horario"));
                    h.setId_medico(rs.getInt("id_medico"));
                    h.setFecha(rs.getDate("fecha"));
                    h.setHora_inicio(rs.getTime("hora_inicio"));
                    h.setHora_fin(rs.getTime("hora_fin"));
                    lista.add(h);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Horario> listarHorariosDisponibles(int id_medico) {

        List<Horario> lista = new ArrayList<>();

        String sql = "SELECT * FROM Horarios WHERE id_medico = ? ORDER BY fecha, hora_inicio";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Horario h = new Horario();
                    h.setId_horario(rs.getInt("id_horario"));
                    h.setId_medico(rs.getInt("id_medico"));
                    h.setFecha(rs.getDate("fecha"));
                    h.setHora_inicio(rs.getTime("hora_inicio"));
                    h.setHora_fin(rs.getTime("hora_fin"));
                    lista.add(h);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
