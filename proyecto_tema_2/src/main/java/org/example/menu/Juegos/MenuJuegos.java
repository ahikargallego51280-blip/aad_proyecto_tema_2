
package org.example.menu.Juegos;

import org.example.menu.Utilidades.UtilidadesMenu;

public class MenuJuegos {

    private final UtilidadesJuegos utilidades = new UtilidadesJuegos();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE JUEGOS");
            System.out.println("1. Registrar juego");
            System.out.println("2. Listar juegos");
            System.out.println("3. Buscar juego");
            System.out.println("4. Modificar juego");
            System.out.println("5. Dar de baja juego");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = UtilidadesMenu.leerEntero();

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

        } while (opcion != 0);
    }
}
