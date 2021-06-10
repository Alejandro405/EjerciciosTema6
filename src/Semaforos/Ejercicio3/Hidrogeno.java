package Semaforos.Ejercicio3;

public class Hidrogeno extends Thread{
    private int id;
    private GestorAgua gestorAgua;

    public Hidrogeno(int newId, GestorAgua newGestor){
        id = newId;
        gestorAgua = newGestor;
    }

    @Override
    public void run()
    {
        try {
            int i = 0;
            while (i < 20) {
                gestorAgua.hListo(id);
                Thread.sleep(10);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
