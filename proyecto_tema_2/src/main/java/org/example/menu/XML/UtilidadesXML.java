
package org.example.menu.XML;

import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.dao.ActividadSocioDAO;
import org.example.dao.EstadisticasDAO;
import org.example.export.xml.ExportadorXML;
import org.example.model.ActividadSocio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UtilidadesXML {

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

    public void exportarHistorial(String documento, LocalDate desde, LocalDate hasta) {
        try {
            ActividadSocioDAO dao = new ActividadSocioDAO();
            ExportadorXML exportador = new ExportadorXML();

            List<ActividadSocio> actividades =
                    dao.listarEntreFechas(documento, desde, hasta);

            exportador.exportarHistorial(documento, desde, hasta, actividades);

            System.out.println("Historial exportado correctamente a XML.");

        } catch (Exception e) {
            System.out.println("No se pudo exportar el historial a XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    public void exportarEstadisticas(LocalDate desde, LocalDate hasta) {
        try {
            EstadisticasDAO dao = new EstadisticasDAO();
            ExportadorXML exportador = new ExportadorXML();

            Map<String, Long> datos = dao.obtenerEstadisticas(desde, hasta);

            exportador.exportarEstadisticas(desde, hasta, datos);

            System.out.println("Estadísticas exportadas correctamente a XML.");

        } catch (Exception e) {
            System.out.println("No se pudieron exportar las estadísticas a XML.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
