
package org.example.menu.JSON;

import org.example.dao.SocioDAO;
import org.example.dao.JuegoDAO;
import org.example.dao.PartidaDAO;
import org.example.dao.ActividadSocioDAO;
import org.example.dao.EstadisticasDAO;
import org.example.export.json.ExportadorJSON;
import org.example.model.ActividadSocio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public void exportarHistorial(String documento, LocalDate desde, LocalDate hasta) {
        try {
            ActividadSocioDAO dao = new ActividadSocioDAO();
            ExportadorJSON exportador = new ExportadorJSON();

            List<ActividadSocio> actividades =
                    dao.listarEntreFechas(documento, desde, hasta);

            exportador.exportarHistorial(documento, desde, hasta, actividades);

        } catch (Exception e) {
            System.out.println("No se pudo exportar el historial a JSON.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    public void exportarEstadisticas(LocalDate desde, LocalDate hasta) {
        try {
            EstadisticasDAO dao = new EstadisticasDAO();
            ExportadorJSON exportador = new ExportadorJSON();

            Map<String, Long> datos = dao.obtenerEstadisticas(desde, hasta);

            exportador.exportarEstadisticas(desde, hasta, datos);

        } catch (Exception e) {
            System.out.println("No se pudieron exportar las estadísticas a JSON.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}
