package controladores;

// controla los procesadores
// cada unidad es un procesador, no hay que traducir nada
public class ControladorProcesadores extends ControladorRecurso {
<<<<<<< HEAD
    public ControladorProcesadores(int procesadores) {
        super(procesadores);
    }
    @Override
    protected int traducir(long cantidad) {
        return (int) cantidad;
=======

    public ControladorProcesadores(int numProcesadores) {
        super(numProcesadores);
    }

    @Override
    protected int traducir(int numProcesadores) {
        return numProcesadores;
>>>>>>> 8c6455c487993a3363330454fb445de8c8a765e4
    }
}
