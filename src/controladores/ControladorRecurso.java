package controladores;

import threads.MaquinaVirtual;

public abstract class ControladorRecurso {

    protected final int capacidadTotal;
    protected int numLibres;
    protected final MaquinaVirtual[] propietario; //null = libre, cada pos representa un recurso

    protected ControladorRecurso(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
        this.numLibres = capacidadTotal;
        this.propietario = new MaquinaVirtual[capacidadTotal];
    }

    protected abstract int traducir(long cantidad);

    public synchronized boolean reservar(MaquinaVirtual mv, long cantidad) {
        int n = traducir(cantidad);
        if (n > numLibres) {
            return false;
        }
        int asignadas = 0;
        for (int i = 0; i < propietario.length && asignadas < n; i++) {
            if (propietario[i] == null) {
                propietario[i] = mv;
                asignadas++;
            }
        }
        numLibres -= n;
        return true;
    }

    public synchronized void liberar(MaquinaVirtual mv) {
        for (int i = 0; i < propietario.length; i++) {
            if (propietario[i] == mv) {
                propietario[i] = null;
                numLibres++;
            }
        }
    }

    public synchronized int getLibres() {
        return numLibres;
    }

    public synchronized boolean estadoInicial() {
        return numLibres == capacidadTotal;
    }
}