package controladores;

// controla los procesadores
// cada unidad es un procesador, no hay que traducir nada
public class ControladorProcesadores extends ControladorRecurso {

    public ControladorProcesadores(int numProcesadores) {
        super(numProcesadores);
    }

    @Override
    protected int traducir(int numProcesadores) {
        return numProcesadores;
    }
}
