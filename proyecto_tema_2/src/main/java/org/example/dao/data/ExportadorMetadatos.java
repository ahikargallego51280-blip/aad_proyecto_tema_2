package org.example.dao.data;

import org.example.dao.data.MetadatosDAO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ExportadorMetadatos {

    public boolean exportarJSON() throws SQLException, IOException {

        MetadatosDAO dao = new MetadatosDAO();

        String json = dao.obtenerMetadatosJSON();

        Path carpeta = Path.of("src/main/java/org/example/export/exportaciones");
        Files.createDirectories(carpeta);

        Path archivo = carpeta.resolve("metadatos.json");

        Files.writeString(
                archivo,
                json,
                StandardCharsets.UTF_8
        );

        System.out.println(
                "\n!! Metadatos exportados correctamente !!"
        );

        return true;
    }
}