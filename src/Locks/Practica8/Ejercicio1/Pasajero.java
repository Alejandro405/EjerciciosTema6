package Locks.Practica8.Ejercicio1;

public class Pasajero extends Thread{
    private int id;
    private Coche bagon;

    public Pasajero(int newId, Coche newBagon)
    {
        id = newId;
        bagon = newBagon;
    }

    @Override
    public void run()
    {
        boolean interrupted = false;
        try {
            while (!interrupted)
            {
                bagon.entraCliente(id);
                //Como ya me he montado me tengo que bajar
                bagon.bajaCliente(id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            interrupted = true;
        }
    }
}
