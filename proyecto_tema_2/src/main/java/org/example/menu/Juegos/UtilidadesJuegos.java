
package org.example.menu.Juegos;

import org.example.dao.JuegoDAO;
import org.example.dao.SocioDAO;
import org.example.menu.Utilidades.UtilidadesMenu;
import org.example.model.Juego;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UtilidadesJuegos {

    private final Scanner sc = new Scanner(System.in);
    private final JuegoDAO dao = new JuegoDAO();
    private final SocioDAO socioDAO = new SocioDAO();

    // Registrar un nuevo juego en BD
    public void registrar() {
        try {
            System.out.println("\nREGISTRAR JUEGO");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();

            System.out.print("Autores: ");
            String autores = sc.nextLine().trim();

            System.out.print("Descripción: ");
            String descripcion = sc.nextLine().trim();

            System.out.print("Número mínimo de jugadores: ");
            int jugadoresMin = UtilidadesMenu.leerEntero();

            System.out.print("Número máximo de jugadores: ");
            int jugadoresMax = UtilidadesMenu.leerEntero();

            System.out.print("Duración en minutos: ");
            int duracion = UtilidadesMenu.leerEntero();

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
            int opcionPropietario = UtilidadesMenu.leerEntero();

            String propietario = null;

            if (opcionPropietario == 2) {
                System.out.print("Documento del socio propietario: ");
                propietario = sc.nextLine().trim();

                if (propietario.isEmpty() || !socioDAO.existe(propietario)) {
                    System.out.println(
                            "No existe ningún socio con ese documento."
                    );
                    return;
                }

            } else if (opcionPropietario != 1) {
                System.out.println("Opción de propietario no válida.");
                return;
            }

            if (nombre.isEmpty() || jugadoresMin < 1
                    || jugadoresMax < jugadoresMin || duracion <= 0) {
                System.out.println(
                        "Revisa el nombre y los valores numéricos."
                );
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

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    public void listar() {
        try {
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

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            System.out.println("\nBUSCAR JUEGO");
            System.out.println("1. Buscar por ID");
            System.out.println("2. Buscar por nombre");
            System.out.println("3. Buscar por temática");
            System.out.println("4. Buscar por número de jugadores");
            System.out.print("Opción: ");

            int opcion = UtilidadesMenu.leerEntero();
            List<Juego> juegos = null;

            if (opcion == 1) {
                System.out.print("ID del juego: ");
                int id = UtilidadesMenu.leerEntero();
                if (id <= 0) {
                    System.out.println("El ID debe ser mayor que cero.");
                    return;
                }
                Juego juego = dao.buscarPorId(id);

                if (juego == null) {
                    System.out.println("No se ha encontrado ese juego.");
                } else {
                    System.out.println(juego);
                }

                return;

            } else if (opcion == 2) {
                System.out.print("Nombre del juego: ");
                String nombre = sc.nextLine().trim();
                juegos = dao.buscarPorNombre(nombre);

            } else if (opcion == 3) {
                System.out.print("Temática: ");
                String tematica = sc.nextLine().trim();
                juegos = dao.buscarPorTematica(tematica);

            } else if (opcion == 4) {
                System.out.print("Número de jugadores: ");
                int jugadores = UtilidadesMenu.leerEntero();

                if (jugadores < 1) {
                    System.out.println(
                            "El número debe ser mayor que cero."
                    );
                    return;
                }

                juegos = dao.buscarPorNumeroJugadores(jugadores);

            } else {
                System.out.println("Opción no válida.");
                return;
            }

            if (juegos.isEmpty()) {
                System.out.println("No se han encontrado juegos.");
            } else {
                for (Juego juego : juegos) {
                    System.out.println(juego);
                }

                System.out.println(
                        "Total de juegos encontrados: " + juegos.size()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    public void modificar() {
        try {
            System.out.print("ID del juego: ");
            int id = UtilidadesMenu.leerEntero();
            if (id <= 0) {
                System.out.println("El ID debe ser mayor que cero.");
                return;
            }
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
            int jugadoresMin = UtilidadesMenu.leerEntero();

            System.out.print("Jugadores máximos: ");
            int jugadoresMax = UtilidadesMenu.leerEntero();

            System.out.print("Duración en minutos: ");
            int duracion = UtilidadesMenu.leerEntero();

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
            int opcionPropietario = UtilidadesMenu.leerEntero();

            String propietario = null;

            if (opcionPropietario == 2) {
                System.out.print("Documento del socio propietario: ");
                propietario = sc.nextLine().trim();

                if (propietario.isEmpty() || !socioDAO.existe(propietario)) {
                    System.out.println(
                            "No existe ningún socio con ese documento."
                    );
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
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println(
                        "La fecha debe ser válida y tener el formato AAAA-MM-DD."
                );
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

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    public void darDeBaja() {
        try {
            System.out.print("ID del juego: ");
            int id = UtilidadesMenu.leerEntero();
            if (id <= 0) {
                System.out.println("El ID debe ser mayor que cero.");
                return;
            }
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

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
