package org.example.export.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Socio;
import org.example.model.Juego;
import org.example.model.Partida;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportadorJSON {

    private final ObjectMapper mapper = new ObjectMapper();

    public ExportadorJSON() {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private File crearArchivo(String nombre) throws IOException {
        File directorio = new File("exportaciones");

        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta exportaciones.");
        }

        return new File(directorio, nombre);
    }

    public void exportarSocios(List<Socio> socios) throws IOException {
        File archivo = crearArchivo("socios.json");
        mapper.writeValue(archivo, socios);
        System.out.println("Socios exportadas: " + archivo.getAbsolutePath());
    }

    public void exportarJuegos(List<Juego> juegos) throws IOException {
        File archivo = crearArchivo("juegos.json");
        mapper.writeValue(archivo, juegos);
        System.out.println("Juegos exportados: " + archivo.getAbsolutePath());
    }

    public void exportarPartidas(List<Partida> partidas) throws IOException {
        File archivo = crearArchivo("partidas.json");
        mapper.writeValue(archivo, partidas);
        System.out.println("Partidas exportadas: " + archivo.getAbsolutePath());
    }
}