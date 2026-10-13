package org.example.model;

public class Socia {

    private String documento;
    private String nombre;
    private String apellidos;
    private String telefono;
    private int edad;
    private String email;
    private boolean activa;

    public Socia(String documento, String nombre, String apellidos,
                 String telefono, int edad, String email, boolean activa) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.edad = edad;
        this.email = email;
        this.activa = activa;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEdad() {
        return edad;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Socia{" +
                "documento='" + documento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", edad=" + edad +
                ", activa=" + activa +
                '}';
    }
}