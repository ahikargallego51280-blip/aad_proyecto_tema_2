package org.example;

import org.example.database.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try (Connection conexion = Conexion.conectar()) {
            System.out.println("¡Conexión correcta con MariaDB!");

            System.out.println(
                    "Base de datos: " +
                            conexion.getCatalog()
            );

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            e.printStackTrace();
        }
    }
}
