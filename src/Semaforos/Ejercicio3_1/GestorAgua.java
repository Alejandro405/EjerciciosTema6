package Semaforos.Ejercicio3_1;

import java.util.concurrent.Semaphore;

/*
    Por la condicicon de la reaccion podemos decir que se trata de un problema de productor consumido
        Oxígeno = Consumidor
        Hidrógeno = Productor
    Acceso a los contadores = exclusión mútua
 */
public class GestorAgua {
    //Retardos para controlar la ejeución de las hebras
    private static final int ESPERA_H = 5;
    private static final int ESPERA_O = 10;


    private int numHidrogeno;
    private int numOxigeno;
    private boolean produciendo;
    private Semaphore producir;
    private Semaphore mutex_Oxigeno;
    private Semaphore mutex_Hidrogeno;

    /*
        Solamente tenos una condicion de sincronización, por tanto se necesita al menos un semáforo, ademas de los de la
        exclusión mutua
     */
    public GestorAgua()
    {
        numHidrogeno = 0;
        numOxigeno = 0;
        produciendo = false;
        producir = new Semaphore(0, true);
        mutex_Hidrogeno = new Semaphore(1, true);
        mutex_Oxigeno = new Semaphore(1, true);
    }


    /*
        El oxigeno al ser el limitante, jugará el papel de consumidor
     */
    public void oListo(int id) throws InterruptedException {
        mutex_Oxigeno.acquire();
        numOxigeno++;
        System.out.println("Llega la hebra de oxígeno con pid: "+id);
        if (numHidrogeno == 2) {
            System.out.println("\tHay reactivos suficientes, comienza la reacción");
            numHidrogeno = 0;
            numOxigeno = 0;
            mutex_Hidrogeno.release();
            mutex_Oxigeno.release();
        }

    }
    /*
        El hidrógeno será el productor
     */
    public void hListo(int id) throws InterruptedException {
        mutex_Hidrogeno.acquire();
        System.out.println("Llega la hebra de hidrógeno con pid: "+id);
        numHidrogeno++;
        if (numHidrogeno < 2) {
            mutex_Hidrogeno.release();
        } else if (numHidrogeno == 2 && numOxigeno == 1) {
            System.out.println("\tHay reactivos suficientes, comienza la reacción");
            numHidrogeno = 0;
            numOxigeno = 0;
            mutex_Hidrogeno.release();
            mutex_Oxigeno.release();
        }
    }

    public static void main(String[] args) {
        GestorAgua gestor = new GestorAgua();
        Oxigeno o[] = new Oxigeno[3];
        Hidrogeno h[] = new Hidrogeno[6];
        for (int i = 0; i < h.length; i++) {
            if (i < o.length)
                o[i] = new Oxigeno(i, gestor);

            h[i] = new Hidrogeno(i, gestor);
        }

        for (int i = 0; i < h.length; i++) {
            if (i < o.length)
                o[i].start();

            h[i].start();
        }
    }
}
