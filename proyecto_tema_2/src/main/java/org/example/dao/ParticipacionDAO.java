
package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Participacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipacionDAO {

    private boolean partidaActiva(int idPartida) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM partida
                WHERE id_partida = ? AND activa = TRUE
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean inscribirSocio(int idPartida, String documento)
            throws SQLException {

        if (documento == null || documento.isBlank()) {
            return false;
        }

        if (!partidaActiva(idPartida)) {
            return false;
        }

        String sql = """
                INSERT INTO participacion
                (id_partida, documento_socio, nombre_invitada)
                VALUES (?, ?, NULL)
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setString(2, documento.trim());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean inscribirInvitado(int idPartida, String nombre)
            throws SQLException {

        if (nombre == null || nombre.isBlank()) {
            return false;
        }

        if (!partidaActiva(idPartida)) {
            return false;
        }

        String sql = """
                INSERT INTO participacion
                (id_partida, documento_socio, nombre_invitada)
                VALUES (?, NULL, ?)
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setString(2, nombre.trim());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Participacion> listarPorPartida(int idPartida)
            throws SQLException {

        String sql = """
                SELECT *
                FROM participacion
                WHERE id_partida = ?
                ORDER BY id_participacion
                """;

        List<Participacion> participantes = new ArrayList<>();

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participantes.add(new Participacion(
                            rs.getInt("id_participacion"),
                            rs.getInt("id_partida"),
                            rs.getString("documento_socio"),
                            rs.getString("nombre_invitada"),
                            rs.getBoolean("es_ganadora")
                    ));
                }
            }
        }

        return participantes;
    }

    public boolean registrarGanadora(int idParticipacion, boolean ganadora)
            throws SQLException {

        String sql = """
                UPDATE participacion
                SET es_ganadora = ?
                WHERE id_participacion = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ganadora);
            ps.setInt(2, idParticipacion);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean retirarParticipante(int idParticipacion)
            throws SQLException {

        String sql = """
                DELETE FROM participacion
                WHERE id_participacion = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idParticipacion);

            return ps.executeUpdate() > 0;
        }
    }

    public int contarParticipantes(int idPartida) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM participacion
                WHERE id_partida = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPartida);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
