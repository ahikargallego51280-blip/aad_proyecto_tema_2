
package org.example.menu;

import org.example.dao.AsociacionDAO;
import org.example.model.Asociacion;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuAsociacion {

    private final Scanner sc = new Scanner(System.in);
    private final AsociacionDAO asociacionDAO = new AsociacionDAO();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ ASOCIACIÓN");
            System.out.println("1. Consultar información");
            System.out.println("2. Registrar asociación");
            System.out.println("3. Modificar información");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> consultar();
                    case 2 -> registrar();
                    case 3 -> modificar();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void consultar() throws SQLException {
        Asociacion asociacion = asociacionDAO.obtener();

        if (asociacion == null) {
            System.out.println("No hay ninguna asociación registrada.");
            return;
        }

        System.out.println("\nINFORMACIÓN DE LA ASOCIACIÓN");
        System.out.println("Nombre: " + asociacion.getNombre());
        System.out.println("CIF: " + asociacion.getCif());
        System.out.println("Dirección: " + asociacion.getDireccion());
        System.out.println("Teléfono: " + valorOIndicar(asociacion.getTelefono()));
        System.out.println("Email: " + valorOIndicar(asociacion.getEmail()));
    }

    private void registrar() throws SQLException {
        if (asociacionDAO.obtener() != null) {
            System.out.println("Ya existe una asociación. Puedes modificar sus datos.");
            return;
        }

        System.out.println("\nREGISTRAR ASOCIACIÓN");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        System.out.print("CIF: ");
        String cif = sc.nextLine().trim();

        System.out.print("Dirección: ");
        String direccion = sc.nextLine().trim();

        System.out.print("Teléfono (opcional): ");
        String telefono = sc.nextLine().trim();

        System.out.print("Email (opcional): ");
        String email = sc.nextLine().trim();

        if (nombre.isEmpty() || cif.isEmpty() || direccion.isEmpty()) {
            System.out.println("El nombre, CIF y dirección son obligatorios.");
            return;
        }

        Asociacion asociacion = new Asociacion(
                0,
                nombre,
                cif,
                direccion,
                telefono.isEmpty() ? null : telefono,
                email.isEmpty() ? null : email
        );

        if (asociacionDAO.insertar(asociacion)) {
            System.out.println("Asociación registrada correctamente.");
        } else {
            System.out.println("No se ha podido registrar la asociación.");
        }
    }

    private void modificar() throws SQLException {
        Asociacion asociacion = asociacionDAO.obtener();

        if (asociacion == null) {
            System.out.println("No hay ninguna asociación registrada.");
            return;
        }

        System.out.println("\nMODIFICAR ASOCIACIÓN");
        System.out.println("Deja el campo vacío para conservar su valor.");

        System.out.print("Nombre (" + asociacion.getNombre() + "): ");
        String nombre = sc.nextLine().trim();
        if (!nombre.isEmpty()) {
            asociacion.setNombre(nombre);
        }

        System.out.print("CIF (" + asociacion.getCif() + "): ");
        String cif = sc.nextLine().trim();
        if (!cif.isEmpty()) {
            asociacion.setCif(cif);
        }

        System.out.print("Dirección (" + asociacion.getDireccion() + "): ");
        String direccion = sc.nextLine().trim();
        if (!direccion.isEmpty()) {
            asociacion.setDireccion(direccion);
        }

        System.out.print("Teléfono (" + valorOIndicar(asociacion.getTelefono()) + "): ");
        String telefono = sc.nextLine().trim();
        if (!telefono.isEmpty()) {
            asociacion.setTelefono(telefono);
        }

        System.out.print("Email (" + valorOIndicar(asociacion.getEmail()) + "): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) {
            asociacion.setEmail(email);
        }

        if (asociacion.getNombre().isBlank()
                || asociacion.getCif().isBlank()
                || asociacion.getDireccion().isBlank()) {
            System.out.println("El nombre, CIF y dirección son obligatorios.");
            return;
        }

        if (asociacionDAO.actualizar(asociacion)) {
            System.out.println("Datos modificados correctamente.");
        } else {
            System.out.println("No se han podido modificar los datos.");
        }
    }

    private String valorOIndicar(String valor) {
        return valor == null || valor.isBlank() ? "No indicado" : valor;
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
