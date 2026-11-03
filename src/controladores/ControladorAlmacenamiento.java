package controladores;


public class ControladorAlmacenamiento extends ControladorRecurso {
    private final int tamBloqueKiB;

    public ControladorAlmacenamiento(int almacenamientoGiB, int tamBloqueKiB) {
        super((int) (almacenamientoGiB * 1024L * 1024L / tamBloqueKiB)); // total bloques
        this.tamBloqueKiB = tamBloqueKiB;
    }
    @Override
    protected int traducir(long gib) {
        long kib = gib * 1024L * 1024L; // pasar de gib a kib para coherencia de unidades en calculo
        return (int) ((kib + tamBloqueKiB - 1) / tamBloqueKiB);
    }
}
