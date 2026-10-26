
package org.example.menu.XML;

import org.example.menu.Utilidades.UtilidadesMenu;

public class MenuExportarXML {

    private final UtilidadesXML utilidades = new UtilidadesXML();

    public void mostrar() {
        System.out.println();
        System.out.println("----- EXPORTAR DATOS XML -----");
        System.out.println("1. Exportar socios");
        System.out.println("2. Exportar juegos");
        System.out.println("3. Exportar partidas");
        System.out.println("0. Volver");
        System.out.print("Selecciona una opcion: ");

        int opcion = UtilidadesMenu.leerEntero();

        if (opcion == 1) {
            utilidades.exportarSocios();
        } else if (opcion == 2) {
            utilidades.exportarJuegos();
        } else if (opcion == 3) {
            utilidades.exportarPartidas();
        } else if (opcion == 0) {
            System.out.println("Volviendo al menu principal.");
        } else {
            System.out.println("Opcion no valida.");
        }
    }
}
