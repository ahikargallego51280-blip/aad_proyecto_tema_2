package org.example;

import org.example.menu.MenuPrincipal;
import org.example.seguridad.Acceso;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class App {

    public static void main(String[] args) {

        Acceso acceso = new Acceso();

        try {
            if (acceso.acceder()) {
                MenuPrincipal menu = new MenuPrincipal();
                menu.mostrar();
            } else {
                System.out.println("Acceso denegado.");
            }
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Error durante el inicio de sesión.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }
}