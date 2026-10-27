package org.example;

import org.example.model.Socio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SocioTest {

    @Test
    public void comprobarDatosSocio() {
        Socio socio = new Socio(
                "12345678Z",
                "Laura",
                "Garcia",
                "600123123",
                25,
                "laura@gmail.com",
                true
        );

        assertEquals("12345678Z", socio.getDocumento());
        assertEquals("Laura", socio.getNombre());
        assertEquals("Garcia", socio.getApellidos());
        assertEquals("600123123", socio.getTelefono());
        assertEquals(25, socio.getEdad());
        assertEquals("laura@gmail.com", socio.getEmail());
        assertTrue(socio.isActiva());
    }

    @Test
    public void comprobarModificarSocio() {
        Socio socio = new Socio(
                "12345678Z",
                "Laura",
                "Garcia",
                "600123123",
                25,
                "laura@gmail.com",
                true
        );

        socio.setNombre("Maria");
        socio.setApellidos("Lopez");
        socio.setTelefono("611111111");
        socio.setEdad(30);
        socio.setEmail("maria@gmail.com");
        socio.setActiva(false);

        assertEquals("Maria", socio.getNombre());
        assertEquals("Lopez", socio.getApellidos());
        assertEquals("611111111", socio.getTelefono());
        assertEquals(30, socio.getEdad());
        assertEquals("maria@gmail.com", socio.getEmail());
        assertFalse(socio.isActiva());
    }
}

