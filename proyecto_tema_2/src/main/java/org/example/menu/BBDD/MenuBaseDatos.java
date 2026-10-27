
package org.example.menu.BBDD;

import org.example.dao.data.ExportadorMetadatos;
import org.example.menu.Utilidades.UtilidadesMenu;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuBaseDatos {

    private final Scanner sc = new Scanner(System.in);

    private final UtilidadesBaseDatos utilidades =
            new UtilidadesBaseDatos();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ BASE DE DATOS");
            System.out.println("1. Información de conexión");
            System.out.println("2. Listar tablas");
            System.out.println("3. Consultar estructura de una tabla");
            System.out.println("4. Exportar metadatos a JSON");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = UtilidadesMenu.leerEntero();

            try {
                if (opcion == 1) {
                    utilidades.mostrarConexion();

                } else if (opcion == 2) {
                    utilidades.listarTablas();

                } else if (opcion == 3) {
                    System.out.print("Nombre de la tabla: ");
                    String tabla = sc.nextLine().trim();

                    utilidades.mostrarEstructura(tabla);

                } else if (opcion == 4) {
                    utilidades.exportarMetadatos();

                } else if (opcion == 0) {
                    System.out.println(
                            "Volviendo al menú principal..."
                    );

                } else {
                    System.out.println("Opción no válida.");
                }

            } catch (SQLException e) {
                System.out.println(
                        "Error de base de datos: " + e.getMessage()
                );
            }

        } while (opcion != 0);
    }
}
