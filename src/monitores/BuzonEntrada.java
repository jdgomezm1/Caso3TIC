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

    // CORRECCIÓN: ahora lanza InterruptedException en vez de atraparla dentro del while.
    // Antes, si el thread se interrumpía, se volvía a marcar la interrupción y el
    // siguiente wait() fallaba de inmediato: el while quedaba en espera activa.
    public synchronized void ingresarSolicitud(Solicitud solicitud) throws InterruptedException {

        while (solicitudes.size() >= capacidad) {
            wait(); // Espera PASIVA si el buzón está lleno (usuarios)
        }

        solicitudes.add(solicitud);
        System.out.println("Solicitud ingresada: " + solicitud + " del usuario " + solicitud.getIdUsuario());

        // CORRECCIÓN: se quitó el notifyAll() que había aquí. Los gestores hacen espera
        // SEMIACTIVA (yield), así que nunca están en wait() sobre este monitor; los únicos
        // que esperan aquí son usuarios con el buzón lleno, y agregar una solicitud no les
        // libera espacio. El notifyAll() solo los despertaba para volver a dormirse.
    }

    // CORRECCIÓN: ya no hace wait() si el buzón está vacío sino q retorna null.
    // en el enunciado pide espera SEMIACTIVA para los gestores: el gestor recibe null,
    // hace Thread.yield() y vuelve a intentar (eso va en GestorRecursos).
    // con wait(), cuando todos los usuarios terminaban, nadie volvía a hacer notify
    // y los gestores quedaban dormidos para siempre: el programa nunca terminaba.
    public synchronized Solicitud retirarSolicitud() {

        if (solicitudes.isEmpty()) {
            return null;
        }

        Solicitud solicitud = solicitudes.poll();
        System.out.println("Solicitud retirada: " + solicitud + " del usuario " + solicitud.getIdUsuario());
        notifyAll(); // Notifica a los usuarios que hay espacio disponible en el buzón
        return solicitud;
    }

    // CORRECCIÓN: ahora es synchronized. usuariosActivos-- no es atómico
    // (leer, restar, escribir). si dos usuarios terminaban al mismo tiempo se podía
    // perder una resta, el contador nunca llegaba a 0 y los gestores nunca terminaban.
    public synchronized void usuarioTermino(){

        usuariosActivos--;
        System.out.println("Un usuario ha terminado. Usuarios activos restantes: " + usuariosActivos);
    }

    // CORRECCIÓN: ahora es synchronized, para leer las dos condiciones en un solo paso
    // atómico y con valores actualizados (sin synchronized otro thread podría ver un
    // usuariosActivos viejo).
    public synchronized boolean terminado(){
        return usuariosActivos == 0 && solicitudes.isEmpty();
    }

    // NUEVO: lo usa main en la verificación final ("el buzón debe quedar vacío").
    public synchronized boolean estaVacio() {
        return solicitudes.isEmpty();
    }

}
