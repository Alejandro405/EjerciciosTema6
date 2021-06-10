package Locks.Ejercicios.Ejercicio1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mesa {
    private int numPasteles;
    private static final int MAX_PASTELES = 10;

    private Lock l;
    private Condition esperaCogerTrozo, esperaReponer;

    public Mesa()
    {
        numPasteles = MAX_PASTELES;

        l = new ReentrantLock(true);
        esperaCogerTrozo = l.newCondition();
        esperaReponer = l.newCondition();
    }

    public void cogerPastel(int id) throws InterruptedException {
        l.lock();
        try{
            while (numPasteles == 0)
                esperaCogerTrozo.await();
            numPasteles--;
            System.out.println("El niño "+id+" coge trozo de pastel");
            if (numPasteles == 0)
                esperaReponer.signalAll();
        } finally {
            l.unlock();
        }
    }

    public void reponerPasteles(int id) throws InterruptedException {
        l.lock();
        try{
            while(numPasteles > 0)
                esperaReponer.await();
            System.out.println("\tHay que reponer los pasteles");
            numPasteles = MAX_PASTELES;
            System.out.println("\tYa se ha repuesto los pasteles");
            esperaCogerTrozo.signalAll();
        } finally {
            l.unlock();
        }
    }

    public static void main(String[] args) {
        Mesa mesa = new Mesa();
        Thread niños[] = new Niño[10];
        Pastelero pastelero = new Pastelero(0, mesa);

        pastelero.start();
        for (int i = 0; i < niños.length; i++) {
            niños[i] = new Niño(i, mesa);
            niños[i].start();
        }
    }
}
