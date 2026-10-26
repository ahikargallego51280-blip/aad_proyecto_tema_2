package org.example.menu.Estadisticas;

import org.example.dao.EstadisticasDAO;

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

        } catch (DateTimeParseException e) {
            System.out.println("Fecha incorrecta. Usa AAAA-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Error al consultar las estadisticas.");
            e.printStackTrace();
        }
    }
}