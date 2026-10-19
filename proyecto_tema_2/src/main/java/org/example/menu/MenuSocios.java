
package org.example.menu;

import org.example.dao.SocioDAO;
import org.example.model.Socio;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuSocios {

    private final Scanner sc = new Scanner(System.in);
    private final SocioDAO dao = new SocioDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE SOCIOS");
            System.out.println("1. Registrar socio");
            System.out.println("2. Listar socios");
            System.out.println("3. Modificar socio");
            System.out.println("4. Dar de baja socio");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> modificar();
                    case 4 -> darDeBaja();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void registrar() throws SQLException {
        System.out.println("\nREGISTRAR SOCIO");

        System.out.print("DNI/NIE/Pasaporte: ");
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
            System.out.println("Revisa los datos obligatorios y la edad.");
            return;
        }

        Socio socio = new Socio(
                documento,
                nombre,
                apellidos,
                telefono.isEmpty() ? null : telefono,
                edad,
                email.isEmpty() ? null : email,
                true
        );

        if (dao.insertar(socio)) {
            System.out.println("Socio registrado correctamente.");
        } else {
            System.out.println("No se ha podido registrar el socio.");
        }
    }

    private void listar() throws SQLException {
        List<Socio> socios = dao.listarTodas();

        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
            return;
        }

        System.out.println("\nLISTADO DE SOCIOS");

        for (Socio socio : socios) {
            System.out.println(socio);
        }

        System.out.println("Total de socios: " + socios.size());
    }

    private void modificar() throws SQLException {
        System.out.println("\nMODIFICAR SOCIO");

        System.out.print("Documento del socio: ");
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

        System.out.print("¿Está activo? (s/n): ");
        boolean activo = sc.nextLine().trim().equalsIgnoreCase("s");

        if (documento.isEmpty() || nombre.isEmpty()
                || apellidos.isEmpty() || edad < 0) {
            System.out.println("Datos no válidos.");
            return;
        }

        Socio socio = new Socio(
                documento,
                nombre,
                apellidos,
                telefono.isEmpty() ? null : telefono,
                edad,
                email.isEmpty() ? null : email,
                activo
        );

        if (dao.actualizar(socio)) {
            System.out.println("Socio modificado correctamente.");
        } else {
            System.out.println("No existe un socio con ese documento.");
        }
    }

    private void darDeBaja() throws SQLException {
        System.out.println("\nDAR DE BAJA SOCIO");

        System.out.print("Documento del socio: ");
        String documento = sc.nextLine().trim();

        if (documento.isEmpty()) {
            System.out.println("El documento no puede estar vacío.");
            return;
        }

        System.out.print("¿Confirmas la baja? (s/n): ");
        String confirmacion = sc.nextLine().trim();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada.");
            return;
        }

        if (dao.darDeBaja(documento)) {
            System.out.println("Socio dado de baja correctamente.");
        } else {
            System.out.println("No existe un socio con ese documento.");
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
}
