package monitores;

public class ZonaEspera {

    int umbral;
    long generacion;
    int pendientes;

    public long getGeneracion() {
        return generacion;
    }

    public void dormir(Long generacion) {
        while (generacion == this.generacion) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void entrar(){

    }

    public void salir(){

    }

    public void notificarLiberacion(){

    }

    public boolean alcanzaUmbral(){
        return false;
    }

    public void esperarVaciado(){

    }

    public int getPendientes() {
        return pendientes;
    }
    
}
