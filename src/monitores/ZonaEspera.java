package monitores;

public class ZonaEspera {

    private long generacion = 0; // sube cada vez que alguien libera recursos para evitar que maquinas se queden dormidas cuando en verdad se libero un recurso
    private int pendientes = 0; // MVs que fallaron Y estan en espera
    private final int umbral;

    public ZonaEspera(int umbral) {
        this.umbral = umbral;
    }

    public synchronized long getGeneracion() {
        return generacion;
    }

    // La MV la llama solo en su PRIMER fallo
    public synchronized void entrar() {
        pendientes++;
    }

    // La MV la llama cuando por fin logra reservar los tres recursos
    public synchronized void salir() {
        pendientes--;
        if (pendientes == 0) {
            notifyAll();
        }
    }

    // Espera pasiva: duerme solo si nadie ha liberado desde que la MV leyó 'gen'
    public synchronized void dormir(long gen) {
        while (generacion == gen) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // Se llama después de liberar recursos
    public synchronized void notificarLiberacion() {
        generacion++;
        notifyAll();
    }

    public synchronized boolean alcanzaUmbral() {
        return pendientes >= umbral;
    }

    // Espera total del gestor
    public synchronized void esperarVaciado() {
        while (pendientes > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
