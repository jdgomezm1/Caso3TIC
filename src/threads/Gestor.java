package threads;

import java.util.List;

import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import controladores.ControladorProcesadores;
import modelo.Solicitud;
import monitores.BuzonEntrada;
import monitores.ZonaEspera;

public class Gestor extends Thread {

    int idGestor;
    BuzonEntrada buzonEntrada;
    ZonaEspera zona;
    ControladorProcesadores controladorProcesadores;
    ControladorAlmacenamiento controladorAlmacenamiento;
    ControladorMemoria controladorMemoria;
    List<MaquinaVirtual> maquinasVirtuales;


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
            while(true) {
            Solicitud solicitud = buzonEntrada.retirarSolicitud();
            if (solicitud == null) {
                try{
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            

            System.out.println("Gestor " + idGestor + " procesando solicitud: " + solicitud + " del usuario " + solicitud.getIdUsuario());
            // Aquí puedes agregar la lógica para procesar la solicitud utilizando los controladores y la zona de espera
        }
    }

    public List<MaquinaVirtual> getMaquinasVirtuales() {
        return maquinasVirtuales;
    }


    
}
