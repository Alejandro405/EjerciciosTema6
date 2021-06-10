package Monitores.Ejercicio1;

public class Mesa {
    private static final int MAX_PASTELES = 7;
    private int numPasteles;

    public Mesa(){
        numPasteles = 0;
    }

    public synchronized void cogePastel(int idNiño) throws InterruptedException {
        while (numPasteles == 0)
            wait();
        //Sección crítaca
        numPasteles--;
        System.out.println("Niño con id: "+idNiño+". Quedan "+numPasteles+" pasteles restantes");
        //Si dejas la mesa vacía despierta al pastelero
        if (numPasteles == 0)
            notifyAll();
    }

    public synchronized void reponerPasteles(int idPastelero) throws InterruptedException {
        while (numPasteles > 0)
            wait();
        numPasteles = MAX_PASTELES;
        System.out.println("\tPastelero con id: "+idPastelero+", repone la mesa de pasteles. Hay "+MAX_PASTELES);
        notifyAll();
    }
}
