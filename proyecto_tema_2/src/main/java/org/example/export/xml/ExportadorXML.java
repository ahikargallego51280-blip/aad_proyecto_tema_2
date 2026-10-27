
package org.example.export.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Socio;
import org.example.model.Juego;
import org.example.model.Partida;
import org.example.model.ActividadSocio;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportadorXML {

    private final XmlMapper mapper = new XmlMapper();

    public ExportadorXML() {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private File crearArchivo(String nombre) throws IOException {
        File carpeta = new File("src/main/java/org/example/export/exportaciones");

        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta de exportaciones.");
        }

        return new File(carpeta, nombre);
    }

    public void exportarSocios(List<Socio> socios) throws IOException {
        File archivo = crearArchivo("socios.xml");

        mapper.writer()
                .withRootName("socios")
                .writeValue(archivo, socios);

        System.out.println("Socios exportados: " + archivo.getAbsolutePath());
    }

    public void exportarJuegos(List<Juego> juegos) throws IOException {
        File archivo = crearArchivo("juegos.xml");

        mapper.writer()
                .withRootName("juegos")
                .writeValue(archivo, juegos);

        System.out.println("Juegos exportados: " + archivo.getAbsolutePath());
    }

    public void exportarPartidas(List<Partida> partidas) throws IOException {
        File archivo = crearArchivo("partidas.xml");

        mapper.writer()
                .withRootName("partidas")
                .writeValue(archivo, partidas);

        System.out.println("Partidas exportadas: " + archivo.getAbsolutePath());
    }

    public void exportarHistorial(
            String documento,
            LocalDate desde,
            LocalDate hasta,
            List<ActividadSocio> actividades) throws IOException {

        Historial historial = new Historial();
        historial.documentoSocio = documento;
        historial.fechaDesde = desde.toString();
        historial.fechaHasta = hasta.toString();
        historial.actividades = actividades;

        File archivo = crearArchivo("historial.xml");

        mapper.writer()
                .withRootName("historial")
                .writeValue(archivo, historial);

        System.out.println("Historial exportado: " + archivo.getAbsolutePath());
    }

    public void exportarEstadisticas(
            LocalDate desde,
            LocalDate hasta,
            Map<String, Long> estadisticas) throws IOException {

        Estadisticas datos = new Estadisticas();
        datos.fechaDesde = desde.toString();
        datos.fechaHasta = hasta.toString();

        for (Map.Entry<String, Long> entrada : estadisticas.entrySet()) {
            DatoEstadistica dato = new DatoEstadistica();
            dato.nombre = entrada.getKey();
            dato.valor = entrada.getValue();
            datos.datos.add(dato);
        }

        File archivo = crearArchivo("estadisticas.xml");

        mapper.writer()
                .withRootName("estadisticas")
                .writeValue(archivo, datos);

        System.out.println("Estadisticas exportadas: "
                + archivo.getAbsolutePath());
    }

    public static class Historial {
        public String documentoSocio;
        public String fechaDesde;
        public String fechaHasta;

        @JacksonXmlElementWrapper(localName = "actividades")
        @JacksonXmlProperty(localName = "actividad")
        public List<ActividadSocio> actividades = new ArrayList<>();
    }

    public static class Estadisticas {
        public String fechaDesde;
        public String fechaHasta;

        @JacksonXmlElementWrapper(localName = "datos")
        @JacksonXmlProperty(localName = "estadistica")
        public List<DatoEstadistica> datos = new ArrayList<>();
    }

    public static class DatoEstadistica {
        public String nombre;
        public Long valor;
    }
}
