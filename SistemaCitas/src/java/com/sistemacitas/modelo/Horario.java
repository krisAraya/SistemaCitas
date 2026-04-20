package com.sistemacitas.modelo;

import java.sql.Date;
import java.sql.Time;

public class Horario {

    private int id_horario;
    private int id_medico;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_fin;

    public int getId_horario() { return id_horario; }
    public void setId_horario(int id_horario) { this.id_horario = id_horario; }

    public int getId_medico() { return id_medico; }
    public void setId_medico(int id_medico) { this.id_medico = id_medico; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHora_inicio() { return hora_inicio; }
    public void setHora_inicio(Time hora_inicio) { this.hora_inicio = hora_inicio; }

    public Time getHora_fin() { return hora_fin; }
    public void setHora_fin(Time hora_fin) { this.hora_fin = hora_fin; }
}
