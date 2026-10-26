package org.example.menu.Utilidades;

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
}