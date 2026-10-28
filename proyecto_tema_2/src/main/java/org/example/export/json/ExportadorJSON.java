
package org.example.export.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Socio;
import org.example.model.Juego;
import org.example.model.Partida;
import org.example.model.ActividadSocio;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExportadorJSON {

    private final ObjectMapper mapper = new ObjectMapper();

    public ExportadorJSON() {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private File crearArchivo(String nombre) throws IOException {
        File directorio = new File("exportaciones");

        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException(
                    "No se pudo crear la carpeta exportaciones."
            );
        }

        if (!directorio.isDirectory()) {
            throw new IOException(
                    "La ruta de exportaciones no es una carpeta."
            );
        }

        return new File(directorio, nombre);
    }

    public void exportarSocios(List<Socio> socios) throws IOException {
        if (socios == null) {
            throw new IllegalArgumentException(
                    "La lista de socios no puede ser nula."
            );
        }

        File archivo = crearArchivo("socios.json");
        mapper.writeValue(archivo, socios);

        System.out.println(
                "Socios exportados: " + archivo.getAbsolutePath()
        );
    }

    public void exportarJuegos(List<Juego> juegos) throws IOException {
        if (juegos == null) {
            throw new IllegalArgumentException(
                    "La lista de juegos no puede ser nula."
            );
        }

        File archivo = crearArchivo("juegos.json");
        mapper.writeValue(archivo, juegos);

        System.out.println(
                "Juegos exportados: " + archivo.getAbsolutePath()
        );
    }

    public void exportarPartidas(List<Partida> partidas)
            throws IOException {

        if (partidas == null) {
            throw new IllegalArgumentException(
                    "La lista de partidas no puede ser nula."
            );
        }

        File archivo = crearArchivo("partidas.json");
        mapper.writeValue(archivo, partidas);

        System.out.println(
                "Partidas exportadas: " + archivo.getAbsolutePath()
        );
    }

    public void exportarHistorial(
            String documento,
            LocalDate desde,
            LocalDate hasta,
            List<ActividadSocio> actividades) throws IOException {

        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException(
                    "El documento del socio es obligatorio."
            );
        }

        validarFechas(desde, hasta);

        if (actividades == null) {
            throw new IllegalArgumentException(
                    "La lista de actividades no puede ser nula."
            );
        }

        Map<String, Object> datos = new java.util.LinkedHashMap<>();

        datos.put("documentoSocio", documento);
        datos.put("fechaDesde", desde);
        datos.put("fechaHasta", hasta);
        datos.put("actividades", actividades);

        File archivo = crearArchivo("historial.json");
        mapper.writeValue(archivo, datos);

        System.out.println(
                "Historial exportado: " + archivo.getAbsolutePath()
        );
    }

    public void exportarEstadisticas(
            LocalDate desde,
            LocalDate hasta,
            Map<String, Long> estadisticas) throws IOException {

        validarFechas(desde, hasta);

        if (estadisticas == null) {
            throw new IllegalArgumentException(
                    "Las estadísticas no pueden ser nulas."
            );
        }

        Map<String, Object> datos = new java.util.LinkedHashMap<>();

        datos.put("fechaDesde", desde);
        datos.put("fechaHasta", hasta);
        datos.put("estadisticas", estadisticas);

        File archivo = crearArchivo("estadisticas.json");
        mapper.writeValue(archivo, datos);

        System.out.println(
                "Estadísticas exportadas: " + archivo.getAbsolutePath()
        );
    }

    private void validarFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias."
            );
        }

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la final."
            );
        }
    }
}
