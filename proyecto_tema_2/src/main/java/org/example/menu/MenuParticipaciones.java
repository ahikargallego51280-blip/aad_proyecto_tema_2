
package org.example.menu;

import org.example.dao.JuegoDAO;
import org.example.dao.ParticipacionDAO;
import org.example.dao.PartidaDAO;
import org.example.dao.SocioDAO;
import org.example.model.Juego;
import org.example.model.Participacion;
import org.example.model.Partida;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuParticipaciones {

    private final Scanner sc = new Scanner(System.in);
    private final ParticipacionDAO participacionDAO = new ParticipacionDAO();
    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final JuegoDAO juegoDAO = new JuegoDAO();
    private final SocioDAO socioDAO = new SocioDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE PARTICIPANTES");
            System.out.println("1. Inscribir socio");
            System.out.println("2. Inscribir invitado");
            System.out.println("3. Listar participantes");
            System.out.println("4. Registrar ganador");
            System.out.println("5. Retirar participante");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> inscribirSocio();
                    case 2 -> inscribirInvitado();
                    case 3 -> listarParticipantes();
                    case 4 -> registrarGanador();
                    case 5 -> retirarParticipante();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    // Inscribir un socio
    private void inscribirSocio() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = leerEntero();

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
            System.out.println("No existe ningún socio con ese documento.");
            return;
        }

        if (participacionDAO.inscribirSocio(idPartida, documento)) {
            System.out.println("Socio inscrito correctamente.");
        } else {
            System.out.println("No se ha podido realizar la inscripción.");
        }
    }

    // Inscribir un invitado
    private void inscribirInvitado() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = leerEntero();

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
            System.out.println("No se ha podido realizar la inscripción.");
        }
    }

    // Comprobar la partida y las plazas disponibles
    private boolean comprobarPlaza(int idPartida) throws SQLException {
        Partida partida = partidaDAO.buscarPorId(idPartida);

        if (partida == null || !partida.isActiva()) {
            System.out.println("La partida no existe o está cancelada.");
            return false;
        }

        Juego juego = juegoDAO.buscarPorId(partida.getIdJuego());

        if (juego == null || !juego.isActivo()) {
            System.out.println("El juego no está disponible.");
            return false;
        }

        int inscritos = participacionDAO.contarParticipantes(idPartida);
        int plazas = partida.getJugadoresDeseados() - inscritos;

        if (plazas <= 0) {
            System.out.println("La partida ya ha alcanzado el número de participantes deseado.");
            return false;
        }

        System.out.println("Plazas disponibles: " + plazas);
        return true;
    }

    // Listar los participantes de una partida
    private void listarParticipantes() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = leerEntero();

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
            System.out.println("ID de participación: " + p.getIdParticipacion());
            System.out.println("Persona: " + p.getNombrePersona());
            System.out.println("Tipo: " + (p.esSocio() ? "Socio" : "Invitado"));
            System.out.println("Ganador: " + (p.isEsGanadora() ? "Sí" : "No"));
        }

        System.out.println("\nTotal de participantes: " + participantes.size());

        int plazasRestantes = Math.max(
                0,
                partida.getJugadoresDeseados() - participantes.size()
        );

        System.out.println("Plazas restantes: " + plazasRestantes);
    }

    // Registrar un ganador
    private void registrarGanador() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = leerEntero();

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
        int idParticipacion = leerEntero();

        Participacion seleccionada =
                buscarParticipacion(participantes, idParticipacion);

        if (seleccionada == null) {
            System.out.println("Esa participación no pertenece a esta partida.");
            return;
        }

        if (participacionDAO.registrarGanadora(idParticipacion, true)) {
            System.out.println("Ganador registrado correctamente.");
        } else {
            System.out.println("No se ha podido registrar el ganador.");
        }
    }

    // Retirar un participante
    private void retirarParticipante() throws SQLException {
        System.out.print("ID de la partida: ");
        int idPartida = leerEntero();

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

        System.out.print("ID de participación que quieres retirar: ");
        int idParticipacion = leerEntero();

        Participacion seleccionada =
                buscarParticipacion(participantes, idParticipacion);

        if (seleccionada == null) {
            System.out.println("Esa participación no pertenece a esta partida.");
            return;
        }

        System.out.print("¿Confirmas que quieres retirar a esta persona? (s/n): ");
        String respuesta = sc.nextLine().trim();

        if (!respuesta.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (participacionDAO.retirarParticipante(idParticipacion)) {
            System.out.println("Participante retirado correctamente.");
        } else {
            System.out.println("No se ha podido retirar al participante.");
        }
    }

    // Buscar una participación por su ID
    private Participacion buscarParticipacion(
            List<Participacion> participantes, int id) {

        for (Participacion p : participantes) {
            if (p.getIdParticipacion() == id) {
                return p;
            }
        }

        return null;
    }

    // Leer un número entero
    private int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }
}
