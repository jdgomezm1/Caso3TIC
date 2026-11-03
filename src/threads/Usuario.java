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

    // numero aleatorio entre 1 y el entero mas cercano a 3/4 de la capacidad
    private static int aleatorioHastaTresCuartos(int max) {
        int tope = (int) Math.max(1, Math.round(0.75 * max)); // nunca menor que 1
        return 1 + (int) (Math.random() * tope);              // 1..tope
    }

    private void generarSolicitud(String idSol) throws InterruptedException {

        int procesadores = aleatorioHastaTresCuartos(maxProcesadores);

        int memoria = aleatorioHastaTresCuartos(maxMemoria);
        memoria = Integer.highestOneBit(memoria); // potencia de 2 mas alta <= valor

        int almacenamiento = aleatorioHastaTresCuartos(maxAlmacenamiento);
        almacenamiento = (almacenamiento / 2) * 2; // multiplo de 2 mas alto <= valor
        // si salio 1, el multiplo de 2 seria 0: se pide 2 (o 1 si la capacidad no alcanza)
        if (almacenamiento == 0) {
            almacenamiento = Math.min(2, maxAlmacenamiento);
        }

        System.out.println("Usuario " + this.id + " generando solicitud.");

        Solicitud solicitud = new Solicitud(idSol, this.id, procesadores, memoria, almacenamiento);

        buzonEntrada.ingresarSolicitud(solicitud);
    }

    public void run() {
        // finally: el usuario siempre reporta que termino, si no los gestores no terminan
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
