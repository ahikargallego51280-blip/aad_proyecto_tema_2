
package org.example.menu.Socios;

import org.example.dao.SocioDAO;
import org.example.model.Socio;
import org.example.menu.Utilidades.UtilidadesMenu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UtilidadesSocios {

    private final Scanner sc = new Scanner(System.in);
    private final SocioDAO dao = new SocioDAO();

    // Registrar un socio
    public void registrar() throws SQLException {
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
        int edad = UtilidadesMenu.leerEntero();

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

    // Listar todos los socios
    public void listar() throws SQLException {
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

    // Buscar socios por nombre o apellidos
    public void buscar() throws SQLException {
        System.out.println("\nBUSCAR SOCIO");
        System.out.println("1. Buscar por nombre");
        System.out.println("2. Buscar por apellidos");
        System.out.print("Opción: ");

        int opcion = UtilidadesMenu.leerEntero();
        List<Socio> socios;

        if (opcion == 1) {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }

            socios = dao.buscarPorNombre(nombre);

        } else if (opcion == 2) {
            System.out.print("Apellidos: ");
            String apellidos = sc.nextLine().trim();

            if (apellidos.isEmpty()) {
                System.out.println("Los apellidos no pueden estar vacíos.");
                return;
            }

            socios = dao.buscarPorApellidos(apellidos);

        } else {
            System.out.println("Opción no válida.");
            return;
        }

        if (socios.isEmpty()) {
            System.out.println("No se han encontrado socios.");
        } else {
            System.out.println("\nRESULTADOS DE LA BÚSQUEDA");

            for (Socio socio : socios) {
                System.out.println(socio);
            }

            System.out.println(
                    "Total de socios encontrados: " + socios.size()
            );
        }
    }

    // Modificar un socio
    public void modificar() throws SQLException {
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
        int edad = UtilidadesMenu.leerEntero();

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

    // Dar de baja un socio
    public void darDeBaja() throws SQLException {
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
}
