package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Asociacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsociacionDAO {

    public Asociacion obtener() throws SQLException {
        String sql = """
                SELECT *
                FROM asociacion
                ORDER BY id
                LIMIT 1
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Asociacion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cif"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        }

        return null;
    }

    public boolean insertar(Asociacion asociacion) throws SQLException {
        String sql = """
                INSERT INTO asociacion
                (nombre, cif, direccion, telefono, email)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, asociacion.getNombre());
            ps.setString(2, asociacion.getCif());
            ps.setString(3, asociacion.getDireccion());
            ps.setString(4, asociacion.getTelefono());
            ps.setString(5, asociacion.getEmail());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Asociacion asociacion) throws SQLException {
        String sql = """
                UPDATE asociacion
                SET nombre = ?,
                    cif = ?,
                    direccion = ?,
                    telefono = ?,
                    email = ?
                WHERE id = ?
                """;

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, asociacion.getNombre());
            ps.setString(2, asociacion.getCif());
            ps.setString(3, asociacion.getDireccion());
            ps.setString(4, asociacion.getTelefono());
            ps.setString(5, asociacion.getEmail());
            ps.setInt(6, asociacion.getId());

            return ps.executeUpdate() > 0;
        }
    }
}