
package org.example.menu.Estadisticas;

import org.example.dao.EstadisticasDAO;
import org.example.menu.JSON.UtilidadesJSON;
import org.example.menu.XML.UtilidadesXML;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class MenuEstadisticas {

    private final Scanner sc = new Scanner(System.in);
    private final EstadisticasDAO estadisticasDAO = new EstadisticasDAO();

    public void mostrar() {
        System.out.println("\n--- ESTADISTICAS DE LA ASOCIACION ---");

        try {
            System.out.print("Fecha inicial (AAAA-MM-DD): ");
            LocalDate desde = LocalDate.parse(sc.nextLine());

            System.out.print("Fecha final (AAAA-MM-DD): ");
            LocalDate hasta = LocalDate.parse(sc.nextLine());

            if (hasta.isBefore(desde)) {
                System.out.println("La fecha final no puede ser anterior.");
                return;
            }

            Map<String, Long> datos =
                    estadisticasDAO.obtenerEstadisticas(desde, hasta);

            System.out.println("\nPeriodo: " + desde + " - " + hasta);

            for (Map.Entry<String, Long> dato : datos.entrySet()) {
                System.out.println(dato.getKey() + ": " + dato.getValue());
            }

            System.out.println("\n¿Quieres exportar las estadisticas?");
            System.out.println("1. JSON");
            System.out.println("2. XML");
            System.out.println("0. No exportar");
            System.out.print("Opcion: ");

            String opcion = sc.nextLine();

            if (opcion.equals("1")) {
                new UtilidadesJSON().exportarEstadisticas(desde, hasta);
            } else if (opcion.equals("2")) {
                new UtilidadesXML().exportarEstadisticas(desde, hasta);
            } else if (opcion.equals("0")) {
            } else {
                System.out.println("Opcion no valida.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Fecha incorrecta. Usa AAAA-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Error al consultar las estadisticas.");
            e.printStackTrace();
        }
    }
}
