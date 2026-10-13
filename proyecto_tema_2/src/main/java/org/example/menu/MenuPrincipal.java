
package org.example.menu;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);

    public void mostrar() {
        int opcion;

        do {
            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║       🎲 ASOCIACIÓN DE JUEGOS        ║");
            System.out.println("║          Sistema de gestión          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println();
            System.out.println("  👥 1. Gestión de socias");
            System.out.println("  🎮 2. Gestión de juegos");
            System.out.println("  📅 3. Gestión de partidas");
            System.out.println("  🏆 4. Gestión de participantes");
            System.out.println("  📦 5. Exportar datos (JSON/XML)");
            System.out.println("  🗄️  6. Información de la base de datos");
            System.out.println("  ℹ️  7. Información de la asociación");
            System.out.println("  🚪 0. Salir");
            System.out.println();
            System.out.print("👉 Selecciona una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuSocias();
                case 2 -> menuJuegos();
                case 3 -> menuPartidas();
                case 4 -> menuParticipantes();
                case 5 -> menuExportaciones();
                case 6 -> mostrarBaseDatos();
                case 7 -> mostrarInformacion();
                case 0 -> despedida();
                default -> System.out.println("❌ Opción no válida.");
            }

        } while (opcion != 0);
    }

    private void menuSocias() {
        MenuSocias menu = new MenuSocias();
        menu.mostrar();
    }


    private void menuJuegos() {
        MenuJuegos menu = new MenuJuegos();
        menu.mostrar();
    }


    private void menuPartidas() {
        mostrarSubmenu("📅 GESTIÓN DE PARTIDAS",
                new String[]{
                        "➕ Crear partida",
                        "📋 Listar partidas",
                        "✏️ Modificar partida",
                        "🔴 Cancelar partida"
                });
    }

    private void menuParticipantes() {
        mostrarSubmenu("🏆 GESTIÓN DE PARTICIPANTES",
                new String[]{
                        "➕ Añadir participante",
                        "📋 Ver participantes",
                        "🏅 Registrar ganadora",
                        "➖ Retirar participante"
                });
    }

    private void menuExportaciones() {
        mostrarSubmenu("📦 EXPORTACIÓN DE DATOS",
                new String[]{
                        "📄 Exportar socias a JSON",
                        "📄 Exportar juegos a JSON",
                        "📄 Exportar partidas a JSON",
                        "📑 Exportar datos a XML"
                });
    }

    private void mostrarSubmenu(String titulo, String[] opciones) {
        int opcion;

        do {
            System.out.println("\n" + titulo);
            System.out.println("────────────────────────────────────");

            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }

            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("👉 Opción: ");

            opcion = leerEntero();

            if (opcion == 0) {
                System.out.println("↩️ Volviendo al menú principal...");
            } else if (opcion >= 1 && opcion <= opciones.length) {
                System.out.println(
                        "🛠️ Esta función se conectará en los siguientes pasos."
                );
            } else {
                System.out.println("❌ Opción no válida.");
            }

        } while (opcion != 0);
    }

    private void mostrarBaseDatos() {
        System.out.println("\n🗄️ ===== BASE DE DATOS =====");
        System.out.println("La conexión con MariaDB está configurada.");
        System.out.println("Las funciones de metadatos se añadirán después.");
    }

    private void mostrarInformacion() {
        System.out.println("\nℹ️ ===== ASOCIACIÓN =====");
        System.out.println("🎲 Asociación de juegos de mesa");
        System.out.println("📍 Vitoria-Gasteiz");
        System.out.println(
                "Sistema de gestión de socias, juegos y partidas."
        );
    }

    private void despedida() {
        System.out.println("\n🎲 Gracias por utilizar la aplicación.");
        System.out.println("👋 ¡Hasta pronto!");
    }

    private int leerEntero() {
        while (true) {
            String entrada = sc.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("❌ Introduce un número válido: ");
            }
        }
    }
}
