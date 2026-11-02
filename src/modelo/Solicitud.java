package modelo;

public class Solicitud {

    private String id;
    private int idUsuario;
    private int numProcesadores;
    private int memoria;
    private int almacenamiento;

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
    
}
