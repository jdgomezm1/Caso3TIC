package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import controladores.ControladorProcesadores;
import monitores.BuzonEntrada;
import monitores.ZonaEspera;
import threads.Gestor;
import threads.MaquinaVirtual;
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
            // si el archivo no se pudo leer, se termina
            if (!main.leerArchivo(main.nombreArchivo)) {
                return;
            }
        } else {
            System.out.println("ingresa la ruta del archivo de configuración.");
            // sin archivo no hay parametros, se termina
            return;
        }

        // capacidad del buzon = tam1
        BuzonEntrada buzonEntrada = new BuzonEntrada(main.tam1, main.nu);

        ZonaEspera zona = new ZonaEspera(main.tam2);

        ControladorProcesadores controladorProcesadores = new ControladorProcesadores(main.procesadores);
        ControladorMemoria controladorMemoria = new ControladorMemoria(main.memoria, main.pagina);
        ControladorAlmacenamiento controladorAlmacenamiento = new ControladorAlmacenamiento(main.almacenamiento, main.bloque);

        // se guardan los usuarios para hacerles join al final
        Usuario[] usuarios = new Usuario[main.nu];
        // lo mismo pa los gestores
        Gestor[] gestores = new Gestor[main.ng];
        // la lista compartida de MVs debe ser syncrhonized
        List<MaquinaVirtual> maquinasVirtuales = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < main.nu; i++) {
            // el usuario i genera base*(i+1) solicitudes
            // ojo: confirmar con el enunciado como se calcula a partir de base
            int id= i+1;
            usuarios[id] = new Usuario(id, main.base * id, buzonEntrada,main.procesadores, main.memoria, main.almacenamiento);
            usuarios[id].start();
        }

        for ( int i=0; i<main.ng; i++) {
            gestores[i] = new Gestor(i+1, buzonEntrada, zona, controladorProcesadores, controladorAlmacenamiento, controladorMemoria, maquinasVirtuales);
            gestores[i].start();
        }

        // esperar a que terminen los usuarios
        // ojo: sin gestores, si hay mas de tam1 solicitudes el buzon se llena y esto no termina
        try {
            for (Usuario u : usuarios) {
                u.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            for (Gestor g : gestores) {
                g.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        try {
            for (MaquinaVirtual mv : maquinasVirtuales) {
                mv.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // pendiente: verificar que el buzon y la zona queden vacios y los recursos libres
    }


    // devuelve true si el archivo se leyo bien
    public boolean leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // 1. numero de usuarios (nu)
            if ((linea = br.readLine()) != null) {
                this.nu = Integer.parseInt(linea.trim());
            }
            // 2. numero base de eventos (base)
            if ((linea = br.readLine()) != null) {
                this.base = Integer.parseInt(linea.trim());
            }
            // 3. numero de gestores (ng)
            if ((linea = br.readLine()) != null) {
                this.ng = Integer.parseInt(linea.trim());
            }
            // 4. capacidad del buzon de entrada (tam1)
            if ((linea = br.readLine()) != null) {
                this.tam1 = Integer.parseInt(linea.trim());
            }
            // 5. umbral de la zona de espera (tam2)
            if ((linea = br.readLine()) != null) {
                this.tam2 = Integer.parseInt(linea.trim());
            }
            // 6. numero de procesadores
            if ((linea = br.readLine()) != null) {
                this.procesadores = Integer.parseInt(linea.trim());
            }
            // 7. memoria en gib y tamaño de pagina en kib
            if ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                this.memoria = Integer.parseInt(partes[0]);
                if (partes.length > 1) {
                    this.pagina = Integer.parseInt(partes[1]);
                }
            }
            // 8. almacenamiento en gib y tamaño de bloque en kib
            if ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                this.almacenamiento = Integer.parseInt(partes[0]);
                if (partes.length > 1) {
                    this.bloque = Integer.parseInt(partes[1]);
                }
            }

            // validar que no falte ningun parametro
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
