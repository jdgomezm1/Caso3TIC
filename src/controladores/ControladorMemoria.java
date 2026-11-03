package controladores;



public class ControladorMemoria extends ControladorRecurso {
    private final int tamPaginaKiB;

    public ControladorMemoria(int memoriaGiB, int tamPaginaKiB) {
        super((int) (memoriaGiB * 1024L * 1024L / tamPaginaKiB)); // total paginas
        this.tamPaginaKiB = tamPaginaKiB;
    }
    @Override
    protected int traducir(long gib) {
        long kib = gib * 1024L * 1024L; // pasar de gib a kib para coherencia de unidades en calculo
        return (int) ((kib + tamPaginaKiB - 1) / tamPaginaKiB); // techo
    }
}

