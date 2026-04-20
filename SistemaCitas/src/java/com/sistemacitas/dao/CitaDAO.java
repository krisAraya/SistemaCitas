package com.sistemacitas.dao;

import com.sistemacitas.conexion.Conexion;
import com.sistemacitas.modelo.Cita;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitaDAO {

    public boolean horaOcupada(int idMedico, Date fecha, Time hora) {
        String sql = "SELECT COUNT(*) FROM Citas "
                   + "WHERE id_medico = ? AND fecha = ? AND hora = ? "
                   + "AND estado IN ('pendiente', 'confirmada')";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean horaOcupadaExceptoCita(int idMedico, Date fecha, Time hora, int idCita) {
        String sql = "SELECT COUNT(*) FROM Citas "
                   + "WHERE id_medico = ? AND fecha = ? AND hora = ? AND id_cita <> ? "
                   + "AND estado IN ('pendiente', 'confirmada')";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);
            ps.setDate(2, fecha);
            ps.setTime(3, hora);
            ps.setInt(4, idCita);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Map<String, List<String>> listarHorasOcupadasPorMedico(int idMedico) {
        Map<String, List<String>> ocupadas = new HashMap<>();

        String sql = "SELECT fecha, hora FROM Citas "
                   + "WHERE id_medico = ? AND estado IN ('pendiente', 'confirmada')";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fecha = rs.getDate("fecha").toString();
                    String hora = rs.getTime("hora").toLocalTime().toString();
                    if (hora.length() >= 5) {
                        hora = hora.substring(0, 5);
                    }

                    if (!ocupadas.containsKey(fecha)) {
                        ocupadas.put(fecha, new ArrayList<String>());
                    }

                    ocupadas.get(fecha).add(hora);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ocupadas;
    }

    public boolean registrarCita(Cita cita) {

        String sqlCita = "INSERT INTO Citas (id_paciente, id_medico, fecha, hora, estado) VALUES (?, ?, ?, ?, ?)";
        String sqlNotificacion = "INSERT INTO Notificaciones (id_usuario, id_cita, accion, fecha_hora) VALUES (?, ?, ?, NOW())";

        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            String sqlValidar = "SELECT COUNT(*) FROM Citas "
                              + "WHERE id_medico = ? AND fecha = ? AND hora = ? "
                              + "AND estado IN ('pendiente', 'confirmada')";

            try (PreparedStatement psValidar = con.prepareStatement(sqlValidar)) {
                psValidar.setInt(1, cita.getId_medico());
                psValidar.setDate(2, cita.getFecha());
                psValidar.setTime(3, cita.getHora());

                try (ResultSet rs = psValidar.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        con.rollback();
                        return false;
                    }
                }
            }

            int idCitaGenerada;

            try (PreparedStatement ps = con.prepareStatement(sqlCita, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cita.getId_paciente());
                ps.setInt(2, cita.getId_medico());
                ps.setDate(3, cita.getFecha());
                ps.setTime(4, cita.getHora());
                ps.setString(5, cita.getEstado());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    idCitaGenerada = rs.getInt(1);
                }
            }

            try (PreparedStatement psNoti = con.prepareStatement(sqlNotificacion)) {

                String mensajePaciente = "Tu cita fue registrada para el "
                        + cita.getFecha() + " a las " + cita.getHora()
                        + ". Estado: " + cita.getEstado();

                psNoti.setInt(1, cita.getId_paciente());
                psNoti.setInt(2, idCitaGenerada);
                psNoti.setString(3, mensajePaciente);
                psNoti.executeUpdate();

                String mensajeMedico = "Nueva cita registrada para el "
                        + cita.getFecha() + " a las " + cita.getHora()
                        + ". Estado: " + cita.getEstado();

                psNoti.setInt(1, cita.getId_medico());
                psNoti.setInt(2, idCitaGenerada);
                psNoti.setString(3, mensajeMedico);
                psNoti.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Cita> listarCitasPorPaciente(int id_paciente) {

        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT c.*, u.nombre AS nombre_medico, u.especialidad AS especialidad_medico "
                   + "FROM Citas c "
                   + "INNER JOIN Usuarios u ON c.id_medico = u.id_usuario "
                   + "WHERE c.id_paciente = ? "
                   + "ORDER BY c.fecha DESC, c.hora DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_paciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId_cita(rs.getInt("id_cita"));
                    c.setId_paciente(rs.getInt("id_paciente"));
                    c.setId_medico(rs.getInt("id_medico"));
                    c.setNombreMedico(rs.getString("nombre_medico"));
                    c.setEspecialidadMedico(rs.getString("especialidad_medico"));
                    c.setFecha(rs.getDate("fecha"));
                    c.setHora(rs.getTime("hora"));
                    c.setEstado(rs.getString("estado"));
                    c.setNotas(rs.getString("notas"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Cita> listarCitasPorMedico(int id_medico) {

        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT c.*, u.nombre AS nombre_paciente "
                   + "FROM Citas c "
                   + "INNER JOIN Usuarios u ON c.id_paciente = u.id_usuario "
                   + "WHERE c.id_medico = ? "
                   + "ORDER BY c.fecha DESC, c.hora DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId_cita(rs.getInt("id_cita"));
                    c.setId_paciente(rs.getInt("id_paciente"));
                    c.setId_medico(rs.getInt("id_medico"));
                    c.setNombrePaciente(rs.getString("nombre_paciente"));
                    c.setFecha(rs.getDate("fecha"));
                    c.setHora(rs.getTime("hora"));
                    c.setEstado(rs.getString("estado"));
                    c.setNotas(rs.getString("notas"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Cita> listarHistorialPacientesAtendidos(int id_medico) {

        List<Cita> lista = new ArrayList<>();

        String sql = "SELECT c.*, u.nombre AS nombre_paciente "
                   + "FROM Citas c "
                   + "INNER JOIN Usuarios u ON c.id_paciente = u.id_usuario "
                   + "WHERE c.id_medico = ? AND c.estado = 'confirmada' "
                   + "ORDER BY u.nombre, c.fecha DESC, c.hora DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_medico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId_cita(rs.getInt("id_cita"));
                    c.setId_paciente(rs.getInt("id_paciente"));
                    c.setId_medico(rs.getInt("id_medico"));
                    c.setNombrePaciente(rs.getString("nombre_paciente"));
                    c.setFecha(rs.getDate("fecha"));
                    c.setHora(rs.getTime("hora"));
                    c.setEstado(rs.getString("estado"));
                    c.setNotas(rs.getString("notas"));
                    lista.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<String> listarNotificaciones(int id_usuario) {

        List<String> lista = new ArrayList<>();

        String sql = "SELECT accion, fecha_hora FROM Notificaciones WHERE id_usuario = ? ORDER BY fecha_hora DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String mensaje = rs.getString("accion") + " - " + rs.getTimestamp("fecha_hora");
                    lista.add(mensaje);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void actualizarEstado(int id_cita, String estado) {

        String sqlUpdate = "UPDATE Citas SET estado = ? WHERE id_cita = ?";
        String sqlBuscarPaciente = "SELECT id_paciente FROM Citas WHERE id_cita = ?";
        String sqlNotificacion = "INSERT INTO Notificaciones (id_usuario, id_cita, accion, fecha_hora) VALUES (?, ?, ?, NOW())";

        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setString(1, estado);
                ps.setInt(2, id_cita);
                ps.executeUpdate();
            }

            int idPaciente = 0;

            try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscarPaciente)) {
                psBuscar.setInt(1, id_cita);

                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        idPaciente = rs.getInt("id_paciente");
                    }
                }
            }

            if (idPaciente > 0) {
                try (PreparedStatement psNoti = con.prepareStatement(sqlNotificacion)) {
                    String mensaje = "Tu cita #" + id_cita + " fue actualizada a estado: " + estado;
                    psNoti.setInt(1, idPaciente);
                    psNoti.setInt(2, id_cita);
                    psNoti.setString(3, mensaje);
                    psNoti.executeUpdate();
                }
            }

            con.commit();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean reprogramarCita(int id_cita, int id_medico, Date fechaNueva, Time horaNueva) {

        String sqlUpdate = "UPDATE Citas SET fecha = ?, hora = ?, estado = 'pendiente' WHERE id_cita = ? AND id_medico = ?";
        String sqlPaciente = "SELECT id_paciente FROM Citas WHERE id_cita = ? AND id_medico = ?";
        String sqlNotificacion = "INSERT INTO Notificaciones (id_usuario, id_cita, accion, fecha_hora) VALUES (?, ?, ?, NOW())";

        Connection con = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            if (horaOcupadaExceptoCita(id_medico, fechaNueva, horaNueva, id_cita)) {
                con.rollback();
                return false;
            }

            int filas;

            try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setDate(1, fechaNueva);
                ps.setTime(2, horaNueva);
                ps.setInt(3, id_cita);
                ps.setInt(4, id_medico);
                filas = ps.executeUpdate();
            }

            if (filas == 0) {
                con.rollback();
                return false;
            }

            int idPaciente = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlPaciente)) {
                ps.setInt(1, id_cita);
                ps.setInt(2, id_medico);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idPaciente = rs.getInt("id_paciente");
                    }
                }
            }

            if (idPaciente > 0) {
                try (PreparedStatement ps = con.prepareStatement(sqlNotificacion)) {
                    String mensaje = "Tu cita #" + id_cita + " fue reprogramada para " + fechaNueva + " a las " + horaNueva;
                    ps.setInt(1, idPaciente);
                    ps.setInt(2, id_cita);
                    ps.setString(3, mensaje);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean guardarNotaMedica(int id_cita, int id_medico, String notas) {

        String sql = "UPDATE Citas SET notas = ? WHERE id_cita = ? AND id_medico = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, notas);
            ps.setInt(2, id_cita);
            ps.setInt(3, id_medico);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
