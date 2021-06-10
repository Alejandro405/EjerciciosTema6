package Locks.Practica8.Ejercicio2;

public class Oxigeno extends Thread{
    private int id;
    private GestorAgua gestor;

    public Oxigeno (int newId, GestorAgua newGestor)
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
                gestor.oListo(id);
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
