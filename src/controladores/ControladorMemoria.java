package controladores;

<<<<<<< HEAD


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
=======
// controla la memoria
// cada unidad es una pagina de tamPaginaKiB
public class ControladorMemoria extends ControladorRecurso {

    private final int tamPaginaKiB;

    public ControladorMemoria(int memoriaGiB, int tamPaginaKiB) {
        super(gibAUnidades(memoriaGiB, tamPaginaKiB));
        this.tamPaginaKiB = tamPaginaKiB;
    }

    // gib pedidos a numero de paginas, redondeando hacia arriba
    @Override
    protected int traducir(int gib) {
        return gibAUnidades(gib, tamPaginaKiB);
    }

    public int getTamPaginaKiB() {
        return tamPaginaKiB;
>>>>>>> 8c6455c487993a3363330454fb445de8c8a765e4
    }
}

