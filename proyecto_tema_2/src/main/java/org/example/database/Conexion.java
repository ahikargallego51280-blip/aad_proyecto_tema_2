package org.example.database;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    private static final String URL = obtenerVariable(
            "DB_URL",

            "jdbc:mariadb://localhost:3306/asociacion_juegos"
    );
    private static final String USUARIO = obtenerVariable("DB_USER", "root");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD", "");

    private static String obtenerVariable(
            String nombre,
            String valorPorDefecto) {
        String valor = dotenv.get(nombre);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(nombre);
        }
        return valor != null ? valor : valorPorDefecto;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}