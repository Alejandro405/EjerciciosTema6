package Semaforos.Ejercicio2_1;

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
            int i = 0;
            while (i < 4)
            {
                bagon.entraCliente(id);
                Thread.sleep(5);
                bagon.pasearClientes(id);
                Thread.sleep(5);
                i++;
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }


    }
}
