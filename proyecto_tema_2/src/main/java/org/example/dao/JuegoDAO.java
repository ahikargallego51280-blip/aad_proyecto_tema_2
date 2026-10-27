
package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Juego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JuegoDAO {

    public boolean insertar(Juego juego) throws SQLException {
        String sql = """
                INSERT INTO juego
                (nombre, autores, descripcion, jugadores_min,
                 jugadores_max, duracion_min, tematica, tipo,
                 ubicacion, estado, propietario_documento,
                 fecha_alta, activo)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, juego.getNombre());
            ps.setString(2, juego.getAutores());
            ps.setString(3, juego.getDescripcion());
            ps.setInt(4, juego.getJugadoresMin());
            ps.setInt(5, juego.getJugadoresMax());
            ps.setInt(6, juego.getDuracionMin());
            ps.setString(7, juego.getTematica());
            ps.setString(8, juego.getTipo());
            ps.setString(9, juego.getUbicacion());
            ps.setString(10, juego.getEstado());
            ps.setString(11, juego.getPropietarioDocumento());
            ps.setString(12, juego.getFechaAlta());
            ps.setBoolean(13, juego.isActivo());

            int filas = ps.executeUpdate();

            if (filas == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        System.out.println(
                                "🆔 ID del juego registrado: " + rs.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    public List<Juego> listarTodos() throws SQLException {
        List<Juego> juegos = new ArrayList<>();

        String sql = "SELECT * FROM juego ORDER BY nombre";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                juegos.add(mapearJuego(rs));
            }
        }

        return juegos;
    }

    public List<Juego> listarPorPropietario(String documento)
            throws SQLException {

        List<Juego> juegos = new ArrayList<>();

        String sql = """
        SELECT *
        FROM juego
        WHERE propietario_documento = ?
        ORDER BY nombre
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    juegos.add(mapearJuego(rs));
                }
            }
        }

        return juegos;
    }

    public Juego buscarPorId(int idJuego) throws SQLException {
        String sql = "SELECT * FROM juego WHERE id_juego = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idJuego);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearJuego(rs);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Juego juego) throws SQLException {
        String sql = """
                UPDATE juego
                SET nombre = ?, autores = ?, descripcion = ?,
                    jugadores_min = ?, jugadores_max = ?,
                    duracion_min = ?, tematica = ?, tipo = ?,
                    ubicacion = ?, estado = ?,
                    propietario_documento = ?, fecha_alta = ?,
                    activo = ?
                WHERE id_juego = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, juego.getNombre());
            ps.setString(2, juego.getAutores());
            ps.setString(3, juego.getDescripcion());
            ps.setInt(4, juego.getJugadoresMin());
            ps.setInt(5, juego.getJugadoresMax());
            ps.setInt(6, juego.getDuracionMin());
            ps.setString(7, juego.getTematica());
            ps.setString(8, juego.getTipo());
            ps.setString(9, juego.getUbicacion());
            ps.setString(10, juego.getEstado());
            ps.setString(11, juego.getPropietarioDocumento());
            ps.setString(12, juego.getFechaAlta());
            ps.setBoolean(13, juego.isActivo());
            ps.setInt(14, juego.getIdJuego());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean darDeBaja(int idJuego) throws SQLException {
        String sql = """
                UPDATE juego
                SET activo = FALSE
                WHERE id_juego = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idJuego);

            return ps.executeUpdate() == 1;
        }
    }

    private Juego mapearJuego(ResultSet rs) throws SQLException {
        return new Juego(
                rs.getInt("id_juego"),
                rs.getString("nombre"),
                rs.getString("autores"),
                rs.getString("descripcion"),
                rs.getInt("jugadores_min"),
                rs.getInt("jugadores_max"),
                rs.getInt("duracion_min"),
                rs.getString("tematica"),
                rs.getString("tipo"),
                rs.getString("ubicacion"),
                rs.getString("estado"),
                rs.getString("propietario_documento"),
                rs.getString("fecha_alta"),
                rs.getBoolean("activo")
        );
    }

    // BUSCADORES AVANZADOS
    public List<Juego> buscarPorNombre(String nombre) throws SQLException {
        List<Juego> juegos = new ArrayList<>();

        String sql = """
            SELECT *
            FROM juego
            WHERE LOWER(nombre) LIKE LOWER(?)
            ORDER BY nombre
            """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    juegos.add(mapearJuego(rs));
                }
            }
        }

        return juegos;
    }

    public List<Juego> buscarPorTematica(String tematica) throws SQLException {
        List<Juego> juegos = new ArrayList<>();

        String sql = """
            SELECT *
            FROM juego
            WHERE LOWER(tematica) LIKE LOWER(?)
            ORDER BY nombre
            """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + tematica.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    juegos.add(mapearJuego(rs));
                }
            }
        }

        return juegos;
    }

    public List<Juego> buscarPorNumeroJugadores(int numeroJugadores)
            throws SQLException {

        List<Juego> juegos = new ArrayList<>();

        String sql = """
            SELECT *
            FROM juego
            WHERE jugadores_min <= ?
              AND jugadores_max >= ?
            ORDER BY nombre
            """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroJugadores);
            ps.setInt(2, numeroJugadores);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    juegos.add(mapearJuego(rs));
                }
            }
        }

        return juegos;
    }

}
