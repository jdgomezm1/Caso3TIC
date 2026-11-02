package monitores;

import java.util.Queue;

import modelo.Solicitud;

public class BuzonEntrada {

    int capacidad;
    int usuariosActivos;
    Queue<Solicitud> solicitudes;

    public BuzonEntrada(int capacidad, int nu) {
        this.capacidad = capacidad;
        this.usuariosActivos = nu;
        // Inicializar la cola de solicitudes
        this.solicitudes = new java.util.LinkedList<>();
    }

    public synchronized void ingresarSolicitud(Solicitud solicitud) {

        while (solicitudes.size() >= capacidad) {
            try {
                wait(); // Espera si el buzón está lleno
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        solicitudes.add(solicitud);
        System.out.println("Solicitud ingresada: " + solicitud + "del usuario " + solicitud.getIdUsuario());
        notifyAll(); // Notifica a los gestores que hay una nueva solicitud

    }
    public synchronized Solicitud retirarSolicitud() {
        
        while (solicitudes.isEmpty()) {
            try {
                wait(); // Espera si el buzón está vacío
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Solicitud solicitud = solicitudes.poll();
        System.out.println("Solicitud retirada: " + solicitud + "del usuario " + solicitud.getIdUsuario());
        notifyAll(); // Notifica a los usuarios que hay espacio disponible en el buzón
        return solicitud;
    }
    public void usuarioTermino(){

        usuariosActivos--;
        System.out.println("Un usuario ha terminado. Usuarios activos restantes: " + usuariosActivos);
    }

    public boolean terminado(){
        return usuariosActivos == 0 && solicitudes.isEmpty();
    }
    
}
