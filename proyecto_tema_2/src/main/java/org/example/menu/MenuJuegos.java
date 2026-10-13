
package org.example.menu;

import org.example.dao.JuegoDAO;
import org.example.model.Juego;
import org.example.dao.SociaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuJuegos {

    private final Scanner sc = new Scanner(System.in);
    private final JuegoDAO dao = new JuegoDAO();
    private final SociaDAO sociaDAO = new SociaDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          🎮 GESTIÓN DE JUEGOS        ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("1. ➕ Registrar juego");
            System.out.println("2. 📋 Listar juegos");
            System.out.println("3. 🔎 Buscar juego por ID");
            System.out.println("4. ✏️ Modificar juego");
            System.out.println("5. 🔴 Dar de baja juego");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("👉 Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> modificar();
                    case 5 -> darDeBaja();
                    case 0 -> System.out.println("↩️ Volviendo...");
                    default -> System.out.println("❌ Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("❌ Error de base de datos: "
                        + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void registrar() throws SQLException {
        System.out.println("\n➕ ===== REGISTRAR JUEGO =====");

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

        System.out.println("Tipo: COMPETITIVO, COOPERATIVO o AMBOS");
        System.out.print("Tipo: ");
        String tipo = sc.nextLine().trim().toUpperCase();

        System.out.print("Ubicación: ");
        String ubicacion = sc.nextLine().trim();

        System.out.println("Estado: DISPONIBLE, PRESTADO o REPARACION");
        System.out.print("Estado: ");
        String estado = sc.nextLine().trim().toUpperCase();

        System.out.print("Documento de la socia propietaria: ");
        String propietario = sc.nextLine().trim();

        if (!propietario.isEmpty()
                && !sociaDAO.existe(propietario)) {
            System.out.println("No existe ninguna socia con ese documento.");
            return;
        }

        if (propietario.isEmpty()) {
            propietario = null;
        }

        if (nombre.isEmpty() || jugadoresMin < 1
                || jugadoresMax < jugadoresMin || duracion <= 0) {
            System.out.println("❌ Revisa el nombre y los valores numéricos.");
            return;
        }

        if (!tipo.equals("COMPETITIVO")
                && !tipo.equals("COOPERATIVO")
                && !tipo.equals("AMBOS")) {
            System.out.println("❌ Tipo de juego no válido.");
            return;
        }

        if (!estado.equals("DISPONIBLE")
                && !estado.equals("PRESTADO")
                && !estado.equals("REPARACION")) {
            System.out.println("❌ Estado no válido.");
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
                propietario.isEmpty() ? null : propietario,
                LocalDate.now().toString(),
                true
        );

        if (dao.insertar(juego)) {
            System.out.println("✅ Juego registrado correctamente.");
        }
    }

    private void listar() throws SQLException {
        System.out.println("\n📋 ===== LISTADO DE JUEGOS =====");

        List<Juego> juegos = dao.listarTodos();

        if (juegos.isEmpty()) {
            System.out.println("📭 No hay juegos registrados.");
            return;
        }

        for (Juego juego : juegos) {
            System.out.println("------------------------------");
            System.out.println(juego);
        }

        System.out.println("------------------------------");
        System.out.println("Total de juegos: " + juegos.size());
    }

    private void buscar() throws SQLException {
        System.out.println("\n🔎 ===== BUSCAR JUEGO =====");
        System.out.print("ID del juego: ");

        int id = leerEntero();
        Juego juego = dao.buscarPorId(id);

        if (juego == null) {
            System.out.println("🔎 No se ha encontrado ese juego.");
        } else {
            System.out.println(juego);
        }
    }

    private void modificar() throws SQLException {
        System.out.println("\n✏️ ===== MODIFICAR JUEGO =====");
        System.out.print("ID del juego: ");

        int id = leerEntero();
        Juego existente = dao.buscarPorId(id);

        if (existente == null) {
            System.out.println("🔎 No se ha encontrado ese juego.");
            return;
        }

        System.out.println("Introduce los nuevos datos del juego.");

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

        System.out.print("Documento de la socia propietaria: ");
        String propietario = sc.nextLine().trim();

        System.out.print("Fecha de alta (AAAA-MM-DD): ");
        String fechaAlta = sc.nextLine().trim();

        System.out.print("¿Está activo? (s/n): ");
        boolean activo = sc.nextLine().trim().equalsIgnoreCase("s");

        if (nombre.isEmpty() || jugadoresMin < 1
                || jugadoresMax < jugadoresMin || duracion <= 0) {
            System.out.println("❌ Revisa los valores introducidos.");
            return;
        }

        if (!tipo.equals("COMPETITIVO")
                && !tipo.equals("COOPERATIVO")
                && !tipo.equals("AMBOS")) {
            System.out.println("❌ Tipo no válido.");
            return;
        }

        if (!estado.equals("DISPONIBLE")
                && !estado.equals("PRESTADO")
                && !estado.equals("REPARACION")) {
            System.out.println("❌ Estado no válido.");
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
                propietario.isEmpty() ? null : propietario,
                fechaAlta,
                activo
        );

        if (dao.actualizar(juego)) {
            System.out.println("✅ Juego modificado correctamente.");
        } else {
            System.out.println("❌ No se ha podido modificar el juego.");
        }
    }

    private void darDeBaja() throws SQLException {
        System.out.println("\n🔴 ===== DAR DE BAJA JUEGO =====");
        System.out.print("ID del juego: ");

        int id = leerEntero();

        System.out.print("¿Confirmas la baja? (s/n): ");
        String confirmacion = sc.nextLine().trim();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("↩️ Operación cancelada.");
            return;
        }

        if (dao.darDeBaja(id)) {
            System.out.println("✅ Juego dado de baja.");
        } else {
            System.out.println("🔎 No se ha encontrado ese juego.");
        }
    }

    private int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("❌ Introduce un número válido: ");
            }
        }
    }
}
