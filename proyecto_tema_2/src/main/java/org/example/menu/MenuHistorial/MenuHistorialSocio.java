
package org.example.menu.MenuHistorial;

import org.example.dao.ActividadSocioDAO;
import org.example.dao.SocioDAO;
import org.example.model.ActividadSocio;
import org.example.model.Socio;
import org.example.menu.JSON.UtilidadesJSON;
import org.example.menu.XML.UtilidadesXML;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuHistorialSocio {

    private final Scanner sc = new Scanner(System.in);
    private final SocioDAO socioDAO = new SocioDAO();
    private final ActividadSocioDAO actividadDAO = new ActividadSocioDAO();

    public void mostrar() {
        System.out.println("\n--- HISTORIAL DE SOCIOS ---");

        try {
            List<Socio> socios = socioDAO.listarTodas();

            if (socios.isEmpty()) {
                System.out.println("No hay socios registrados.");
                return;
            }

            for (int i = 0; i < socios.size(); i++) {
                Socio socio = socios.get(i);

                System.out.println((i + 1) + ". "
                        + socio.getNombre() + " "
                        + socio.getApellidos());
            }

            System.out.print("Elige una socia por número: ");

            int opcion;

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número.");
                return;
            }

            if (opcion < 1 || opcion > socios.size()) {
                System.out.println("Opción incorrecta.");
                return;
            }

            Socio socio = socios.get(opcion - 1);

            System.out.print("Fecha inicial (AAAA-MM-DD): ");
            LocalDate desde = LocalDate.parse(sc.nextLine());

            System.out.print("Fecha final (AAAA-MM-DD): ");
            LocalDate hasta = LocalDate.parse(sc.nextLine());

            if (hasta.isBefore(desde)) {
                System.out.println("La fecha final no puede ser anterior.");
                return;
            }

            List<ActividadSocio> actividades =
                    actividadDAO.listarEntreFechas(
                            socio.getDocumento(), desde, hasta);

            System.out.println("\n--- HISTORIAL DE "
                    + socio.getNombre() + " "
                    + socio.getApellidos() + " ---");

            System.out.println("Periodo: " + desde + " - " + hasta);

            int victorias = 0;
            int sinGanadorRegistrado = 0;
            int duracion = 0;

            if (actividades.isEmpty()) {
                System.out.println("No hay actividades en este periodo.");
            } else {
                for (ActividadSocio a : actividades) {
                    System.out.println("\nJuego: " + a.getNombreJuego());
                    System.out.println("Tipo: " + a.getTipoJuego());
                    System.out.println("Fecha: " + a.getFecha());
                    System.out.println("Hora: " + a.getHora());

                    if (a.isResultadoRegistrado()) {
                        if (a.isGanadora()) {
                            System.out.println("Resultado: Victoria");
                            victorias++;
                        } else {
                            System.out.println("Resultado: No ganadora");
                        }
                    } else {
                        System.out.println("Resultado: Sin ganador registrado");
                        sinGanadorRegistrado++;
                    }

                    duracion += a.getDuracionMinutos();

                    System.out.println("--------------------");
                }
            }

            System.out.println("\n--- ESTADÍSTICAS DE LA SOCIA ---");
            System.out.println("Total de actividades: " + actividades.size());
            System.out.println("Victorias: " + victorias);
            System.out.println("Sin ganador registrado: " + sinGanadorRegistrado);
            System.out.println("Duración acumulada de los juegos: "
                    + duracion + " minutos");

            System.out.println("\n¿Quieres exportar el historial?");
            System.out.println("1. JSON");
            System.out.println("2. XML");
            System.out.println("0. No exportar");
            System.out.print("Opción: ");

            String formato = sc.nextLine().trim();
            if (formato.equals("1")) {
                new UtilidadesJSON().exportarHistorial(
                        socio.getDocumento(), desde, hasta);
            } else if (formato.equals("2")) {
                new UtilidadesXML().exportarHistorial(
                        socio.getDocumento(), desde, hasta);
            } else if (formato.equals("0")) {
                System.out.println("No se exportará el historial.");
            } else {
                System.out.println("Opción no válida.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Fecha incorrecta. Usa AAAA-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
        }
    }
}
