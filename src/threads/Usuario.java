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

    private void generarSolicitud(String idSol) {

        int procesadores= (int) (Math.random()*((3.0/4.0)*maxProcesadores)+1);
        int memoria = (int) (Math.random()*((3.0/4.0)*maxMemoria)+1);
        memoria= Integer.highestOneBit(memoria);

        int almacenamiento = (int) (Math.random()*((3.0/4.0)*maxAlmacenamiento)+1);
        almacenamiento= (int) almacenamiento/2;
        almacenamiento = 2*almacenamiento;

        System.out.println("Usuario " + this.id + " generando solicitud.");

        Solicitud solicitud = new Solicitud(idSol, this.id, procesadores, memoria, almacenamiento);

        buzonEntrada.ingresarSolicitud(solicitud);
    }

    public void run() {
        for (int i = 0; i < numSolicitudes; i++) {
            generarSolicitud(i+"-"+id);
        }
        buzonEntrada.usuarioTermino();
    }
}
