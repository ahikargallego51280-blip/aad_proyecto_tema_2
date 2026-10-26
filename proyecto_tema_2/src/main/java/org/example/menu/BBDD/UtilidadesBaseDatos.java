
package org.example.menu.BBDD;

import org.example.dao.data.ExportadorMetadatos;
import org.example.database.Conexion;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilidadesBaseDatos {

    // Muestra info sobre conexion con bd
    public void mostrarConexion() throws SQLException {
        try (Connection conn = Conexion.conectar()) {
            DatabaseMetaData meta = conn.getMetaData();

            System.out.println("\nINFORMACIÓN DE CONEXIÓN");
            System.out.println("Base de datos: " + conn.getCatalog());
            System.out.println("Controlador: " + meta.getDriverName());
            System.out.println(
                    "Versión del controlador: " + meta.getDriverVersion()
            );
            System.out.println("URL JDBC: " + meta.getURL());
            System.out.println("Usuario: " + meta.getUserName());
            System.out.println(
                    "Versión del servidor: "
                            + meta.getDatabaseProductVersion()
            );
        }
    }

    // Obtener nombres de tablas que existen en BD
    public void listarTablas() throws SQLException {
        try (Connection conn = Conexion.conectar()) {
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet rs = meta.getTables(
                    conn.getCatalog(),
                    null,
                    "%",
                    new String[]{"TABLE"})) {

                System.out.println("\nTABLAS DE LA BASE DE DATOS");

                boolean hayTablas = false;

                while (rs.next()) {
                    hayTablas = true;
                    System.out.println(
                            "- " + rs.getString("TABLE_NAME")
                    );
                }

                if (!hayTablas) {
                    System.out.println("No se encontraron tablas.");
                }
            }
        }
    }

    // Genera un archivo JSON con metadatos de BD y captura errores que puedan ocurrir durante la exportacion
    public void exportarMetadatos() {
        try {
            ExportadorMetadatos exportador = new ExportadorMetadatos();
            exportador.exportarJSON();

        } catch (Exception e) {
            System.out.println(
                    "Error al exportar los metadatos: " + e.getMessage()
            );
        }
    }

    // Mostrar estructura de una tabla concreta
    public void mostrarEstructura(String tabla)
            throws SQLException {

        if (tabla == null || tabla.trim().isEmpty()) {
            System.out.println(
                    "Debes introducir un nombre de tabla."
            );
            return;
        }

        try (Connection conn = Conexion.conectar()) {
            DatabaseMetaData meta = conn.getMetaData();
            String catalogo = conn.getCatalog();

            String nombreTabla = null;

            try (ResultSet tablas = meta.getTables(
                    catalogo,
                    null,
                    "%",
                    new String[]{"TABLE"})) {

                while (tablas.next()) {
                    String nombreActual =
                            tablas.getString("TABLE_NAME");

                    if (tabla.trim().equalsIgnoreCase(nombreActual)) {
                        nombreTabla = nombreActual;
                        break;
                    }
                }
            }

            if (nombreTabla == null) {
                System.out.println("No existe esa tabla.");
                return;
            }

            System.out.println(
                    "\nESTRUCTURA DE " + nombreTabla
            );

            try (ResultSet columnas = meta.getColumns(
                    catalogo,
                    null,
                    nombreTabla,
                    "%")) {

                while (columnas.next()) {
                    String nombre =
                            columnas.getString("COLUMN_NAME");

                    String tipo =
                            columnas.getString("TYPE_NAME");

                    int tamano =
                            columnas.getInt("COLUMN_SIZE");

                    boolean admiteNulos =
                            columnas.getInt("NULLABLE")
                                    == DatabaseMetaData.columnNullable;

                    System.out.println(
                            nombre
                                    + " | Tipo: " + tipo
                                    + " | Tamaño: " + tamano
                                    + " | Admite nulos: "
                                    + (admiteNulos ? "Sí" : "No")
                    );
                }
            }

            System.out.println("\nCLAVES PRIMARIAS");

            try (ResultSet claves = meta.getPrimaryKeys(
                    catalogo,
                    null,
                    nombreTabla)) {

                boolean tieneClave = false;

                while (claves.next()) {
                    tieneClave = true;
                    System.out.println(
                            "- " + claves.getString("COLUMN_NAME")
                    );
                }

                if (!tieneClave) {
                    System.out.println(
                            "No se encontraron claves primarias."
                    );
                }
            }
        }
    }
}
