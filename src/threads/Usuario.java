package threads;

import modelo.Solicitud;
import monitores.BuzonEntrada;

public class Usuario extends Thread {

    private int id;
    private int numSolicitudes;
    private BuzonEntrada buzonEntrada;
    private int maxProcesadores;
    private int maxMemoria;
    private int maxAlmacenamiento;

    public Usuario(int id, int numSolicitudes, BuzonEntrada buzonEntrada, int maxProcesadores, int maxMemoria, int maxAlmacenamiento) {
        this.id = id;
        this.numSolicitudes = numSolicitudes;
        this.buzonEntrada = buzonEntrada;
        this.maxProcesadores = maxProcesadores;
        this.maxMemoria = maxMemoria;
        this.maxAlmacenamiento = maxAlmacenamiento;
    }

    // CORRECCIÓN: número aleatorio entre 1 y el entero más cercano a 3/4 de la capacidad.
    // Antes: (int)(Math.random()*(0.75*max)+1). Cuando 0.75*max termina en .25
    // (max = 3, 7, 11, ...) podía salir un valor por encima del tope.
    // Ejemplo: max = 7 -> 0.75*7 = 5.25 -> el entero más cercano es 5, pero salía hasta 6.
    private static int aleatorioHastaTresCuartos(int max) {
        int tope = (int) Math.max(1, Math.round(0.75 * max)); // nunca menor que 1
        return 1 + (int) (Math.random() * tope);              // 1..tope
    }

    private void generarSolicitud(String idSol) throws InterruptedException {

        int procesadores = aleatorioHastaTresCuartos(maxProcesadores);

        int memoria = aleatorioHastaTresCuartos(maxMemoria);
        memoria = Integer.highestOneBit(memoria); // potencia de 2 más alta <= valor generado

        int almacenamiento = aleatorioHastaTresCuartos(maxAlmacenamiento);
        almacenamiento = (almacenamiento / 2) * 2; // múltiplo de 2 más alto <= valor generado
        // CORRECCIÓN: si el valor generado era 1, el múltiplo de 2 quedaba en 0 GiB
        // (una MV sin disco). Decisión: en ese caso se pide 2 GiB, o 1 si la capacidad
        // total de almacenamiento no alcanza para 2. Hay que mencionarlo en el informe.
        if (almacenamiento == 0) {
            almacenamiento = Math.min(2, maxAlmacenamiento);
        }

        System.out.println("Usuario " + this.id + " generando solicitud.");

        Solicitud solicitud = new Solicitud(idSol, this.id, procesadores, memoria, almacenamiento);

        buzonEntrada.ingresarSolicitud(solicitud);
    }

    public void run() {
        // CORRECCIÓN: try/finally. ingresarSolicitud ahora puede lanzar InterruptedException;
        // pase lo que pase, el usuario reporta su terminación en el buzón. Si no lo hiciera,
        // usuariosActivos nunca llegaría a 0 y los gestores no terminarían.
        try {
            for (int i = 0; i < numSolicitudes; i++) {
                generarSolicitud(i + "-" + id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            buzonEntrada.usuarioTermino();
        }
    }
}
