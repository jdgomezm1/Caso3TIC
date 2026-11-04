package threads;

import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import controladores.ControladorProcesadores;
import java.util.List;
import modelo.Solicitud;
import monitores.BuzonEntrada;
import monitores.ZonaEspera;

// saca solicitudes del buzon y por cada una crea e inicia una mv
// antes de sacar una revisa el umbral de la zona de espera
// si el buzon esta vacio hace espera semiactiva (yield)
// termina cuando no hay usuarios activos y el buzon esta vacio
public class Gestor extends Thread {

    int idGestor;
    BuzonEntrada buzonEntrada;
    ZonaEspera zona;
    ControladorProcesadores controladorProcesadores;
    ControladorAlmacenamiento controladorAlmacenamiento;
    ControladorMemoria controladorMemoria;
    // lista compartida entre todos los gestores, se protege con synchronized al agregar
    static List<MaquinaVirtual> maquinasVirtuales;


    public Gestor(int idGestor, BuzonEntrada buzonEntrada, ZonaEspera zona, ControladorProcesadores controladorProcesadores, ControladorAlmacenamiento controladorAlmacenamiento, ControladorMemoria controladorMemoria, List<MaquinaVirtual> maquinasVirtuales) {
        this.idGestor = idGestor;
        this.buzonEntrada = buzonEntrada;
        this.zona = zona;
        this.controladorProcesadores = controladorProcesadores;
        this.controladorAlmacenamiento = controladorAlmacenamiento;
        this.controladorMemoria = controladorMemoria;
        this.maquinasVirtuales = maquinasVirtuales;
    }

    public void run() {
        // sigue mientras haya usuarios activos o solicitudes en el buzon
        while (!buzonEntrada.terminado()) {

            // prioridad a las pendientes: si se alcanzo el umbral, espera total
            // mientras espera no saca nada del buzon
            if (zona.alcanzaUmbral()) {
                System.out.println("Gestor " + idGestor + " umbral alcanzado, espera a que se vacie la zona");
                zona.esperarVaciado();
                System.out.println("Gestor " + idGestor + " zona vacia, sigue");
            }

            Solicitud solicitud = buzonEntrada.retirarSolicitud();
            if (solicitud == null) {
                // buzon vacio: espera semiactiva y vuelve a revisar
                Thread.yield();
            } else {
                System.out.println("Gestor " + idGestor + " procesando solicitud: " + solicitud + " del usuario " + solicitud.getIdUsuario());
                // orden del constructor: procesadores, memoria, almacenamiento
                MaquinaVirtual mv = new MaquinaVirtual(solicitud.getId(), solicitud, zona, controladorProcesadores, controladorMemoria, controladorAlmacenamiento);
                synchronized (maquinasVirtuales) {
                    maquinasVirtuales.add(mv);
                }
                mv.start();
            }
        }
        System.out.println("Gestor " + idGestor + " termino");
    }

    public List<MaquinaVirtual> getMaquinasVirtuales() {
        return maquinasVirtuales;
    }



}