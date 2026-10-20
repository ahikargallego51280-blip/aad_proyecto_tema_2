package org.example.menu;

import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.model.Juego;
import org.example.model.Partida;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuPartidas {

    private final Scanner sc = new Scanner(System.in);
    private final PartidaDAO partidaDAO = new PartidaDAO();
    private final JuegoDAO juegoDAO = new JuegoDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE PARTIDAS");
            System.out.println("1. Crear partida");
            System.out.println("2. Listar partidas");
            System.out.println("3. Modificar partida");
            System.out.println("4. Cancelar partida");
            System.out.println("5. Buscar partida");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                if (opcion == 1) {
                    crear();
                } else if (opcion == 2) {
                    listar();
                } else if (opcion == 3) {
                    modificar();
                } else if (opcion == 4) {
                    cancelar();
                } else if (opcion == 5) {
                    buscar();
                } else if (opcion == 0) {
                    System.out.println("Volviendo al menú principal...");
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    // Crear una partida
    private void crear() throws SQLException {
        System.out.print("ID del juego: ");
        int idJuego = leerEntero();

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
        int jugadores = leerEntero();

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
    private void listar() throws SQLException {
        List<Partida> partidas = partidaDAO.listarTodas();

        mostrarResultados(partidas);
    }

    // Buscar partidas por juego o por fecha
    private void buscar() throws SQLException {
        System.out.println("\nBUSCAR PARTIDA");
        System.out.println("1. Buscar por ID del juego");
        System.out.println("2. Buscar por fecha");
        System.out.print("Opción: ");

        int opcion = leerEntero();
        List<Partida> partidas;

        if (opcion == 1) {
            System.out.print("ID del juego: ");
            int idJuego = leerEntero();

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

    // Mostrar los resultados de una búsqueda o listado
    private void mostrarResultados(List<Partida> partidas) {
        if (partidas.isEmpty()) {
            System.out.println("No se han encontrado partidas.");
            return;
        }

        System.out.println("\nRESULTADOS");

        for (Partida partida : partidas) {
            System.out.println(partida);
        }

        System.out.println("Total de partidas: " + partidas.size());
    }

    // Modificar una partida
    private void modificar() throws SQLException {
        System.out.print("ID de la partida: ");
        int id = leerEntero();

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
        int idJuego = leerEntero();

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
        int jugadores = leerEntero();

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
    private void cancelar() throws SQLException {
        System.out.print("ID de la partida: ");
        int id = leerEntero();

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

    private LocalDate leerFecha() {
        try {
            return LocalDate.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha no válida. Usa el formato AAAA-MM-DD.");
            return null;
        }
    }

    private LocalTime leerHora() {
        try {
            return LocalTime.parse(sc.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("Hora no válida. Usa el formato HH:MM.");
            return null;
        }
    }
}