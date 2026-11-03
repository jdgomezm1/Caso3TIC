package controladores;

<<<<<<< HEAD

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
=======
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
>>>>>>> 8c6455c487993a3363330454fb445de8c8a765e4
    }
}
