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
            main.leerArchivo(main.nombreArchivo);
        } else {
            System.out.println("ingresa la ruta del archivo de configuración.");
        }

        BuzonEntrada buzonEntrada = new BuzonEntrada(10,main.nu);

        for(int i =0; i < main.nu; i++) {
            Usuario usuario = new Usuario(i, main.base*i, buzonEntrada, main.procesadores, main.memoria, main.almacenamiento);
            usuario.start();


        }

    }


    public void leerArchivo(String rutaArchivo) {
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

            System.out.println("¡Archivo leído y parámetros cargados exitosamente!");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Formato numérico inválido en el archivo. " + e.getMessage());
        }
    }


    
}
