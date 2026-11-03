package controladores;

// controla el almacenamiento
// cada unidad es un bloque de tamBloqueKiB
public class ControladorAlmacenamiento extends ControladorRecurso {

    private final int tamBloqueKiB;

    public ControladorAlmacenamiento(int almacenamientoGiB, int tamBloqueKiB) {
        super(gibAUnidades(almacenamientoGiB, tamBloqueKiB));
        this.tamBloqueKiB = tamBloqueKiB;
    }

    // gib pedidos a numero de bloques, redondeando hacia arriba
    @Override
    protected int traducir(int gib) {
        return gibAUnidades(gib, tamBloqueKiB);
    }

    public int getTamBloqueKiB() {
        return tamBloqueKiB;
    }
}
