
package org.example.menu;

import org.example.database.Conexion;
import org.example.dao.data.ExportadorMetadatos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuBaseDatos {

    private final Scanner sc = new Scanner(System.in);

    public void mostrar() {
        int opcion;

        do {
            System.out.println("\nMENÚ BASE DE DATOS");
            System.out.println("1. Información de conexión");
            System.out.println("2. Listar tablas");
            System.out.println("3. Consultar estructura de una tabla");
            System.out.println("4. Exportar metadatos a JSON");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> mostrarConexion();
                    case 2 -> listarTablas();
                    case 3 -> mostrarEstructura();
                    case 4 -> exportarMetadatos();
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    // Exportar metadatos a JSON
    private void exportarMetadatos() {
        try {
            ExportadorMetadatos exportador = new ExportadorMetadatos();
            exportador.exportarJSON();
            System.out.println("Metadatos exportados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al exportar los metadatos: " + e.getMessage());
        }
    }

    // Mostrar información de la conexión
    private void mostrarConexion() throws SQLException {
        try (Connection conn = Conexion.conectar()) {
            DatabaseMetaData meta = conn.getMetaData();

            System.out.println("\nINFORMACIÓN DE CONEXIÓN");
            System.out.println("Base de datos: " + conn.getCatalog());
            System.out.println("Controlador: " + meta.getDriverName());
            System.out.println("Versión del controlador: " + meta.getDriverVersion());
            System.out.println("URL JDBC: " + meta.getURL());
            System.out.println("Usuario: " + meta.getUserName());
            System.out.println("Versión del servidor: " + meta.getDatabaseProductVersion());
        }
    }

    // Listar las tablas
    private void listarTablas() throws SQLException {
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
                    System.out.println("- " + rs.getString("TABLE_NAME"));
                }

                if (!hayTablas) {
                    System.out.println("No se encontraron tablas.");
                }
            }
        }
    }

    // Mostrar columnas y claves primarias de una tabla
    private void mostrarEstructura() throws SQLException {
        System.out.print("Nombre de la tabla: ");
        String tabla = sc.nextLine().trim();

        if (tabla.isEmpty()) {
            System.out.println("Debes introducir un nombre de tabla.");
            return;
        }

        try (Connection conn = Conexion.conectar()) {
            DatabaseMetaData meta = conn.getMetaData();
            String catalogo = conn.getCatalog();

            boolean existe = false;

            try (ResultSet tablas = meta.getTables(
                    catalogo, null, "%", new String[]{"TABLE"})) {

                while (tablas.next()) {
                    if (tabla.equalsIgnoreCase(tablas.getString("TABLE_NAME"))) {
                        tabla = tablas.getString("TABLE_NAME");
                        existe = true;
                        break;
                    }
                }
            }

            if (!existe) {
                System.out.println("No existe esa tabla.");
                return;
            }

            System.out.println("\nESTRUCTURA DE " + tabla);

            try (ResultSet columnas = meta.getColumns(
                    catalogo, null, tabla, "%")) {

                while (columnas.next()) {
                    String nombre = columnas.getString("COLUMN_NAME");
                    String tipo = columnas.getString("TYPE_NAME");
                    int tamano = columnas.getInt("COLUMN_SIZE");
                    boolean admiteNulos = columnas.getInt("NULLABLE")
                            == DatabaseMetaData.columnNullable;

                    System.out.println(
                            nombre + " | Tipo: " + tipo
                                    + " | Tamaño: " + tamano
                                    + " | Admite nulos: " + (admiteNulos ? "Sí" : "No")
                    );
                }
            }

            System.out.println("\nCLAVES PRIMARIAS");

            try (ResultSet claves = meta.getPrimaryKeys(
                    catalogo, null, tabla)) {

                boolean tieneClave = false;

                while (claves.next()) {
                    tieneClave = true;
                    System.out.println("- " + claves.getString("COLUMN_NAME"));
                }

                if (!tieneClave) {
                    System.out.println("No se encontraron claves primarias.");
                }
            }
        }
    }

    // Leer una opción numérica
    private int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número válido: ");
            }
        }
    }
}
