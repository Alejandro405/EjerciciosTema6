package Semaforos.Ejercicio3_1;

public class Oxigeno extends Thread{
    private int id;
    private GestorAgua gestorAgua;

    public Oxigeno(int newId, GestorAgua newGestor){
        id = newId;
        gestorAgua = newGestor;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (i < 2) {
                gestorAgua.oListo(id);
                Thread.sleep(5);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
