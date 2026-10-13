package org.example.dao;

import org.example.database.Conexion;
import org.example.model.Socia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SociaDAO {

    // Insertar Socia
    public boolean insertar(Socia socia) throws SQLException {

        String sql = """
                INSERT INTO socia
                (documento, nombre, apellidos, telefono,
                 edad, email, activa)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, socia.getDocumento());
            ps.setString(2, socia.getNombre());
            ps.setString(3, socia.getApellidos());
            ps.setString(4, socia.getTelefono());
            ps.setInt(5, socia.getEdad());
            ps.setString(6, socia.getEmail());
            ps.setBoolean(7, socia.isActiva());

            return ps.executeUpdate() == 1;
        }
    }

    // Consultar Socias
    public List<Socia> listarTodas() throws SQLException {

        List<Socia> socias = new ArrayList<>();

        String sql = "SELECT * FROM socia ORDER BY apellidos, nombre";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Socia socia = new Socia(
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getInt("edad"),
                        rs.getString("email"),
                        rs.getBoolean("activa")
                );

                socias.add(socia);
            }
        }

        return socias;
    }

    // Modificar datos de socia
    public boolean actualizar(Socia socia) throws SQLException {

        String sql = """
                UPDATE socia
                SET nombre = ?, apellidos = ?, telefono = ?,
                    edad = ?, email = ?, activa = ?
                WHERE documento = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, socia.getNombre());
            ps.setString(2, socia.getApellidos());
            ps.setString(3, socia.getTelefono());
            ps.setInt(4, socia.getEdad());
            ps.setString(5, socia.getEmail());
            ps.setBoolean(6, socia.isActiva());
            ps.setString(7, socia.getDocumento());

            return ps.executeUpdate() == 1;
        }
    }

    // Dar de baja una socia sin borrar su historial
    public boolean darDeBaja(String documento) throws SQLException {

        String sql = """
                UPDATE socia
                SET activa = FALSE
                WHERE documento = ?
                """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);

            return ps.executeUpdate() == 1;
        }
    }
}