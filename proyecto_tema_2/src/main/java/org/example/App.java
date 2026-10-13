package org.example;

import org.example.dao.SociaDAO;
import org.example.database.Conexion;
import org.example.menu.MenuPrincipal;
import org.example.model.Socia;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.mostrar();
    }
}
