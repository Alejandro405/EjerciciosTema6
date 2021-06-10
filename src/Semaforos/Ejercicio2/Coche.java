package Semaforos.Ejercicio2;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 *  Recurso comparido
 */
public class Coche {
    private Random generador;
    private boolean paseando;
    private int capcacidad;
    private int numClientes;
    private int tiempoEspera;
    private Semaphore mutex;//Controla el numero de clientes que entrana al bagon para circiular
    private Semaphore circular;

    public Coche(int capcac, int duracionPaseo) {
        paseando = false;
        numClientes = 0;

        tiempoEspera = duracionPaseo;
        capcacidad = capcac;
        mutex = new Semaphore(1, true);
        circular = new Semaphore(0, true);
    }
    public Coche(int capac) {
        paseando = false;
        generador = new Random();
        numClientes = 0;

        tiempoEspera = generador.nextInt(30)+10;
        capcacidad = capac;
        mutex = new Semaphore(1, true);
        circular = new Semaphore(0, true);
    }

    public void entraCliente(int id) throws InterruptedException {
        System.out.println("Cliente "+id+" quiere entrar");
        if (numClientes < capcacidad)
        {
            mutex.acquire();
            numClientes++;
            mutex.release();
            if (numClientes == capcacidad) {
                System.out.println("EL cliente " + id + " acaba de entrar al bagon. LISTOS PARA EL PASEO!!");
                circular.release();//Ya se ha llenado el bagon, ya se puede pasear
            } else {
                System.out.println("EL cliente " + id + " acaba de entrar al bagon, faltan " + (capcacidad - numClientes));
            }
        }
        else
        {
            System.out.println("No se puede entrar al bagon");
        }
    }

    public void pasearClientes() throws InterruptedException {
        mutex.acquire();
        if (numClientes == capcacidad && !paseando)
        {
            numClientes = 0;
            paseando = true;
            mutex.release();
            System.out.println("EL bagon esta en marcha!!!!");
            Thread.sleep(tiempoEspera);
            paseando = false;
        }
        else
        {
            mutex.release();
            if (paseando){
                System.out.println("Los clientes se estan paseando. ESPERE!!!");
            } else {
                System.out.println("Faltan Clientes para dar el paseo. HAY "+numClientes);
            }
        }
    }
}
