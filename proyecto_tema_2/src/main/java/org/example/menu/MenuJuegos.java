
package org.example.menu;

import org.example.dao.JuegoDAO;
import org.example.dao.SocioDAO;
import org.example.model.Juego;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuJuegos {

    private final Scanner sc = new Scanner(System.in);
    private final JuegoDAO dao = new JuegoDAO();
    private final SocioDAO socioDAO = new SocioDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE JUEGOS");
            System.out.println("1. Registrar juego");
            System.out.println("2. Listar juegos");
            System.out.println("3. Buscar juego");
            System.out.println("4. Modificar juego");
            System.out.println("5. Dar de baja juego");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> modificar();
                    case 5 -> darDeBaja();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    // Registrar un juego
    private void registrar() throws SQLException {
        System.out.println("\nREGISTRAR JUEGO");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Autores: ");
        String autores = sc.nextLine().trim();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine().trim();

        System.out.print("Número mínimo de jugadores: ");
        int jugadoresMin = leerEntero();

        System.out.print("Número máximo de jugadores: ");
        int jugadoresMax = leerEntero();

        System.out.print("Duración en minutos: ");
        int duracion = leerEntero();

        System.out.print("Temática: ");
        String tematica = sc.nextLine().trim();

        System.out.print("Tipo (COMPETITIVO, COOPERATIVO o AMBOS): ");
        String tipo = sc.nextLine().trim().toUpperCase();

        System.out.print("Ubicación: ");
        String ubicacion = sc.nextLine().trim();

        System.out.print("Estado (DISPONIBLE, PRESTADO o REPARACION): ");
        String estado = sc.nextLine().trim().toUpperCase();

        System.out.println("\nPropietario del juego");
        System.out.println("1. Asociación");
        System.out.println("2. Socio");
        System.out.print("Opción: ");
        int opcionPropietario = leerEntero();

        String propietario = null;

        if (opcionPropietario == 2) {
            System.out.print("Documento del socio propietario: ");
            propietario = sc.nextLine().trim();

            if (propietario.isEmpty() || !socioDAO.existe(propietario)) {
                System.out.println("No existe ningún socio con ese documento.");
                return;
            }
        } else if (opcionPropietario != 1) {
            System.out.println("Opción de propietario no válida.");
            return;
        }

        if (nombre.isEmpty() || jugadoresMin < 1
                || jugadoresMax < jugadoresMin || duracion <= 0) {
            System.out.println("Revisa el nombre y los valores numéricos.");
            return;
        }

        if (!tipo.equals("COMPETITIVO")
                && !tipo.equals("COOPERATIVO")
                && !tipo.equals("AMBOS")) {
            System.out.println("Tipo de juego no válido.");
            return;
        }

        if (!estado.equals("DISPONIBLE")
                && !estado.equals("PRESTADO")
                && !estado.equals("REPARACION")) {
            System.out.println("Estado no válido.");
            return;
        }

        Juego juego = new Juego(
                0,
                nombre,
                autores.isEmpty() ? null : autores,
                descripcion.isEmpty() ? null : descripcion,
                jugadoresMin,
                jugadoresMax,
                duracion,
                tematica.isEmpty() ? null : tematica,
                tipo,
                ubicacion.isEmpty() ? null : ubicacion,
                estado,
                propietario,
                LocalDate.now().toString(),
                true
        );

        if (dao.insertar(juego)) {
            System.out.println("Juego registrado correctamente.");
        } else {
            System.out.println("No se ha podido registrar el juego.");
        }
    }

    // Listar juegos
    private void listar() throws SQLException {
        System.out.println("\nLISTADO DE JUEGOS");

        List<Juego> juegos = dao.listarTodos();

        if (juegos.isEmpty()) {
            System.out.println("No hay juegos registrados.");
            return;
        }

        for (Juego juego : juegos) {
            System.out.println(juego);
        }

        System.out.println("Total de juegos: " + juegos.size());
    }

    // Buscar un juego por su ID
    private void buscar() throws SQLException {
        System.out.print("ID del juego: ");
        int id = leerEntero();

        Juego juego = dao.buscarPorId(id);

        if (juego == null) {
            System.out.println("No se ha encontrado ese juego.");
        } else {
            System.out.println(juego);
        }
    }

    // Modificar un juego
    private void modificar() throws SQLException {
        System.out.print("ID del juego: ");
        int id = leerEntero();

        Juego existente = dao.buscarPorId(id);

        if (existente == null) {
            System.out.println("No se ha encontrado ese juego.");
            return;
        }

        System.out.println("\nMODIFICAR JUEGO");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Autores: ");
        String autores = sc.nextLine().trim();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine().trim();

        System.out.print("Jugadores mínimos: ");
        int jugadoresMin = leerEntero();

        System.out.print("Jugadores máximos: ");
        int jugadoresMax = leerEntero();

        System.out.print("Duración en minutos: ");
        int duracion = leerEntero();

        System.out.print("Temática: ");
        String tematica = sc.nextLine().trim();

        System.out.print("Tipo (COMPETITIVO, COOPERATIVO, AMBOS): ");
        String tipo = sc.nextLine().trim().toUpperCase();

        System.out.print("Ubicación: ");
        String ubicacion = sc.nextLine().trim();

        System.out.print("Estado (DISPONIBLE, PRESTADO, REPARACION): ");
        String estado = sc.nextLine().trim().toUpperCase();

        System.out.println("\nPropietario del juego");
        System.out.println("1. Asociación");
        System.out.println("2. Socio");
        System.out.print("Opción: ");
        int opcionPropietario = leerEntero();

        String propietario = null;

        if (opcionPropietario == 2) {
            System.out.print("Documento del socio propietario: ");
            propietario = sc.nextLine().trim();

            if (propietario.isEmpty() || !socioDAO.existe(propietario)) {
                System.out.println("No existe ningún socio con ese documento.");
                return;
            }
        } else if (opcionPropietario != 1) {
            System.out.println("Opción de propietario no válida.");
            return;
        }

        System.out.print("Fecha de alta (AAAA-MM-DD): ");
        String fechaAlta = sc.nextLine().trim();

        System.out.print("¿Está activo? (s/n): ");
        boolean activo = sc.nextLine().trim().equalsIgnoreCase("s");

        if (nombre.isEmpty() || jugadoresMin < 1
                || jugadoresMax < jugadoresMin || duracion <= 0) {
            System.out.println("Revisa los valores introducidos.");
            return;
        }

        if (!tipo.equals("COMPETITIVO")
                && !tipo.equals("COOPERATIVO")
                && !tipo.equals("AMBOS")) {
            System.out.println("Tipo de juego no válido.");
            return;
        }

        if (!estado.equals("DISPONIBLE")
                && !estado.equals("PRESTADO")
                && !estado.equals("REPARACION")) {
            System.out.println("Estado no válido.");
            return;
        }

        try {
            LocalDate.parse(fechaAlta);
        } catch (Exception e) {
            System.out.println("La fecha debe tener el formato AAAA-MM-DD.");
            return;
        }

        Juego juego = new Juego(
                id,
                nombre,
                autores.isEmpty() ? null : autores,
                descripcion.isEmpty() ? null : descripcion,
                jugadoresMin,
                jugadoresMax,
                duracion,
                tematica.isEmpty() ? null : tematica,
                tipo,
                ubicacion.isEmpty() ? null : ubicacion,
                estado,
                propietario,
                fechaAlta,
                activo
        );

        if (dao.actualizar(juego)) {
            System.out.println("Juego modificado correctamente.");
        } else {
            System.out.println("No se ha podido modificar el juego.");
        }
    }

    // Dar de baja un juego
    private void darDeBaja() throws SQLException {
        System.out.print("ID del juego: ");
        int id = leerEntero();

        Juego juego = dao.buscarPorId(id);

        if (juego == null) {
            System.out.println("No se ha encontrado ese juego.");
            return;
        }

        System.out.print("¿Confirmas la baja? (s/n): ");
        String confirmacion = sc.nextLine().trim();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (dao.darDeBaja(id)) {
            System.out.println("Juego dado de baja.");
        } else {
            System.out.println("No se ha podido dar de baja el juego.");
        }
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
