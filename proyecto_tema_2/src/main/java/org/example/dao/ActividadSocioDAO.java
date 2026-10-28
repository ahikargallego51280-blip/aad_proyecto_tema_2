
package org.example.dao;

import org.example.database.Conexion;
import org.example.model.ActividadSocio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActividadSocioDAO {

    public List<ActividadSocio> listarEntreFechas(
            String documentoSocio,
            LocalDate desde,
            LocalDate hasta) throws SQLException {
        if (documentoSocio == null || documentoSocio.isBlank()) {
            throw new IllegalArgumentException(
                    "El documento del socio es obligatorio."
            );
        }

        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias."
            );
        }

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }
        String sql = """
                SELECT pa.id_partida, pa.fecha, pa.hora,
                       j.nombre AS nombre_juego,
                       j.tipo AS tipo_juego,
                       j.duracion_min,
                       p.es_ganadora,
                       EXISTS (
                           SELECT 1
                           FROM participacion resultado
                           WHERE resultado.id_partida = pa.id_partida
                             AND resultado.es_ganadora = TRUE
                       ) AS resultado_registrado
                FROM participacion p
                INNER JOIN partida pa
                    ON pa.id_partida = p.id_partida
                INNER JOIN juego j
                    ON j.id_juego = pa.id_juego
                WHERE p.documento_socio = ?
                  AND pa.fecha BETWEEN ? AND ?
                  AND pa.activa = TRUE
                ORDER BY pa.fecha, pa.hora, pa.id_partida
                """;

        List<ActividadSocio> actividades = new ArrayList<>();

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documentoSocio);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actividades.add(new ActividadSocio(
                            rs.getInt("id_partida"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("nombre_juego"),
                            rs.getString("tipo_juego"),
                            rs.getInt("duracion_min"),
                            rs.getBoolean("es_ganadora"),
                            rs.getBoolean("resultado_registrado")
                    ));
                }
            }
        }

        return actividades;
    }
}
