
package org.example.model;

public class Juego {

    private int idJuego;
    private String nombre;
    private String autores;
    private String descripcion;
    private int jugadoresMin;
    private int jugadoresMax;
    private int duracionMin;
    private String tematica;
    private String tipo;
    private String ubicacion;
    private String estado;
    private String propietarioDocumento;
    private String fechaAlta;
    private boolean activo;

    public Juego(int idJuego, String nombre, String autores,
                 String descripcion, int jugadoresMin, int jugadoresMax,
                 int duracionMin, String tematica, String tipo,
                 String ubicacion, String estado,
                 String propietarioDocumento, String fechaAlta,
                 boolean activo) {
        this.idJuego = idJuego;
        this.nombre = nombre;
        this.autores = autores;
        this.descripcion = descripcion;
        this.jugadoresMin = jugadoresMin;
        this.jugadoresMax = jugadoresMax;
        this.duracionMin = duracionMin;
        this.tematica = tematica;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.propietarioDocumento = propietarioDocumento;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAutores() {
        return autores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getJugadoresMin() {
        return jugadoresMin;
    }

    public int getJugadoresMax() {
        return jugadoresMax;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public String getTematica() {
        return tematica;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public String getPropietarioDocumento() {
        return propietarioDocumento;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setJugadoresMin(int jugadoresMin) {
        this.jugadoresMin = jugadoresMin;
    }

    public void setJugadoresMax(int jugadoresMax) {
        this.jugadoresMax = jugadoresMax;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setPropietarioDocumento(String propietarioDocumento) {
        this.propietarioDocumento = propietarioDocumento;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "ID: " + idJuego + "\n" +
                "Nombre: " + nombre + "\n" +
                "Autoría: " + autores + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Número de jugadores: " + jugadoresMin + " - " + jugadoresMax + "\n" +
                "Duración: " + duracionMin + " minutos\n" +
                "Temática: " + tematica + "\n" +
                "Tipo: " + tipo + "\n" +
                "Ubicación: " + ubicacion + "\n" +
                "Estado: " + estado + "\n" +
                "Propietario: " +
                (propietarioDocumento == null
                        ? "Asociación"
                        : propietarioDocumento) + "\n" +
                "Fecha de alta: " + fechaAlta + "\n" +
                "Activo: " + (activo ? "Sí" : "No") + "\n" +
                "----------------------------------------";
    }

}
