package org.example.dao.data;

import org.example.database.Conexion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetadatosDAO {

    public String obtenerMetadatosJSON()
            throws SQLException, JsonProcessingException {
        Map<String, Object> baseDatos = new LinkedHashMap<>();

        try (Connection conn = Conexion.conectar()) {

            DatabaseMetaData meta = conn.getMetaData();

            baseDatos.put("nombre", conn.getCatalog());
            baseDatos.put("driver", meta.getDriverName());
            baseDatos.put("versionDriver", meta.getDriverVersion());
            baseDatos.put("url", meta.getURL());
            baseDatos.put("versionServidor",
                    meta.getDatabaseProductVersion());

            List<Map<String, Object>> tablas = new ArrayList<>();

            try (ResultSet rsTablas = meta.getTables(
                    conn.getCatalog(),
                    null,
                    "%",
                    new String[]{"TABLE"})) {

                while (rsTablas.next()) {

                    String nombreTabla =
                            rsTablas.getString("TABLE_NAME");

                    Map<String, Object> tabla = new LinkedHashMap<>();

                    tabla.put("esquema",
                            rsTablas.getString("TABLE_SCHEM"));
                    tabla.put("nombre", nombreTabla);

                    List<Map<String, Object>> columnas =
                            new ArrayList<>();

                    try (ResultSet rsColumnas = meta.getColumns(
                            conn.getCatalog(),
                            null,
                            nombreTabla,
                            "%")) {

                        while (rsColumnas.next()) {

                            Map<String, Object> columna =
                                    new LinkedHashMap<>();

                            columna.put("nombre",
                                    rsColumnas.getString("COLUMN_NAME"));

                            columna.put("tipo",
                                    rsColumnas.getString("TYPE_NAME"));

                            columna.put("tamano",
                                    rsColumnas.getInt("COLUMN_SIZE"));

                            columna.put("admiteNulos",
                                    rsColumnas.getInt("NULLABLE")
                                            != DatabaseMetaData.columnNoNulls);

                            columnas.add(columna);
                        }
                    }

                    tabla.put("columnas", columnas);

                    List<String> clavesPrimarias = new ArrayList<>();

                    try (ResultSet rsClaves = meta.getPrimaryKeys(
                            conn.getCatalog(),
                            null,
                            nombreTabla)) {

                        while (rsClaves.next()) {
                            clavesPrimarias.add(
                                    rsClaves.getString("COLUMN_NAME")
                            );
                        }
                    }

                    tabla.put("clavesPrimarias", clavesPrimarias);

                    tablas.add(tabla);
                }
            }

            baseDatos.put("tablas", tablas);
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper.writeValueAsString(baseDatos);

    }
}