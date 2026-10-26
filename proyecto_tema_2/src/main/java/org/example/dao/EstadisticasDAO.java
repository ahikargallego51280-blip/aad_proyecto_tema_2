package org.example.dao;

import org.example.database.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadisticasDAO {

    public Map<String, Long> obtenerEstadisticas(
            LocalDate desde, LocalDate hasta) throws SQLException {

        Map<String, Long> datos = new LinkedHashMap<>();

        try (Connection conn = Conexion.conectar()) {

            // SOCIOS: estado actual
            datos.put("Total de socios",
                    contar(conn, "SELECT COUNT(*) FROM socio"));

            datos.put("Socios activos",
                    contar(conn, "SELECT COUNT(*) FROM socio WHERE activa = TRUE"));

            datos.put("Socios inactivos",
                    contar(conn, "SELECT COUNT(*) FROM socio WHERE activa = FALSE"));

            // JUEGOS
            datos.put("Total de juegos",
                    contar(conn, "SELECT COUNT(*) FROM juego"));

            datos.put("Juegos dados de alta en el periodo",
                    contarPeriodo(conn,
                            "SELECT COUNT(*) FROM juego WHERE fecha_alta BETWEEN ? AND ?",
                            desde, hasta));

            datos.put("Juegos activos",
                    contar(conn, "SELECT COUNT(*) FROM juego WHERE activo = TRUE"));

            datos.put("Juegos disponibles",
                    contar(conn, "SELECT COUNT(*) FROM juego WHERE estado = 'DISPONIBLE'"));

            datos.put("Juegos prestados",
                    contar(conn, "SELECT COUNT(*) FROM juego WHERE estado = 'PRESTADO'"));

            datos.put("Juegos en reparación",
                    contar(conn, "SELECT COUNT(*) FROM juego WHERE estado = 'REPARACION'"));

            // PARTIDAS: dentro del periodo
            datos.put("Partidas del periodo",
                    contarPeriodo(conn,
                            "SELECT COUNT(*) FROM partida WHERE fecha BETWEEN ? AND ?",
                            desde, hasta));

            datos.put("Partidas activas del periodo",
                    contarPeriodo(conn,
                            "SELECT COUNT(*) FROM partida WHERE activa = TRUE AND fecha BETWEEN ? AND ?",
                            desde, hasta));

            datos.put("Partidas inactivas del periodo",
                    contarPeriodo(conn,
                            "SELECT COUNT(*) FROM partida WHERE activa = FALSE AND fecha BETWEEN ? AND ?",
                            desde, hasta));

            // PARTICIPACIONES: partidas dentro del periodo
            String participaciones =
                    " FROM participacion pa " +
                            "JOIN partida p ON p.id_partida = pa.id_partida " +
                            "WHERE p.fecha BETWEEN ? AND ?";

            datos.put("Participaciones del periodo",
                    contarPeriodo(conn,
                            "SELECT COUNT(*)" + participaciones,
                            desde, hasta));

            datos.put("Socios participantes distintos",
                    contarPeriodo(conn,
                            "SELECT COUNT(DISTINCT pa.documento_socio)" + participaciones +
                                    " AND pa.documento_socio IS NOT NULL",
                            desde, hasta));

            datos.put("Participaciones de invitados",
                    contarPeriodo(conn,
                            "SELECT COUNT(*)" + participaciones +
                                    " AND pa.nombre_invitada IS NOT NULL",
                            desde, hasta));

            datos.put("Participaciones marcadas como ganadoras",
                    contarPeriodo(conn,
                            "SELECT COUNT(*)" + participaciones +
                                    " AND pa.es_ganadora = TRUE",
                            desde, hasta));
        }

        return datos;
    }

    private long contar(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getLong(1);
        }
    }

    private long contarPeriodo(
            Connection conn, String sql,
            LocalDate desde, LocalDate hasta) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }
}