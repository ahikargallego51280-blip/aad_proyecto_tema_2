
package org.example.menu;

import org.example.dao.SociaDAO;
import org.example.model.Socia;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuSocias {

    private final Scanner sc = new Scanner(System.in);
    private final SociaDAO dao = new SociaDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          👥 GESTIÓN DE SOCIAS        ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("1. ➕ Registrar socia");
            System.out.println("2. 📋 Listar socias");
            System.out.println("3. ✏️ Modificar socia");
            System.out.println("4. 🔴 Dar de baja socia");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("👉 Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> modificar();
                    case 4 -> darDeBaja();
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
        System.out.println("\n➕ REGISTRAR SOCIA");

        System.out.print("Documento: ");
        String documento = sc.nextLine().trim();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine().trim();

        System.out.print("Teléfono (opcional): ");
        String telefono = sc.nextLine().trim();

        System.out.print("Edad: ");
        int edad = leerEntero();

        System.out.print("Email (opcional): ");
        String email = sc.nextLine().trim();

        if (documento.isEmpty() || nombre.isEmpty()
                || apellidos.isEmpty() || edad < 0) {
            System.out.println("❌ Revisa los datos obligatorios y la edad.");
            return;
        }

        Socia socia = new Socia(
                documento,
                nombre,
                apellidos,
                telefono.isEmpty() ? null : telefono,
                edad,
                email.isEmpty() ? null : email,
                true
        );

        if (dao.insertar(socia)) {
            System.out.println("✅ Socia registrada correctamente.");
        }
    }

    private void listar() throws SQLException {
        System.out.println("\n📋 LISTADO DE SOCIAS");

        List<Socia> socias = dao.listarTodas();

        if (socias.isEmpty()) {
            System.out.println("📭 No hay socias registradas.");
            return;
        }

        for (Socia socia : socias) {
            System.out.println("------------------------------");
            System.out.println(socia);
        }

        System.out.println("------------------------------");
        System.out.println("Total: " + socias.size());
    }

    private void modificar() throws SQLException {
        System.out.println("\n✏️ MODIFICAR SOCIA");

        System.out.print("Documento de la socia: ");
        String documento = sc.nextLine().trim();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("Nuevos apellidos: ");
        String apellidos = sc.nextLine().trim();

        System.out.print("Nuevo teléfono (opcional): ");
        String telefono = sc.nextLine().trim();

        System.out.print("Nueva edad: ");
        int edad = leerEntero();

        System.out.print("Nuevo email (opcional): ");
        String email = sc.nextLine().trim();

        System.out.print("¿Está activa? (s/n): ");
        boolean activa = sc.nextLine().trim().equalsIgnoreCase("s");

        if (documento.isEmpty() || nombre.isEmpty()
                || apellidos.isEmpty() || edad < 0) {
            System.out.println("❌ Datos no válidos.");
            return;
        }

        Socia socia = new Socia(
                documento,
                nombre,
                apellidos,
                telefono.isEmpty() ? null : telefono,
                edad,
                email.isEmpty() ? null : email,
                activa
        );

        if (dao.actualizar(socia)) {
            System.out.println("✅ Socia modificada.");
        } else {
            System.out.println("🔎 No existe una socia con ese documento.");
        }
    }

    private void darDeBaja() throws SQLException {
        System.out.println("\n🔴 DAR DE BAJA SOCIA");

        System.out.print("Documento de la socia: ");
        String documento = sc.nextLine().trim();

        System.out.print("¿Confirmas la baja? (s/n): ");
        String confirmacion = sc.nextLine().trim();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("↩️ Operación cancelada.");
            return;
        }

        if (dao.darDeBaja(documento)) {
            System.out.println("✅ Socia dada de baja.");
        } else {
            System.out.println("🔎 No existe una socia con ese documento.");
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
