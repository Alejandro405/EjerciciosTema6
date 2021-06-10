package Locks.Practica8.Ejercicio1;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
    IMPORTATNEEEEEE: CON locks  ===> AWAIT()
 */
public class Coche extends Thread{
    private int id;
    private int capacity;
    private int duracionPaseo;
    private Random rand;//Generar tiempo del paseo
    private static final int MAX_CAPACITY = 4;
    private int numPaseos;

    private Lock l;
    private Condition condPasear;
    private Condition condSubir;
    private Condition condBajar;
    private int numPasajeros;
    private boolean puedeBajar;
    private boolean puedeSubir;

    public Coche(int newId) {
        id = newId;
        rand = new Random();
        duracionPaseo = rand.nextInt(10);
        capacity = MAX_CAPACITY;
        numPaseos = MAX_CAPACITY;

        l = new ReentrantLock(true);
        numPasajeros =0;
        puedeBajar = false;
        puedeSubir = true;
        condPasear = l.newCondition();
        condSubir = l.newCondition();
        condBajar = l.newCondition();
    }

    /*
    Este método es usado por los clientes para subir al bagón
    C1: no puede estar lleno
    c2: no puede estar el bagon en circulación
     */
    public void entraCliente(int id) throws InterruptedException {
        l.lock();
        try {
            //Condiciones de sincronización
            while (!puedeSubir) //No se llene
                condSubir.await();
            //SC
            numPasajeros++;
            System.out.println("Pasajero con id"+id+" sube al bagon. Hay: "+numPasajeros);
            //Post-Protocolo
            if (numPasajeros == capacity)//Si lo lleno, a pasesar
            {
                //this.puedeBajar = false;
                puedeSubir = false;
                condPasear.signal();
            }
        } finally {
            l.unlock();
        }
    }

    public void bajaCliente(int id) throws InterruptedException {
        l.lock();
        try {
            while (!puedeBajar)
                condBajar.await();
            System.out.println("Noooveee que paso se ha pegado la hebra "+id+"!!");
            numPasajeros--;
            if (numPasajeros == 0) {
                System.out.println("Ya se han bajado todos del bagón");
                condSubir.signalAll();
                puedeBajar = false;
                puedeSubir = true;
            }
        }finally {
            l.unlock();
        }

    }

    public void circular() throws InterruptedException {
        l.lock();
        try {
            while (numPasajeros < capacity){
                condPasear.await();
            }
            //SC
            puedeSubir = false;
            puedeBajar = false;
            Thread.sleep(duracionPaseo);
            System.out.println("\t\t FIN DEL PASEO");
            //Post-Protocolos
            puedeBajar = true;
            condBajar.signalAll();
        } finally {
            l.unlock();
        }
    }

    /*
    método run = método circular
    C1: solo puede circular si el bagón está lleno, en caso contrario tiene que permanecer bloqueado
     */
    @Override
    public void run()
    {
        boolean interrupted = false;
        try {
            while (numPaseos > 0)
            {
                circular();
                numPaseos--;
            }
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
    }
}
