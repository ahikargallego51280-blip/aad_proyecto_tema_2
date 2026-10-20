
package org.example.export.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Socio;
import org.example.model.Juego;
import org.example.model.Partida;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportadorXML {

    private final XmlMapper mapper = new XmlMapper();

    public ExportadorXML() {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void exportarSocios(List<Socio> socios) throws IOException {
        File carpeta = new File("src/main/java/org/example/export/exportaciones");
        carpeta.mkdirs();

        File archivo = new File(carpeta, "socios.xml");

        mapper.writer()
                .withRootName("socios")
                .writeValue(archivo, socios);

        System.out.println("Socios exportados: " + archivo.getAbsolutePath());
    }

    public void exportarJuegos(List<Juego> juegos) throws IOException {
        File carpeta = new File("src/main/java/org/example/export/exportaciones");
        carpeta.mkdirs();

        File archivo = new File(carpeta, "juegos.xml");

        mapper.writer()
                .withRootName("juegos")
                .writeValue(archivo, juegos);

        System.out.println("Juegos exportados: " + archivo.getAbsolutePath());
    }

    public void exportarPartidas(List<Partida> partidas) throws IOException {
        File carpeta = new File("src/main/java/org/example/export/exportaciones");
        carpeta.mkdirs();

        File archivo = new File(carpeta, "partidas.xml");

        mapper.writer()
                .withRootName("partidas")
                .writeValue(archivo, partidas);

        System.out.println("Partidas exportadas: " + archivo.getAbsolutePath());
    }
}
