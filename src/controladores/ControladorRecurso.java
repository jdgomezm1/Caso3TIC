package controladores;

import threads.MaquinaVirtual;

// objeto de control de un recurso, no es un thread
// el recurso se divide en unidades: procesadores, paginas o bloques
// propietario[i] guarda la mv que tiene la unidad i, null si esta libre
// sincronizacion: el monitor es el propio controlador
// reservar y liberar son synchronized, asi revisar y asignar es un solo paso
// no hay wait: si no alcanza, reservar devuelve false y la mv decide que hacer
// no llama a otros objetos sincronizados, asi que no genera interbloqueos
public abstract class ControladorRecurso {

    protected final int capacidadTotal;           // total de unidades
    protected int numLibres;                      // unidades libres
    protected final MaquinaVirtual[] propietario; // dueña de cada unidad, null si esta libre

    protected ControladorRecurso(int capacidadTotal) {
        if (capacidadTotal < 1) {
            throw new IllegalArgumentException("La capacidad debe ser al menos 1 unidad");
        }
        this.capacidadTotal = capacidadTotal;
        this.numLibres = capacidadTotal;
        this.propietario = new MaquinaVirtual[capacidadTotal];
    }

    // reserva la cantidad pedida para la mv
    // todo o nada: si no hay suficientes unidades devuelve false y no asigna nada
    public synchronized boolean reservar(MaquinaVirtual mv, int cantidad) {
        int unidades = traducir(cantidad);
        if (unidades > numLibres) {
            return false;
        }
        int faltan = unidades;
        for (int i = 0; i < capacidadTotal && faltan > 0; i++) {
            if (propietario[i] == null) {
                propietario[i] = mv;
                faltan--;
            }
        }
        numLibres -= unidades;
        return true;
    }

    // libera todas las unidades de la mv
    // si no tiene ninguna no hace nada
    public synchronized void liberar(MaquinaVirtual mv) {
        for (int i = 0; i < capacidadTotal; i++) {
            if (propietario[i] == mv) {
                propietario[i] = null;
                numLibres++;
            }
        }
    }

    public synchronized int getLibres() {
        return numLibres;
    }

    // es final, no necesita synchronized
    public int getCapacidadTotal() {
        return capacidadTotal;
    }

    // cuantas unidades tiene la mv, sirve para pruebas
    public synchronized int unidadesDe(MaquinaVirtual mv) {
        int n = 0;
        for (MaquinaVirtual p : propietario) {
            if (p == mv) n++;
        }
        return n;
    }

    // verificacion final: todo el recurso esta libre
    // revisa el contador y el arreglo
    public synchronized boolean todoLibre() {
        if (numLibres != capacidadTotal) {
            return false;
        }
        for (MaquinaVirtual p : propietario) {
            if (p != null) return false;
        }
        return true;
    }

    // convierte lo pedido (procesadores o gib) a unidades del recurso
    protected abstract int traducir(int cantidad);

    // gib a unidades de tamKiB, redondeando hacia arriba
    // unidades = ceil(gib * 2^20 / tamKiB)
    // se usa long porque gib * 1024 * 1024 se desborda en int
    protected static int gibAUnidades(long gib, int tamKiB) {
        if (tamKiB < 1) {
            throw new IllegalArgumentException("El tamaño de página/bloque debe ser al menos 1 KiB");
        }
        long kib = gib * 1024L * 1024L;
        long unidades = (kib + tamKiB - 1) / tamKiB; // division redondeando hacia arriba
        if (unidades > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Demasiadas unidades (" + unidades
                    + "): use un tamaño de página/bloque más grande");
        }
        return (int) unidades;
    }

    @Override
    public synchronized String toString() {
        return getClass().getSimpleName() + "[" + numLibres + "/" + capacidadTotal + " libres]";
    }
}
