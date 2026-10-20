package org.example.model;

public class Participacion {

    private int idParticipacion;
    private int idPartida;
    private String documentoSocio;
    private String nombreInvitada;
    private boolean esGanadora;

    public Participacion(int idParticipacion, int idPartida,
                         String documentoSocio, String nombreInvitada,
                         boolean esGanadora) {
        this.idParticipacion = idParticipacion;
        this.idPartida = idPartida;
        this.documentoSocio = documentoSocio;
        this.nombreInvitada = nombreInvitada;
        this.esGanadora = esGanadora;
    }

    public int getIdParticipacion() {
        return idParticipacion;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public String getDocumentoSocio() {
        return documentoSocio;
    }

    public void setDocumentoSocio(String documentoSocio) {
        this.documentoSocio = documentoSocio;
    }

    public String getNombreInvitada() {
        return nombreInvitada;
    }

    public void setNombreInvitada(String nombreInvitada) {
        this.nombreInvitada = nombreInvitada;
    }

    public boolean isEsGanadora() {
        return esGanadora;
    }

    public void setEsGanadora(boolean esGanadora) {
        this.esGanadora = esGanadora;
    }

    public boolean esSocio() {
        return documentoSocio != null && !documentoSocio.isBlank();
    }

    public String getNombrePersona() {
        if (esSocio()) {
            return documentoSocio;
        }

        return nombreInvitada;
    }

    @Override
    public String toString() {
        return "Participacion{" +
                "idParticipacion=" + idParticipacion +
                ", idPartida=" + idPartida +
                ", persona='" + getNombrePersona() + '\'' +
                ", esGanadora=" + esGanadora +
                '}';
    }
}