
package org.example.menu.JSON;

import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.export.json.ExportadorJSON;

public class UtilidadesJSON {

    public void exportarSocios() {
        try {
            SocioDAO dao = new SocioDAO();
            ExportadorJSON exportador = new ExportadorJSON();

            exportador.exportarSocios(dao.listarTodas());

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los socios.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    public void exportarJuegos() {
        try {
            JuegoDAO dao = new JuegoDAO();
            ExportadorJSON exportador = new ExportadorJSON();

            exportador.exportarJuegos(dao.listarTodos());

        } catch (Exception e) {
            System.out.println("No se pudieron exportar los juegos.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    public void exportarPartidas() {
        try {
            PartidaDAO dao = new PartidaDAO();
            ExportadorJSON exportador = new ExportadorJSON();

            exportador.exportarPartidas(dao.listarTodas());

        } catch (Exception e) {
            System.out.println("No se pudieron exportar las partidas.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
