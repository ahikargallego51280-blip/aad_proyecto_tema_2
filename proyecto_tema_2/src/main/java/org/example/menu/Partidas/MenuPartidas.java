
package org.example.menu.Partidas;

import org.example.menu.Utilidades.UtilidadesMenu;
import java.sql.SQLException;

public class MenuPartidas {

    private final UtilidadesPartidas utilidades = new UtilidadesPartidas();

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

            opcion = UtilidadesMenu.leerEntero();

            try {
                if (opcion == 1) {
                    utilidades.crear();
                } else if (opcion == 2) {
                    utilidades.listar();
                } else if (opcion == 3) {
                    utilidades.modificar();
                } else if (opcion == 4) {
                    utilidades.cancelar();
                } else if (opcion == 5) {
                    utilidades.buscar();
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
}
