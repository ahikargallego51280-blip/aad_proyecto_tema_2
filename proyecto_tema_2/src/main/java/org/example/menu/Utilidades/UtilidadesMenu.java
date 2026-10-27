package org.example.menu.Utilidades;

import org.example.dao.AsociacionDAO;
import org.example.model.Asociacion;

import java.sql.SQLException;
import java.util.Scanner;

public class UtilidadesMenu {

    private static final Scanner sc = new Scanner(System.in);

    public static int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Introduce un numero valido: ");
            }
        }
    }

    public static void mostrarDatosAsociacion() {
        AsociacionDAO dao = new AsociacionDAO();

        try {
            Asociacion asociacion = dao.obtener();

            if (asociacion == null) {
                System.out.println("No hay datos de la asociacion registrados.");
                return;
            }

            System.out.println(asociacion.getNombre());
            System.out.println("CIF: " + asociacion.getCif());
            System.out.println("Direccion: " + asociacion.getDireccion());

            String telefono = asociacion.getTelefono();
            System.out.println("Telefono: " +
                    (telefono == null || telefono.isBlank()
                            ? "No registrado" : telefono));

            String email = asociacion.getEmail();
            System.out.println("Email: " +
                    (email == null || email.isBlank()
                            ? "No registrado" : email));

        } catch (SQLException e) {
            System.out.println("No se pudo cargar la informacion de la asociacion.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}