package Locks.Ejercicios.Ejercicio1;

public class Niño extends Thread{
    private int id;
    private Mesa mesa;

    public Niño(int newId, Mesa newMesa){
        id = newId;
        mesa = newMesa;
    }

    @Override
    public void run()
    {
        try{
            while (!isInterrupted())
            {
                mesa.cogerPastel(id);
                Thread.sleep(1000);//come y juega
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
