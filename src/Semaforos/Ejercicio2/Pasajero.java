package Semaforos.Ejercicio2;

public class Pasajero extends Thread{
    private int id;
    private Coche bagon;

    public Pasajero(int i, Coche b){
        id = i;
        bagon = b;
    }

    @Override
    public void run()
    {
        try {
            while (true)
            {
                bagon.entraCliente(id);
                Thread.sleep(5);
                bagon.pasearClientes();
                Thread.sleep(5);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }


    }
}
