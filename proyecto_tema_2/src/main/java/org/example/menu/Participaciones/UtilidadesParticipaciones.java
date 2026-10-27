
package org.example.menu.Participaciones;

import org.example.dao.JuegoDAO;
import org.example.dao.ParticipacionDAO;
import org.example.dao.PartidaDAO;
import org.example.dao.SocioDAO;
import org.example.menu.Utilidades.UtilidadesMenu;
import org.example.model.Juego;
import org.example.model.Participacion;
import org.example.model.Partida;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UtilidadesParticipaciones {

    private final Scanner sc = new Scanner(System.in);

    private final ParticipacionDAO participacionDAO =
            new ParticipacionDAO();

    private final PartidaDAO partidaDAO = new PartidaDAO();

    private final JuegoDAO juegoDAO = new JuegoDAO();

    private final SocioDAO socioDAO = new SocioDAO();


    // Comprobar que hay plazas y que el socio exista, y lo inscribe
    public void inscribirSocio() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = UtilidadesMenu.leerEntero();

        if (!comprobarPlaza(idPartida)) {
            return;
        }

        System.out.print("Documento del socio: ");
        String documento = sc.nextLine().trim();

        if (documento.isEmpty()) {
            System.out.println("El documento no puede estar vacío.");
            return;
        }

        if (!socioDAO.existe(documento)) {
            System.out.println(
                    "No existe ningún socio con ese documento."
            );
            return;
        }

        if (participacionDAO.inscribirSocio(idPartida, documento)) {
            System.out.println("Socio inscrito correctamente.");
        } else {
            System.out.println(
                    "No se ha podido realizar la inscripción."
            );
        }
    }


    // Comprobar si hay plazas y registra al invitado
    public void inscribirInvitado() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = UtilidadesMenu.leerEntero();

        if (!comprobarPlaza(idPartida)) {
            return;
        }

        System.out.print("Nombre y apellidos del invitado: ");
        String nombre = sc.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        if (participacionDAO.inscribirInvitado(idPartida, nombre)) {
            System.out.println("Invitado inscrito correctamente.");
        } else {
            System.out.println(
                    "No se ha podido realizar la inscripción."
            );
        }
    }

    // Comprueba que la partida este activa, que el juego este disponible y que queden plazas
    private boolean comprobarPlaza(int idPartida)
            throws SQLException {

        Partida partida = partidaDAO.buscarPorId(idPartida);

        if (partida == null || !partida.isActiva()) {
            System.out.println(
                    "La partida no existe o está cancelada."
            );
            return false;
        }

        Juego juego = juegoDAO.buscarPorId(partida.getIdJuego());

        if (juego == null || !juego.isActivo()) {
            System.out.println("El juego no está disponible.");
            return false;
        }

        int inscritos =
                participacionDAO.contarParticipantes(idPartida);

        int plazas = partida.getJugadoresDeseados() - inscritos;

        if (plazas <= 0) {
            System.out.println(
                    "La partida ya ha alcanzado el número de participantes deseado."
            );
            return false;
        }

        System.out.println("Plazas disponibles: " + plazas);
        return true;
    }


    // Muestra los participantes de una partida y las plazas restantes
    public void listarParticipantes() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = UtilidadesMenu.leerEntero();

        Partida partida = partidaDAO.buscarPorId(idPartida);

        if (partida == null) {
            System.out.println("No existe esa partida.");
            return;
        }

        List<Participacion> participantes =
                participacionDAO.listarPorPartida(idPartida);

        if (participantes.isEmpty()) {
            System.out.println("No hay participantes inscritos.");
            return;
        }

        for (Participacion p : participantes) {
            System.out.println();
            System.out.println(
                    "ID de participación: " + p.getIdParticipacion()
            );
            System.out.println(
                    "Persona: " + p.getNombrePersona()
            );
            System.out.println(
                    "Tipo: " + (p.esSocio() ? "Socio" : "Invitado")
            );
            System.out.println(
                    "Ganador: " + (p.isEsGanadora() ? "Sí" : "No")
            );
        }

        System.out.println(
                "\nTotal de participantes: " + participantes.size()
        );

        int plazasRestantes = Math.max(
                0,
                partida.getJugadoresDeseados() - participantes.size()
        );

        System.out.println("Plazas restantes: " + plazasRestantes);
    }
    // Muestra los participantes, comprueba que la particiàcion elegida pertenezca a esa partida y registra al ganador
    public void registrarGanador() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = UtilidadesMenu.leerEntero();

        Partida partida = partidaDAO.buscarPorId(idPartida);

        if (partida == null) {
            System.out.println("No existe esa partida.");
            return;
        }

        List<Participacion> participantes =
                participacionDAO.listarPorPartida(idPartida);

        if (participantes.isEmpty()) {
            System.out.println("La partida no tiene participantes.");
            return;
        }

        System.out.println("\nParticipantes:");

        for (Participacion p : participantes) {
            System.out.println(
                    "ID " + p.getIdParticipacion()
                            + " - " + p.getNombrePersona()
                            + (p.isEsGanadora() ? " (Ganador)" : "")
            );
        }

        System.out.print("ID de participación del ganador: ");
        int idParticipacion = UtilidadesMenu.leerEntero();

        Participacion seleccionada =
                buscarParticipacion(participantes, idParticipacion);

        if (seleccionada == null) {
            System.out.println(
                    "Esa participación no pertenece a esta partida."
            );
            return;
        }

        if (participacionDAO.registrarGanadora(idParticipacion, true)) {
            System.out.println("Ganador registrado correctamente.");
        } else {
            System.out.println("No se ha podido registrar el ganador.");
        }
    }

    // Seleccione a un participante, confirma su retirada y retirarlo
    public void retirarParticipante() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = UtilidadesMenu.leerEntero();

        Partida partida = partidaDAO.buscarPorId(idPartida);

        if (partida == null) {
            System.out.println("No existe esa partida.");
            return;
        }

        List<Participacion> participantes =
                participacionDAO.listarPorPartida(idPartida);

        if (participantes.isEmpty()) {
            System.out.println("No hay participantes inscritos.");
            return;
        }

        for (Participacion p : participantes) {
            System.out.println(
                    "ID " + p.getIdParticipacion()
                            + " - " + p.getNombrePersona()
            );
        }

        System.out.print(
                "ID de participación que quieres retirar: "
        );
        int idParticipacion = UtilidadesMenu.leerEntero();

        Participacion seleccionada =
                buscarParticipacion(participantes, idParticipacion);

        if (seleccionada == null) {
            System.out.println(
                    "Esa participación no pertenece a esta partida."
            );
            return;
        }

        System.out.print(
                "¿Confirmas que quieres retirar a esta persona? (s/n): "
        );
        String respuesta = sc.nextLine().trim();

        if (!respuesta.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (participacionDAO.retirarParticipante(idParticipacion)) {
            System.out.println("Participante retirado correctamente.");
        } else {
            System.out.println(
                    "No se ha podido retirar al participante."
            );
        }
    }


    // Busca una participacion dentro de una lista pod su id
    private Participacion buscarParticipacion(
            List<Participacion> participantes, int id) {

        for (Participacion p : participantes) {
            if (p.getIdParticipacion() == id) {
                return p;
            }
        }

        return null;
    }
}
