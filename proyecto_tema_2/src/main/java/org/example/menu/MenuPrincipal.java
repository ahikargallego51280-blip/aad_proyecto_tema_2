
package org.example.menu;

import org.example.dao.AsociacionDAO;
import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.export.json.ExportadorJSON;
import org.example.export.xml.ExportadorXML;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);

    public void mostrar() {
        int opcion;

        do {
            System.out.println();
            System.out.println("--------------------------------------");
            System.out.println("ASOCIACION DE JUEGOS");
            System.out.println("Sistema de gestion");
            System.out.println("--------------------------------------");

            System.out.println("1. Gestion de socios");
            System.out.println("2. Gestion de juegos");
            System.out.println("3. Gestion de partidas");
            System.out.println("4. Gestion de participantes");
            System.out.println("5. Exportar datos JSON");
            System.out.println("6. Informacion de la base de datos");
            System.out.println("7. Informacion de la asociacion");
            System.out.println("8. Exportar datos XML");
            System.out.println("0. Salir");

            System.out.print("Selecciona una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> new MenuSocios().mostrar();

                case 2 -> new MenuJuegos().mostrar();

                case 3 -> new MenuPartidas().mostrar();

                case 4 -> new MenuParticipaciones().mostrar();

                case 5 -> exportarJSON();

                case 6 -> new MenuBaseDatos().mostrar();

                case 7 -> mostrarInformacion();

                case 8 -> exportarXML();

                case 0 -> System.out.println(
                        "Gracias por utilizar la aplicacion."
                );

                default -> System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    private void exportarJSON() {
        ExportadorJSON exportador = new ExportadorJSON();

        try {
            System.out.println();
            System.out.println("----- EXPORTAR DATOS JSON -----");
            System.out.println("1. Exportar socios");
            System.out.println("2. Exportar juegos");
            System.out.println("3. Exportar partidas");
            System.out.println("0. Volver");
            System.out.print("Selecciona una opcion: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    SocioDAO dao = new SocioDAO();
                    exportador.exportarSocios(dao.listarTodas());
                }

                case 2 -> {
                    JuegoDAO dao = new JuegoDAO();
                    exportador.exportarJuegos(dao.listarTodos());
                }

                case 3 -> {
                    PartidaDAO dao = new PartidaDAO();
                    exportador.exportarPartidas(dao.listarTodas());
                }

                case 0 -> System.out.println(
                        "Volviendo al menu principal."
                );

                default -> System.out.println("Opcion no valida.");
            }

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los datos.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void exportarXML() {
        ExportadorXML exportador = new ExportadorXML();

        try {
            System.out.println();
            System.out.println("----- EXPORTAR DATOS XML -----");
            System.out.println("1. Exportar socios");
            System.out.println("2. Exportar juegos");
            System.out.println("3. Exportar partidas");
            System.out.println("0. Volver");
            System.out.print("Selecciona una opcion: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    SocioDAO dao = new SocioDAO();
                    exportador.exportarSocios(dao.listarTodas());
                }

                case 2 -> {
                    JuegoDAO dao = new JuegoDAO();
                    exportador.exportarJuegos(dao.listarTodos());
                }

                case 3 -> {
                    PartidaDAO dao = new PartidaDAO();
                    exportador.exportarPartidas(dao.listarTodas());
                }

                case 0 -> System.out.println(
                        "Volviendo al menu principal."
                );

                default -> System.out.println("Opcion no valida.");
            }

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los datos XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void mostrarInformacion() {
        AsociacionDAO dao = new AsociacionDAO();

        try {
            var asociacion = dao.obtener();

            if (asociacion == null) {
                System.out.println("No hay datos de la asociacion.");
                return;
            }

            System.out.println();
            System.out.println("INFORMACION DE LA ASOCIACION");
            System.out.println("Nombre: " + asociacion.getNombre());
            System.out.println("CIF: " + asociacion.getCif());
            System.out.println("Direccion: " + asociacion.getDireccion());
            System.out.println("Telefono: " + asociacion.getTelefono());
            System.out.println("Email: " + asociacion.getEmail());

        } catch (SQLException e) {
            System.out.println("Error al consultar la asociacion.");
            System.out.println(e.getMessage());
        }
    }

    private int leerEntero() {
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
