package modelo;

public class Solicitud {

    // atributos final: la solicitud no cambia despues de creada
    // por eso se puede compartir entre threads sin sincronizar
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

    // la mv usa estos getters para pedir los recursos
    public int getNumProcesadores() {
        return numProcesadores;
    }

    public int getMemoria() {
        return memoria;
    }

    public int getAlmacenamiento() {
        return almacenamiento;
    }

    // para que los println muestren la solicitud legible
    @Override
    public String toString() {
        return id + " [cpu=" + numProcesadores + ", mem=" + memoria + " GiB, disco=" + almacenamiento + " GiB]";
    }
}
