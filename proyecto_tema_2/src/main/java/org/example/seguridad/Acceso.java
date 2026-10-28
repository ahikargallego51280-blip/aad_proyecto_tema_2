package org.example.seguridad;

import java.io.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Acceso {

    private static final Scanner sc = new Scanner(System.in);
    private static final String archivo =
            "src/main/java/org/example/seguridad/credenciales.dat";

    public boolean acceder()
            throws IOException, GeneralSecurityException {

        File fichero = new File(archivo);

        if (!fichero.exists()) {
            System.out.println("Crear usuario:");

            System.out.print("Usuario: ");
            String usuario = sc.nextLine().trim();

            System.out.print("Contraseña: ");
            String clave = sc.nextLine();

            if (usuario.isBlank() || clave.isBlank()) {
                System.out.println("El usuario y la contraseña no pueden estar vacíos.");
                return false;
            }

            guardar(usuario, clave);
            System.out.println("Usuario creado correctamente.");
        }

        System.out.println("\nIniciar sesion");

        System.out.print("Usuario: ");
        String usuario = sc.nextLine().trim();

        System.out.print("Contraseña: ");
        String clave = sc.nextLine();

        if (usuario.isBlank() || clave.isBlank()) {
            System.out.println("El usuario y la contraseña no pueden estar vacíos.");
            return false;
        }

        if (comprobar(usuario, clave)) {
            System.out.println("Acceso permitido.");
            registrarSesion(usuario);
            return true;
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
            return false;
        }
    }

    public static void guardar(String usuario, String clave)
            throws IOException, GeneralSecurityException {

        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);

        byte[] hash = generarHash(clave, sal);

        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(archivo))) {

            out.writeUTF(usuario);
            out.writeInt(sal.length);
            out.write(sal);
            out.writeInt(hash.length);
            out.write(hash);
        }
    }

    public static boolean comprobar(String usuario, String clave)
            throws IOException, GeneralSecurityException {

        String usuarioGuardado;
        byte[] sal;
        byte[] hashGuardado;

        try (DataInputStream in = new DataInputStream(
                new FileInputStream(archivo))) {

            usuarioGuardado = in.readUTF();

            int tamSal = in.readInt();
            sal = new byte[tamSal];
            in.readFully(sal);

            int tamHash = in.readInt();
            hashGuardado = new byte[tamHash];
            in.readFully(hashGuardado);
        }

        byte[] hashIntroducido = generarHash(clave, sal);

        return usuario.equals(usuarioGuardado)
                && MessageDigest.isEqual(hashGuardado, hashIntroducido);
    }

    public static byte[] generarHash(String clave, byte[] sal)
            throws GeneralSecurityException {

        KeySpec spec = new PBEKeySpec(
                clave.toCharArray(), sal, 65536, 256);

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return factory.generateSecret(spec).getEncoded();
    }

    public static void registrarSesion(String usuario)
            throws IOException {

        try (FileWriter fw = new FileWriter(
                "src/main/java/org/example/seguridad/sesiones.txt", true)) {

            String fecha = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            fw.write(usuario + " - " + fecha + "\n");
        }
    }
}