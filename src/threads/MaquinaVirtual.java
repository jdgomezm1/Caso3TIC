package threads;

import java.util.concurrent.ThreadLocalRandom;
import modelo.Solicitud;
import monitores.ZonaEspera;
import controladores.*;

public class MaquinaVirtual extends Thread {

    private final String id;
    private final Solicitud solicitud;
    private final ZonaEspera zona;
    private final ControladorProcesadores procesadores;
    private final ControladorMemoria memoria;
    private final ControladorAlmacenamiento almacenamiento;

    public MaquinaVirtual(String id, Solicitud solicitud, ZonaEspera zona,ControladorProcesadores procesadores,ControladorMemoria memoria,ControladorAlmacenamiento almacenamiento) {
        this.id = id;
        this.solicitud = solicitud;
        this.zona = zona;
        this.procesadores = procesadores;
        this.memoria = memoria;
        this.almacenamiento = almacenamiento;
    }

    @Override
    public void run() {
        boolean enZona = false;

        while (true) {
            long gen = zona.getGeneracion(); // leer antes de intentar reservar para evitar ignorar una liberación de recursos que se da antes de que se duerma la MV
            if (reservarRecursos()) {
                break;
            }
            if (!enZona) {
                zona.entrar();
                enZona = true;
            }
            zona.dormir(gen);
        }

        if (enZona) {
            zona.salir();
        }

        ejecutar();
        liberarTodo();
        zona.notificarLiberacion();
    }

    // se sigue el orden fijo y se retorna si se pudo reservar todo, false si falla alguno de los recursos
    private boolean reservarRecursos() {
        if (!procesadores.reservar(this, solicitud.getNumProcesadores())) {
            return false;
        }
        if (!memoria.reservar(this, solicitud.getMemoria())) {
            procesadores.liberar(this);
            return false;
        }
        if (!almacenamiento.reservar(this, solicitud.getAlmacenamiento())) {
            memoria.liberar(this);
            procesadores.liberar(this);
            return false;
        }
        return true;
    }

    

    private void ejecutar() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(4000, 40001));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void liberarTodo() {
        procesadores.liberar(this);
        memoria.liberar(this);
        almacenamiento.liberar(this);
    }
}

