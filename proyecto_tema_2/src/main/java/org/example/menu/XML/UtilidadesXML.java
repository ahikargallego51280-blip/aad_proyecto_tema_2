
package org.example.menu.XML;

import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.export.xml.ExportadorXML;

public class UtilidadesXML {

    // Exportar socios a XML
    public void exportarSocios() {
        try {
            SocioDAO dao = new SocioDAO();
            ExportadorXML exportador = new ExportadorXML();

            exportador.exportarSocios(dao.listarTodas());

            System.out.println("Socios exportados correctamente.");

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los socios a XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Exportar juegos a XML
    public void exportarJuegos() {
        try {
            JuegoDAO dao = new JuegoDAO();
            ExportadorXML exportador = new ExportadorXML();

            exportador.exportarJuegos(dao.listarTodos());

            System.out.println("Juegos exportados correctamente.");

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los juegos a XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Exportar partidas a XML
    public void exportarPartidas() {
        try {
            PartidaDAO dao = new PartidaDAO();
            ExportadorXML exportador = new ExportadorXML();

            exportador.exportarPartidas(dao.listarTodas());

            System.out.println("Partidas exportadas correctamente.");

        } catch (Exception e) {
            System.out.println("No se pudieron exportar las partidas a XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
