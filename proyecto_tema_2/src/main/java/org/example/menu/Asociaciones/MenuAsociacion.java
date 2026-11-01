package org.example.menu.Asociaciones;

import org.example.dao.AsociacionDAO;
import org.example.model.Asociacion;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuAsociacion {

    public void mostrar() {
        Scanner sc = new Scanner(System.in);
        AsociacionDAO dao = new AsociacionDAO();
        int opcion = -1;

        do {
            System.out.println("\n--- MENU ASOCIACION ---");
            System.out.println("1. Consultar informacion");
            System.out.println("2. Modificar informacion");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());

                if (opcion == 1) {
                    Asociacion a = dao.obtener();

                    if (a == null) {
                        System.out.println("No hay datos de la asociacion.");
                    } else {
                        System.out.println("Nombre: " + a.getNombre());
                        System.out.println("CIF: " + a.getCif());
                        System.out.println("Direccion: " + a.getDireccion());
                        System.out.println("Telefono: " + a.getTelefono());
                        System.out.println("Email: " + a.getEmail());
                    }

                } else if (opcion == 2) {
                    Asociacion a = dao.obtener();

                    if (a == null) {
                        System.out.println("No hay datos de la asociacion.");
                    } else {
                        System.out.print("Nombre actual: " + a.getNombre() + ". Nuevo nombre: ");
                        String nombre = sc.nextLine();
                        if (!nombre.isBlank()) {
                            a.setNombre(nombre);
                        }

                        System.out.print("CIF actual: " + a.getCif() + ". Nuevo CIF: ");
                        String cif = sc.nextLine();
                        if (!cif.isBlank()) {
                            a.setCif(cif);
                        }

                        System.out.print("Direccion actual: " + a.getDireccion() + ". Nueva direccion: ");
                        String direccion = sc.nextLine();
                        if (!direccion.isBlank()) {
                            a.setDireccion(direccion);
                        }

                        System.out.print("Telefono actual: " + a.getTelefono() + ". Nuevo telefono: ");
                        String telefono = sc.nextLine();
                        if (!telefono.isBlank()) {
                            a.setTelefono(telefono);
                        }

                        System.out.print("Email actual: " + a.getEmail() + ". Nuevo email: ");
                        String email = sc.nextLine();
                        if (!email.isBlank()) {
                            a.setEmail(email);
                        }

                        if (a.getNombre() == null || a.getNombre().isBlank()
                                || a.getCif() == null || a.getCif().isBlank()
                                || a.getDireccion() == null || a.getDireccion().isBlank()) {
                            System.out.println("Nombre, CIF y direccion son obligatorios.");
                        } else if (dao.actualizar(a)) {
                            System.out.println("Datos modificados correctamente.");
                        } else {
                            System.out.println("No se pudieron modificar los datos.");
                        }
                    }

                } else if (opcion == 0) {
                    System.out.println("Volviendo al menu principal.");

                } else {
                    System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero valido.");
            } catch (SQLException e) {
                System.out.println("No se ha podido acceder o modificar la base de datos.");            }

        } while (opcion != 0);
    }
}