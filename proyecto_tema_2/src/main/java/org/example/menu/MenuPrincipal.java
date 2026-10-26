package org.example.menu;

import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.dao.AsociacionDAO;
import org.example.export.json.ExportadorJSON;
import org.example.export.xml.ExportadorXML;
import org.example.menu.Asociaciones.MenuAsociacion;
import org.example.menu.BBDD.MenuBaseDatos;
import org.example.menu.Estadisticas.MenuEstadisticas;
import org.example.menu.JSON.MenuExportarJSON;
import org.example.menu.Juegos.MenuJuegos;
import org.example.menu.Participaciones.MenuParticipaciones;
import org.example.menu.Partidas.MenuPartidas;
import org.example.menu.Partidas.UtilidadesPartidas;
import org.example.menu.Socios.MenuSocios;
import org.example.menu.Utilidades.UtilidadesMenu;
import org.example.menu.XML.MenuExportarXML;
import org.example.model.Asociacion;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);

    public void mostrar() {
        int opcion;

        do {
            System.out.println();
            System.out.println("--------------------------------------");

            UtilidadesMenu.mostrarDatosAsociacion();

            System.out.println("Sistema de gestion");
            System.out.println("--------------------------------------");
            System.out.println("1. Gestion de socios");
            System.out.println("2. Gestion de juegos");
            System.out.println("3. Gestion de partidas");
            System.out.println("4. Gestion de participantes");
            System.out.println("5. Exportar datos JSON");
            System.out.println("6. Informacion de la base de datos");
            System.out.println("7. Gestion de la asociacion");
            System.out.println("8. Exportar datos XML");
            System.out.println("9. Historial de socios");
            System.out.println("10. Estadisticas de la asociacion");
            System.out.println("0. Salir");

            System.out.print("Selecciona una opcion: ");
            opcion = UtilidadesMenu.leerEntero();

            if (opcion == 1) {
                new MenuSocios().mostrar();

            } else if (opcion == 2) {
                new MenuJuegos().mostrar();

            } else if (opcion == 3) {
                new MenuPartidas().mostrar();

            } else if (opcion == 4) {
                new MenuParticipaciones().mostrar();

            } else if (opcion == 5) {
                new MenuExportarJSON().mostrar();

            } else if (opcion == 6) {
                new MenuBaseDatos().mostrar();

            } else if (opcion == 7) {
                new MenuAsociacion().mostrar();

            } else if (opcion == 8) {
                new MenuExportarXML().mostrar();

            } else if (opcion == 9) {
                new MenuHistorialSocio().mostrar();

            } else if (opcion == 10) {
                new MenuEstadisticas().mostrar();

            } else if (opcion == 0) {
                System.out.println("Gracias por utilizar la aplicacion.");

            } else {
                System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }
}