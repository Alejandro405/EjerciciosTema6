package Locks.Ejercicios.Ejercicio1;

public class Pastelero extends Thread {
    private int id;
    private Mesa mesa;

    public Pastelero(int newId, Mesa newMesa)
    {
        mesa = newMesa;
        id = newId;
    }

    @Override
    public void run(){
        try{
            while (!isInterrupted())
            {
                mesa.reponerPasteles(id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
