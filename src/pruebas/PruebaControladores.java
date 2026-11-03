package pruebas;

import controladores.ControladorAlmacenamiento;
import controladores.ControladorMemoria;
import controladores.ControladorProcesadores;
import controladores.ControladorRecurso;
import threads.MaquinaVirtual;

// pruebas de los controladores, sirven para la seccion de validacion
// ejecutar: java -cp out pruebas.PruebaControladores
// las mvs solo se usan como dueñas de las unidades, no se inician
public class PruebaControladores {

    private static int fallos = 0;

    public static void main(String[] args) throws InterruptedException {
        pruebaTraduccion();
        pruebaTodoONada();
        pruebaLiberar();
        pruebaConcurrencia();

        System.out.println();
        System.out.println(fallos == 0 ? "TODAS LAS PRUEBAS PASARON" : "FALLARON " + fallos + " PRUEBAS");
    }

    // ojo: cuando maquinavirtual tenga constructor con parametros, cambiar esta linea
    // por ejemplo: new MaquinaVirtual(id, null, null, null, null, null)
    private static MaquinaVirtual nuevaMV(int id) {
        return new MaquinaVirtual();
    }

    // 1. gib a paginas o bloques, redondeando hacia arriba
    private static void pruebaTraduccion() {
        System.out.println("== Traducción GiB -> unidades");
        ControladorMemoria mem = new ControladorMemoria(16, 4);
        verificar("16 GiB con páginas de 4 KiB = 4194304 páginas", mem.getCapacidadTotal() == 4_194_304);

        ControladorAlmacenamiento alm = new ControladorAlmacenamiento(64, 1024);
        verificar("64 GiB con bloques de 1024 KiB = 65536 bloques", alm.getCapacidadTotal() == 65_536);

        // tamaño que no divide exacto: 1048576 / 3 = 349525.33 -> 349526
        ControladorMemoria memRara = new ControladorMemoria(2, 3);
        MaquinaVirtual mv = nuevaMV(1);
        memRara.reservar(mv, 1);
        verificar("1 GiB con páginas de 3 KiB redondea hacia arriba a 349526 páginas",
                memRara.unidadesDe(mv) == 349_526);
    }

    // 2. si no alcanza no se asigna nada y el estado no cambia
    private static void pruebaTodoONada() {
        System.out.println("== Reserva todo o nada");
        ControladorProcesadores cp = new ControladorProcesadores(8);
        MaquinaVirtual a = nuevaMV(1);
        MaquinaVirtual b = nuevaMV(2);

        verificar("A reserva 6 de 8 procesadores", cp.reservar(a, 6));
        verificar("quedan 2 libres", cp.getLibres() == 2);
        verificar("B pide 3 y se rechaza", !cp.reservar(b, 3));
        verificar("tras el rechazo siguen 2 libres", cp.getLibres() == 2);
        verificar("B no quedó con ninguna unidad", cp.unidadesDe(b) == 0);
        verificar("B pide exactamente 2 y se acepta", cp.reservar(b, 2));
        verificar("quedan 0 libres", cp.getLibres() == 0);
    }

    // 3. liberar devuelve solo las unidades de esa mv
    private static void pruebaLiberar() {
        System.out.println("== Liberación");
        ControladorMemoria mem = new ControladorMemoria(4, 1024); // 4096 paginas
        MaquinaVirtual a = nuevaMV(1);
        MaquinaVirtual b = nuevaMV(2);
        MaquinaVirtual c = nuevaMV(3);

        mem.reservar(a, 1); // 1024 paginas
        mem.reservar(b, 2); // 2048 paginas
        mem.liberar(a);
        verificar("al liberar A, B conserva sus 2048 páginas", mem.unidadesDe(b) == 2048);
        verificar("quedan 2048 libres", mem.getLibres() == 2048);

        mem.liberar(c); // c no tiene nada
        verificar("liberar una MV sin unidades no cambia nada", mem.getLibres() == 2048);

        mem.liberar(b);
        verificar("al liberar todo, el recurso vuelve al estado inicial", mem.todoLibre());
    }

    // 4. muchos threads reservando y liberando al tiempo
    // si reservar y liberar no fueran atomicos se asignaria de mas o no quedaria todo libre
    private static void pruebaConcurrencia() throws InterruptedException {
        System.out.println("== Concurrencia (20 threads x 5000 operaciones)");
        final ControladorRecurso cp = new ControladorProcesadores(16);
        final int nThreads = 20;
        final int[] excesos = new int[1];
        Thread[] hilos = new Thread[nThreads];

        for (int t = 0; t < nThreads; t++) {
            final MaquinaVirtual mv = nuevaMV(t);
            hilos[t] = new Thread(() -> {
                for (int k = 0; k < 5000; k++) {
                    int pide = 1 + (int) (Math.random() * 12); // entre 1 y 12, 3/4 de 16
                    if (cp.reservar(mv, pide)) {
                        int libres = cp.getLibres();
                        if (libres < 0 || cp.unidadesDe(mv) != pide) {
                            synchronized (excesos) { excesos[0]++; }
                        }
                        Thread.yield();
                        cp.liberar(mv);
                    }
                }
            });
        }
        for (Thread h : hilos) h.start();
        for (Thread h : hilos) h.join();

        verificar("ninguna MV recibió unidades de más ni de menos", excesos[0] == 0);
        verificar("al terminar, todos los procesadores quedan libres", cp.todoLibre());
    }

    private static void verificar(String descripcion, boolean ok) {
        System.out.println((ok ? "  [OK]    " : "  [FALLA] ") + descripcion);
        if (!ok) fallos++;
    }
}
