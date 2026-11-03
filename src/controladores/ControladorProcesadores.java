package controladores;

public class ControladorProcesadores extends ControladorRecurso {
    public ControladorProcesadores(int procesadores) {
        super(procesadores);
    }
    @Override
    protected int traducir(long cantidad) {
        return (int) cantidad;
    }
}
