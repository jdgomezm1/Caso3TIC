package modelo;

public class Solicitud {

    // CORRECCIÓN: atributos final. La solicitud no cambia después de creada,
    // así que es inmutable y se puede compartir entre threads sin sincronizar.
    private final String id;
    private final int idUsuario;
    private final int numProcesadores;
    private final int memoria;
    private final int almacenamiento;

    public Solicitud(String id, int idUsuario, int numProcesadores, int memoria, int almacenamiento) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.numProcesadores = numProcesadores;
        this.memoria = memoria;
        this.almacenamiento = almacenamiento;
    }

    public String getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    // CORRECCIÓN: faltaban los  getters. La mv los necesita
    // para pedir los recursos a cada controlador.
    public int getNumProcesadores() {
        return numProcesadores;
    }

    public int getMemoria() {
        return memoria;
    }

    public int getAlmacenamiento() {
        return almacenamiento;
    }

    // CORRECCIÓN: sin toString() los println mostraban "modelo.Solicitud@1b6d3586".
    @Override
    public String toString() {
        return id + " [cpu=" + numProcesadores + ", mem=" + memoria + " GiB, disco=" + almacenamiento + " GiB]";
    }
}
