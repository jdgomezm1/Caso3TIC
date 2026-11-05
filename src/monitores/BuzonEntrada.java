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
        // cola de solicitudes
        this.solicitudes = new java.util.LinkedList<>();
    }

    // deposita una solicitud
    // si el buzon esta lleno el usuario espera (espera pasiva)
    public synchronized void ingresarSolicitud(Solicitud solicitud) throws InterruptedException {

        while (solicitudes.size() >= capacidad) {
            System.out.println("Usuario " + solicitud.getIdUsuario() + " espera: buzon lleno");
            wait();
            System.out.println("Usuario " + solicitud.getIdUsuario() + " despierta y revisa el buzon");
        }

        solicitudes.add(solicitud);
        System.out.println("Solicitud ingresada: " + solicitud + " del usuario " + solicitud.getIdUsuario());

        // no hay notify: los gestores no hacen wait aqui (espera semiactiva)
    }

    // retira una solicitud, devuelve null si el buzon esta vacio
    // no hace wait: el gestor hace yield y vuelve a intentar (espera semiactiva)
    public synchronized Solicitud retirarSolicitud() {

        if (solicitudes.isEmpty()) {
            return null;
        }

        Solicitud solicitud = solicitudes.poll();
        System.out.println("Solicitud retirada: " + solicitud + " del usuario " + solicitud.getIdUsuario());
        notifyAll(); // despierta a los usuarios que esperan espacio
        return solicitud;
    }

    // synchronized porque usuariosActivos-- no es atomico
    public synchronized void usuarioTermino(){

        usuariosActivos--;
        System.out.println("Un usuario ha terminado. Usuarios activos restantes: " + usuariosActivos);
    }

    // true si no hay usuarios activos y el buzon esta vacio
    // synchronized para revisar las dos condiciones en un solo paso
    public synchronized boolean terminado(){
        return usuariosActivos == 0 && solicitudes.isEmpty();
    }

    // lo usa main en la verificacion final
    public synchronized boolean estaVacio() {
        return solicitudes.isEmpty();
    }

}
