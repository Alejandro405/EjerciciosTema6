package Semaforos.Ejercicio4_1;

import java.util.Random;

public class Agente extends Thread{
    private int id;
    private Mesa mesa;
    private Random rand;

    private static final int DELAY = 50;

    public Agente(int newId, Mesa newMesa)
    {
        id = newId;
        mesa = newMesa;
        rand = new Random();
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                mesa.aportaIngredientes(rand.nextInt(3), rand.nextInt(3));
                Thread.sleep(DELAY); //Espero a que el fumador que le toque termine
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
