
package org.example.menu;

import org.example.dao.ActividadSocioDAO;
import org.example.dao.SocioDAO;
import org.example.model.ActividadSocio;
import org.example.model.Socio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuHistorialSocio {

    private final Scanner sc = new Scanner(System.in);
    private final SocioDAO socioDAO = new SocioDAO();
    private final ActividadSocioDAO actividadDAO =
            new ActividadSocioDAO();

    public void mostrar() {
        System.out.println(
                "\n----- HISTORIAL Y ESTADÍSTICAS DE UN SOCIO -----");

        try {
            List<Socio> socios = socioDAO.listarTodas();

            if (socios.isEmpty()) {
                System.out.println("No hay socios registrados.");
                return;
            }

            Socio socio = elegirSocio(socios);

            if (socio == null) {
                return;
            }

            System.out.println("\nSocio seleccionado: "
                    + socio.getNombre() + " "
                    + socio.getApellidos()
                    + " (" + socio.getDocumento() + ")");

            LocalDate desde = leerFecha(
                    "Fecha inicial (AAAA-MM-DD): ");

            LocalDate hasta = leerFecha(
                    "Fecha final (AAAA-MM-DD): ");

            if (hasta.isBefore(desde)) {
                System.out.println(
                        "La fecha final no puede ser anterior a la inicial.");
                return;
            }

            List<ActividadSocio> actividades =
                    actividadDAO.listarEntreFechas(
                            socio.getDocumento(), desde, hasta);

            mostrarActividades(actividades, desde, hasta);
            mostrarEstadisticas(actividades);

        } catch (SQLException e) {
            System.out.println(
                    "Error de base de datos: " + e.getMessage());
        }
    }

    private Socio elegirSocio(List<Socio> socios) {
        while (true) {
            System.out.println("\nSOCIOS REGISTRADOS");

            for (int i = 0; i < socios.size(); i++) {
                Socio socio = socios.get(i);

                System.out.printf(
                        "%d. %s %s | Documento: %s | %s%n",
                        i + 1,
                        socio.getNombre(),
                        socio.getApellidos(),
                        socio.getDocumento(),
                        socio.isActiva() ? "Activo" : "De baja");
            }

            System.out.println("0. Volver");
            System.out.print("Selecciona el socio: ");

            int opcion = leerEntero();

            if (opcion == 0) {
                return null;
            }

            if (opcion < 1 || opcion > socios.size()) {
                System.out.println("Selección no válida.");
                continue;
            }

            return socios.get(opcion - 1);
        }
    }

    private void mostrarActividades(
            List<ActividadSocio> actividades,
            LocalDate desde,
            LocalDate hasta) {

        System.out.println("\nACTIVIDADES ENTRE "
                + desde + " Y " + hasta);

        if (actividades.isEmpty()) {
            System.out.println(
                    "El socio no tiene actividades en este periodo.");
            return;
        }

        System.out.printf(
                "%-8s %-12s %-8s %-28s %-14s %-12s%n",
                "Partida",
                "Fecha",
                "Hora",
                "Juego",
                "Tipo",
                "Resultado");

        System.out.println(
                "--------------------------------------------------------------------------------");

        for (ActividadSocio actividad : actividades) {
            String resultado;

            if (!actividad.isResultadoRegistrado()) {
                resultado = "Pendiente";
            } else if (actividad.isGanadora()) {
                resultado = "Victoria";
            } else {
                resultado = "Derrota";
            }

            System.out.printf(
                    "%-8d %-12s %-8s %-28s %-14s %-12s%n",
                    actividad.getIdPartida(),
                    actividad.getFecha(),
                    actividad.getHora(),
                    acortar(actividad.getNombreJuego(), 27),
                    actividad.getTipoJuego(),
                    resultado);
        }
    }

    private void mostrarEstadisticas(
            List<ActividadSocio> actividades) {

        int total = actividades.size();
        int victorias = 0;
        int derrotas = 0;
        int pendientes = 0;
        int duracionTotal = 0;

        int competitivos = 0;
        int cooperativos = 0;
        int ambos = 0;

        Set<String> juegosDistintos = new HashSet<>();

        for (ActividadSocio actividad : actividades) {

            if (!actividad.isResultadoRegistrado()) {
                pendientes++;
            } else if (actividad.isGanadora()) {
                victorias++;
            } else {
                derrotas++;
            }

            duracionTotal += actividad.getDuracionMinutos();
            juegosDistintos.add(actividad.getNombreJuego());

            switch (actividad.getTipoJuego()) {
                case "COMPETITIVO" -> competitivos++;
                case "COOPERATIVO" -> cooperativos++;
                case "AMBOS" -> ambos++;
                default -> { }
            }
        }

        int partidasConResultado = victorias + derrotas;

        double porcentajeVictorias =
                partidasConResultado == 0
                        ? 0.0
                        : victorias * 100.0 / partidasConResultado;

        System.out.println("\nESTADÍSTICAS DEL PERIODO");
        System.out.println("----------------------------------------");
        System.out.println("Total de actividades: " + total);
        System.out.println("Victorias: " + victorias);
        System.out.println("Derrotas: " + derrotas);
        System.out.println("Partidas pendientes: " + pendientes);

        System.out.printf(
                "Porcentaje de victorias: %.2f%%%n",
                porcentajeVictorias);

        System.out.println(
                "Juegos diferentes: " + juegosDistintos.size());

        System.out.println(
                "Juegos competitivos: " + competitivos);

        System.out.println(
                "Juegos cooperativos: " + cooperativos);

        System.out.println(
                "Juegos de tipo ambos: " + ambos);

        System.out.println("Duración estimada acumulada: "
                + duracionTotal + " minutos ("
                + (duracionTotal / 60) + " h "
                + (duracionTotal % 60) + " min)");

        System.out.println(
                "La duración se calcula usando la duración configurada de cada juego.");
    }

    private String acortar(String texto, int maximo) {
        if (texto == null) {
            return "";
        }

        if (texto.length() <= maximo) {
            return texto;
        }

        return texto.substring(0, maximo - 3) + "...";
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();

            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Fecha no válida. Usa AAAA-MM-DD, "
                                + "por ejemplo 2026-10-21.");
            }
        }
    }

    private int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print(
                        "Introduce un número válido: ");
            }
        }
    }
}
