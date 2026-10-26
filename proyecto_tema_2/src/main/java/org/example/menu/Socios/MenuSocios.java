
package org.example.menu.Socios;

import org.example.menu.Utilidades.UtilidadesMenu;
import java.sql.SQLException;

public class MenuSocios {

    private final UtilidadesSocios utilidades = new UtilidadesSocios();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE SOCIOS");
            System.out.println("1. Registrar socio");
            System.out.println("2. Listar socios");
            System.out.println("3. Buscar socio");
            System.out.println("4. Modificar socio");
            System.out.println("5. Dar de baja socio");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = UtilidadesMenu.leerEntero();

            try {
                if (opcion == 1) {
                    utilidades.registrar();
                } else if (opcion == 2) {
                    utilidades.listar();
                } else if (opcion == 3) {
                    utilidades.buscar();
                } else if (opcion == 4) {
                    utilidades.modificar();
                } else if (opcion == 5) {
                    utilidades.darDeBaja();
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
