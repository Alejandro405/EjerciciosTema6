package Semaforos.Ejercicio3;

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
        System.out.println("El oxigeno nº" + id + " llega");
        mutex_Oxigeno.acquire();

        numOxigeno++;
        if (numHidrogeno >= 2) {
            producir.acquire();
            mutex_Hidrogeno.acquire();
            //SC: por cada átomo de oxígeno se consumen dos de hidrógeno
            numHidrogeno -= 2;
            numOxigeno--;
            System.out.println("La reacción se ha llevado acabo. Oxígeno = " + numOxigeno + " Hidrógeno = " + numHidrogeno);
            mutex_Oxigeno.release();
            mutex_Hidrogeno.release();
        } else {
            mutex_Oxigeno.release();
            System.out.println("Faltan átomos de hidrógeno para la reacción");
        }
    }
    /*
        El hidrógeno será el productor
     */
    public void hListo(int id) throws InterruptedException {
        System.out.println("\t Hidrogeno nº"+id+" llega");
        mutex_Oxigeno.acquire();
        numHidrogeno++;
        if (numHidrogeno >= 2){
            producir.release();
            System.out.println("\t Hay átomos suficientes para la reacción. Hay "+numHidrogeno+" átomos de hidrógeno");
            mutex_Oxigeno.release();
        } else {
            mutex_Oxigeno.release();
            System.out.println("\t Faltan átomos para la reacción");
        }
    }
}
