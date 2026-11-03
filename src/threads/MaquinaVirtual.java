package threads;

import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import controladores.ControladorProcesadores;
import modelo.Solicitud;
import monitores.ZonaEspera;

public class MaquinaVirtual extends Thread{

    String id;
    Solicitud solicitud;
    ZonaEspera zona;
    ControladorAlmacenamiento controladorAlmacenamiento;
    ControladorMemoria controladorMemoria;
    ControladorProcesadores controladorProcesadores;


    public MaquinaVirtual(String id, Solicitud solicitud, ZonaEspera zona, ControladorAlmacenamiento controladorAlmacenamiento, ControladorMemoria controladorMemoria, ControladorProcesadores controladorProcesadores) {
        this.id = id;
        this.solicitud = solicitud;
        this.zona = zona;
        this.controladorAlmacenamiento = controladorAlmacenamiento;
        this.controladorMemoria = controladorMemoria;
        this.controladorProcesadores = controladorProcesadores;
    }



    public void run() {
        System.out.println("Maquina Virtual " + id + " procesando solicitud: " + solicitud + " del usuario " + solicitud.getIdUsuario());
        // Aquí puedes agregar la lógica para procesar la solicitud utilizando los controladores y la zona de espera
    }

    public synchronized boolean reservarRecursos() {
        return false;
    }
    
}
