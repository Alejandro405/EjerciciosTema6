package Locks.Practica8.Ejercicio2;

public class Hidrogeno extends Thread {
    private int id;
    private GestorAgua gestor;

    public Hidrogeno (int newId, GestorAgua newGestor)
    {
        id = newId;
        gestor = newGestor;
    }

    @Override
    public void run()
    {
        try {
            while(!this.isInterrupted()) {
                Thread.sleep(100);
                gestor.hListo(id);
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
