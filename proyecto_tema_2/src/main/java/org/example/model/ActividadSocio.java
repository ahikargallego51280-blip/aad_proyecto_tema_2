
package org.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ActividadSocio {

    private final int idPartida;
    private final LocalDate fecha;
    private final LocalTime hora;
    private final String nombreJuego;
    private final String tipoJuego;
    private final int duracionMinutos;
    private final boolean ganadora;
    private final boolean resultadoRegistrado;

    public ActividadSocio(
            int idPartida,
            LocalDate fecha,
            LocalTime hora,
            String nombreJuego,
            String tipoJuego,
            int duracionMinutos,
            boolean ganadora,
            boolean resultadoRegistrado) {

        this.idPartida = idPartida;
        this.fecha = fecha;
        this.hora = hora;
        this.nombreJuego = nombreJuego;
        this.tipoJuego = tipoJuego;
        this.duracionMinutos = duracionMinutos;
        this.ganadora = ganadora;
        this.resultadoRegistrado = resultadoRegistrado;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public String getTipoJuego() {
        return tipoJuego;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public boolean isGanadora() {
        return ganadora;
    }

    public boolean isResultadoRegistrado() {
        return resultadoRegistrado;
    }
}
