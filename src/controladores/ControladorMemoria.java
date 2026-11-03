package controladores;

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
    }
}
