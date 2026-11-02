package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import monitores.BuzonEntrada;
import threads.Usuario;

public class Main {

    int nu;
    int base;
    int ng;
    int tam1;
    int tam2;
    int procesadores;
    Integer memoria;
    Integer pagina;
    Integer almacenamiento;
    Integer bloque;

    String nombreArchivo = "";

    public static void main(String[] args) {
        Main main = new Main();
        if (args.length > 0) {
            main.nombreArchivo = args[0];
            // CORRECCIÓN: si el archivo no se pudo leer, se termina. Antes el programa
            // seguía con todos los parámetros en 0 (o null, y fallaba al desempaquetar).
            if (!main.leerArchivo(main.nombreArchivo)) {
                return;
            }
        } else {
            System.out.println("ingresa la ruta del archivo de configuración.");
            // CORRECCIÓN: faltaba este return; sin archivo el programa seguía con nu = 0, etc.
            return;
        }

        // CORRECCIÓN: la capacidad del buzón es el parámetro tam1, no un 10 fijo.
        BuzonEntrada buzonEntrada = new BuzonEntrada(main.tam1, main.nu);

        // PENDIENTE (bloque A/B): crear aquí la ZonaEspera (umbral = tam2) y los
        // tres controladores (procesadores; memoria + pagina; almacenamiento + bloque).

        // CORRECCIÓN: se guardan los usuarios para poder hacerles join() al final.
        Usuario[] usuarios = new Usuario[main.nu];

        for (int i = 0; i < main.nu; i++) {
            // CORRECCIÓN: antes era main.base*i; con i = 0 el usuario 0 generaba 0 solicitudes.
            // Se usa base*(i+1). OJO: confirmar con la página del enunciado que explica
            // cómo se calcula el número de solicitudes de cada usuario a partir de base.
            usuarios[i] = new Usuario(i, main.base * (i + 1), buzonEntrada,
                    main.procesadores, main.memoria, main.almacenamiento);
            usuarios[i].start();
        }

        // PENDIENTE (bloque A): crear e iniciar los ng gestores.

        // CORRECCIÓN: esperar a que terminen los usuarios con join().
        // OJO: mientras no existan gestores, si los usuarios generan más de tam1 solicitudes
        // se quedan bloqueados con el buzón lleno y este join() no termina.
        try {
            for (Usuario u : usuarios) {
                u.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // PENDIENTE (bloque A): join() a los gestores y luego a todas las MVs creadas;
        // después verificar el estado final: buzón vacío (buzonEntrada.estaVacio()),
        // zona de espera vacía y todos los recursos libres en los tres controladores.
    }


    // CORRECCIÓN: ahora devuelve boolean (true si se leyó bien) para que main
    // no siga ejecutando con parámetros inválidos.
    public boolean leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // 1. Número de usuarios (nu)
            if ((linea = br.readLine()) != null) {
                this.nu = Integer.parseInt(linea.trim());
            }
            // 2. Número base de eventos (base)
            if ((linea = br.readLine()) != null) {
                this.base = Integer.parseInt(linea.trim());
            }
            // 3. Número de gestores (ng)
            if ((linea = br.readLine()) != null) {
                this.ng = Integer.parseInt(linea.trim());
            }
            // 4. Capacidad del buzón de entrada (tam1)
            if ((linea = br.readLine()) != null) {
                this.tam1 = Integer.parseInt(linea.trim());
            }
            // 5. Capacidad de la zona de espera (tam2)
            if ((linea = br.readLine()) != null) {
                this.tam2 = Integer.parseInt(linea.trim());
            }
            // 6. Número de procesadores en el sistema (procesadores)
            if ((linea = br.readLine()) != null) {
                this.procesadores = Integer.parseInt(linea.trim());
            }
            // 7. Capacidad en memoria y tamaño de página (memoria, pagina)
            if ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                this.memoria = Integer.parseInt(partes[0]);
                if (partes.length > 1) {
                    this.pagina = Integer.parseInt(partes[1]);
                }
            }
            // 8. Capacidad en almacenamiento y tamaño de bloque (almacenamiento, bloque)
            if ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                this.almacenamiento = Integer.parseInt(partes[0]);
                if (partes.length > 1) {
                    this.bloque = Integer.parseInt(partes[1]);
                }
            }

            // CORRECCIÓN: validar que no falte ninguna línea. Antes, si faltaba la línea 7/8
            // (o el tamaño de página/bloque), memoria/pagina/... quedaban en null y el programa
            // fallaba más adelante con un NullPointerException difícil de rastrear.
            if (nu < 1 || base < 1 || ng < 1 || tam1 < 1 || tam2 < 0 || procesadores < 1
                    || memoria == null || pagina == null || almacenamiento == null || bloque == null) {
                System.err.println("Error: faltan parámetros o hay valores fuera de rango en " + rutaArchivo);
                return false;
            }

            System.out.println("¡Archivo leído y parámetros cargados exitosamente!");
            return true;

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Formato numérico inválido en el archivo. " + e.getMessage());
        }
        return false;
    }



}
