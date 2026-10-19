package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Partida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {

    public boolean insertar(Partida partida) throws SQLException {
        String sql = """
                INSERT INTO partida
                (id_juego, fecha, hora, jugadores_deseados, activa)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, partida.getIdJuego());
            ps.setDate(2, Date.valueOf(partida.getFecha()));
            ps.setTime(3, Time.valueOf(partida.getHora()));
            ps.setInt(4, partida.getJugadoresDeseados());
            ps.setBoolean(5, partida.isActiva());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Partida> listarTodas() throws SQLException {
        String sql = "SELECT * FROM partida ORDER BY fecha, hora";

        List<Partida> partidas = new ArrayList<>();

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                partidas.add(mapearPartida(rs));
            }
        }

        return partidas;
    }

    public Partida buscarPorId(int idPartida) throws SQLException {
        String sql = "SELECT * FROM partida WHERE id_partida = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPartida(rs);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Partida partida) throws SQLException {
        String sql = """
                UPDATE partida
                SET id_juego = ?,
                    fecha = ?,
                    hora = ?,
                    jugadores_deseados = ?,
                    activa = ?
                WHERE id_partida = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, partida.getIdJuego());
            ps.setDate(2, Date.valueOf(partida.getFecha()));
            ps.setTime(3, Time.valueOf(partida.getHora()));
            ps.setInt(4, partida.getJugadoresDeseados());
            ps.setBoolean(5, partida.isActiva());
            ps.setInt(6, partida.getIdPartida());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean cancelar(int idPartida) throws SQLException {
        String sql = """
                UPDATE partida
                SET activa = FALSE
                WHERE id_partida = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);

            return ps.executeUpdate() > 0;
        }
    }

    private Partida mapearPartida(ResultSet rs) throws SQLException {
        return new Partida(
                rs.getInt("id_partida"),
                rs.getInt("id_juego"),
                rs.getDate("fecha").toLocalDate(),
                rs.getTime("hora").toLocalTime(),
                rs.getInt("jugadores_deseados"),
                rs.getBoolean("activa")
        );
    }
}