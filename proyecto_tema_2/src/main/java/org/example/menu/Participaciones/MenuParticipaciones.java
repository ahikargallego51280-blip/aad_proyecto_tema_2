
package org.example.menu.Participaciones;

import org.example.menu.Utilidades.UtilidadesMenu;

import java.sql.SQLException;

public class MenuParticipaciones {

    private final UtilidadesParticipaciones utilidades =
            new UtilidadesParticipaciones();

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ DE PARTICIPANTES");
            System.out.println("1. Inscribir socio");
            System.out.println("2. Inscribir invitado");
            System.out.println("3. Listar participantes");
            System.out.println("4. Registrar ganador");
            System.out.println("5. Retirar participante");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = UtilidadesMenu.leerEntero();

            try {
                if (opcion == 1) {
                    utilidades.inscribirSocio();

                } else if (opcion == 2) {
                    utilidades.inscribirInvitado();

                } else if (opcion == 3) {
                    utilidades.listarParticipantes();

                } else if (opcion == 4) {
                    utilidades.registrarGanador();

                } else if (opcion == 5) {
                    utilidades.retirarParticipante();

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
