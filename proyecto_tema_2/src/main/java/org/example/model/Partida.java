package org.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Partida {

    private int idPartida;
    private int idJuego;
    private LocalDate fecha;
    private LocalTime hora;
    private int jugadoresDeseados;
    private boolean activa;

    public Partida(int idPartida, int idJuego, LocalDate fecha,
                   LocalTime hora, int jugadoresDeseados, boolean activa) {
        this.idPartida = idPartida;
        this.idJuego = idJuego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.activa = activa;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getJugadoresDeseados() {
        return jugadoresDeseados;
    }

    public void setJugadoresDeseados(int jugadoresDeseados) {
        this.jugadoresDeseados = jugadoresDeseados;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "idPartida=" + idPartida +
                ", idJuego=" + idJuego +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", jugadoresDeseados=" + jugadoresDeseados +
                ", activa=" + activa +
                '}';
    }
}