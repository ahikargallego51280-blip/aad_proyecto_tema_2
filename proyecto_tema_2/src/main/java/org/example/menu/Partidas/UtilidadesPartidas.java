
package org.example.menu.Partidas;

import org.example.dao.JuegoDAO;
import org.example.dao.ParticipacionDAO;
import org.example.dao.PartidaDAO;
import org.example.menu.Utilidades.UtilidadesMenu;
import org.example.model.Juego;
import org.example.model.Partida;
import org.example.model.Participacion;
import org.example.dao.SocioDAO;
import org.example.model.Socio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UtilidadesPartidas {

    private final Scanner sc = new Scanner(System.in);
    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final JuegoDAO juegoDAO = new JuegoDAO();
    private final ParticipacionDAO participacionDAO = new ParticipacionDAO();
    private final SocioDAO socioDAO = new SocioDAO();

    // Crear una partida
    public void crear() throws SQLException {
        System.out.print("ID del juego: ");
        int idJuego = UtilidadesMenu.leerEntero();

        Juego juego = juegoDAO.buscarPorId(idJuego);

        if (juego == null || !juego.isActivo()) {
            System.out.println("El juego no existe o está dado de baja.");
            return;
        }

        if (!"DISPONIBLE".equalsIgnoreCase(juego.getEstado())) {
            System.out.println("El juego no está disponible.");
            return;
        }

        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = leerFecha();

        if (fecha == null) {
            return;
        }

        System.out.print("Hora (HH:MM): ");
        LocalTime hora = leerHora();

        if (hora == null) {
            return;
        }

        System.out.print("Número de jugadores deseados: ");
        int jugadores = UtilidadesMenu.leerEntero();

        if (jugadores < juego.getJugadoresMin()
                || jugadores > juego.getJugadoresMax()) {
            System.out.println("El número de jugadores debe estar entre "
                    + juego.getJugadoresMin() + " y "
                    + juego.getJugadoresMax() + ".");
            return;
        }

        Partida partida = new Partida(
                0, idJuego, fecha, hora, jugadores, true
        );

        if (partidaDAO.insertar(partida)) {
            System.out.println("Partida creada correctamente.");
        } else {
            System.out.println("No se ha podido crear la partida.");
        }
    }

    // Listar todas las partidas
    public void listar() throws SQLException {
        List<Partida> partidas = partidaDAO.listarTodas();

        mostrarResultados(partidas);
    }

    // Buscar partidas por juego o por fecha
    public void buscar() throws SQLException {
        System.out.println("\nBUSCAR PARTIDA");
        System.out.println("1. Buscar por ID del juego");
        System.out.println("2. Buscar por fecha");
        System.out.print("Opción: ");

        int opcion = UtilidadesMenu.leerEntero();
        List<Partida> partidas;

        if (opcion == 1) {
            System.out.print("ID del juego: ");
            int idJuego = UtilidadesMenu.leerEntero();

            if (idJuego <= 0) {
                System.out.println("El ID debe ser mayor que cero.");
                return;
            }

            partidas = partidaDAO.buscarPorJuego(idJuego);

        } else if (opcion == 2) {
            System.out.print("Fecha (AAAA-MM-DD): ");
            LocalDate fecha = leerFecha();

            if (fecha == null) {
                return;
            }

            partidas = partidaDAO.buscarPorFecha(fecha);

        } else {
            System.out.println("Opción no válida.");
            return;
        }

        mostrarResultados(partidas);
    }

    // Mostrar los resultados de una búsqueda
    private void mostrarResultados(List<Partida> partidas)
            throws SQLException {

        if (partidas.isEmpty()) {
            System.out.println("No se han encontrado partidas.");
            return;
        }

        System.out.println("\nLISTADO DE PARTIDAS");

        for (Partida partida : partidas) {
            Juego juego = juegoDAO.buscarPorId(partida.getIdJuego());

            List<Participacion> participantes =
                    participacionDAO.listarPorPartida(
                            partida.getIdPartida()
                    );

            int inscritos = participantes.size();
            int plazasLibres = Math.max(
                    0, partida.getJugadoresDeseados() - inscritos
            );

            System.out.println("\n========================================");
            System.out.println("Partida: " + partida.getIdPartida());
            System.out.println("Juego: " +
                    (juego == null
                            ? "Juego no encontrado"
                            : juego.getNombre()));
            System.out.println("Fecha: " + partida.getFecha());
            System.out.println("Hora: " + partida.getHora());
            System.out.println("Jugadores deseados: "
                    + partida.getJugadoresDeseados());
            System.out.println("Participantes inscritos: " + inscritos);
            System.out.println("Plazas libres: " + plazasLibres);
            System.out.println("Estado: "
                    + (partida.isActiva() ? "Activa" : "Cancelada"));

            System.out.println("Participantes:");

            if (participantes.isEmpty()) {
                System.out.println("  No hay participantes inscritos.");
            } else {

                for (Participacion participante : participantes) {
                    if (participante.esSocio()) {
                        Socio socio = socioDAO.buscarPorDocumento(participante.getDocumentoSocio());

                        String nombreSocio = socio == null ? participante.getDocumentoSocio() : socio.getNombre() + " " + socio.getApellidos();

                        System.out.println("  Socio: " + nombreSocio);
                    } else {
                        System.out.println("  Invitado: "
                                + participante.getNombreInvitada());
                    }
                }

            }
        }

        System.out.println("\nTotal de partidas: " + partidas.size());
    }


    // Modificar una partida
    public void modificar() throws SQLException {
        System.out.print("ID de la partida: ");
        int id = UtilidadesMenu.leerEntero();

        Partida partida = partidaDAO.buscarPorId(id);

        if (partida == null) {
            System.out.println("No existe esa partida.");
            return;
        }

        if (!partida.isActiva()) {
            System.out.println("La partida está cancelada.");
            return;
        }

        System.out.print("Nuevo ID del juego: ");
        int idJuego = UtilidadesMenu.leerEntero();

        Juego juego = juegoDAO.buscarPorId(idJuego);

        if (juego == null || !juego.isActivo()
                || !"DISPONIBLE".equalsIgnoreCase(juego.getEstado())) {
            System.out.println("El juego no existe, no está activo "
                    + "o no está disponible.");
            return;
        }

        System.out.print("Nueva fecha (AAAA-MM-DD): ");
        LocalDate fecha = leerFecha();

        if (fecha == null) {
            return;
        }

        System.out.print("Nueva hora (HH:MM): ");
        LocalTime hora = leerHora();

        if (hora == null) {
            return;
        }

        System.out.print("Nuevo número de jugadores deseados: ");
        int jugadores = UtilidadesMenu.leerEntero();

        if (jugadores < juego.getJugadoresMin()
                || jugadores > juego.getJugadoresMax()) {
            System.out.println("El número de jugadores debe estar entre "
                    + juego.getJugadoresMin() + " y "
                    + juego.getJugadoresMax() + ".");
            return;
        }

        partida.setIdJuego(idJuego);
        partida.setFecha(fecha);
        partida.setHora(hora);
        partida.setJugadoresDeseados(jugadores);

        if (partidaDAO.actualizar(partida)) {
            System.out.println("Partida modificada correctamente.");
        } else {
            System.out.println("No se ha podido modificar la partida.");
        }
    }

    // Cancelar una partida
    public void cancelar() throws SQLException {
        System.out.print("ID de la partida: ");
        int id = UtilidadesMenu.leerEntero();

        Partida partida = partidaDAO.buscarPorId(id);

        if (partida == null) {
            System.out.println("No existe esa partida.");
            return;
        }

        if (!partida.isActiva()) {
            System.out.println("La partida ya está cancelada.");
            return;
        }

        System.out.print("¿Confirmas la cancelación? (s/n): ");
        String respuesta = sc.nextLine().trim();

        if (!respuesta.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (partidaDAO.cancelar(id)) {
            System.out.println("Partida cancelada correctamente.");
        } else {
            System.out.println("No se ha podido cancelar la partida.");
        }
    }

    // Leer una fecha
    private LocalDate leerFecha() {
        try {
            return LocalDate.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha no válida. Usa el formato AAAA-MM-DD.");
            return null;
        }
    }

    // Leer una hora
    private LocalTime leerHora() {
        try {
            return LocalTime.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Hora no válida. Usa el formato HH:MM.");
            return null;
        }
    }
}
