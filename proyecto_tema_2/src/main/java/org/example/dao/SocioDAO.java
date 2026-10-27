package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Socio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    public boolean insertar(Socio socio) throws SQLException {

        String sql = """
                INSERT INTO socio
                (documento, nombre, apellidos, telefono,
                 edad, email, activa)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, socio.getDocumento());
            ps.setString(2, socio.getNombre());
            ps.setString(3, socio.getApellidos());
            ps.setString(4, socio.getTelefono());
            ps.setInt(5, socio.getEdad());
            ps.setString(6, socio.getEmail());
            ps.setBoolean(7, socio.isActiva());

            return ps.executeUpdate() == 1;
        }
    }

    public List<Socio> listarTodas() throws SQLException {

        List<Socio> socios = new ArrayList<>();

        String sql = "SELECT * FROM socio ORDER BY apellidos, nombre";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Socio socio = new Socio(
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getInt("edad"),
                        rs.getString("email"),
                        rs.getBoolean("activa")
                );

                socios.add(socio);
            }
        }

        return socios;
    }

    public Socio buscarPorDocumento(String documento) throws SQLException {

        String sql = "SELECT * FROM socio WHERE documento = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearSocio(rs);
                }
            }
        }

        return null;
    }

    public boolean actualizar(Socio socio) throws SQLException {

        String sql = """
                UPDATE socio
                SET nombre = ?, apellidos = ?, telefono = ?,
                    edad = ?, email = ?, activa = ?
                WHERE documento = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, socio.getNombre());
            ps.setString(2, socio.getApellidos());
            ps.setString(3, socio.getTelefono());
            ps.setInt(4, socio.getEdad());
            ps.setString(5, socio.getEmail());
            ps.setBoolean(6, socio.isActiva());
            ps.setString(7, socio.getDocumento());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean darDeBaja(String documento) throws SQLException {

        String sql = """
                UPDATE socio
                SET activa = FALSE
                WHERE documento = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean existe(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM socio WHERE documento = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // BUSCADORES AVANZADOS

    public List<Socio> buscarPorNombre(String nombre) throws SQLException {
        List<Socio> socios = new ArrayList<>();

        String sql = """
                SELECT *
                FROM socio
                WHERE LOWER(nombre) LIKE LOWER(?)
                ORDER BY apellidos, nombre
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    socios.add(mapearSocio(rs));
                }
            }
        }

        return socios;
    }

    public List<Socio> buscarPorApellidos(String apellidos) throws SQLException {
        List<Socio> socios = new ArrayList<>();

        String sql = """
                SELECT *
                FROM socio
                WHERE LOWER(apellidos) LIKE LOWER(?)
                ORDER BY apellidos, nombre
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + apellidos.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    socios.add(mapearSocio(rs));
                }
            }
        }

        return socios;
    }

    private Socio mapearSocio(ResultSet rs) throws SQLException {
        return new Socio(
                rs.getString("documento"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getInt("edad"),
                rs.getString("email"),
                rs.getBoolean("activa")
        );
    }


}