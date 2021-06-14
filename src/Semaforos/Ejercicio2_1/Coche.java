package Semaforos.Ejercicio2_1;

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
    private Semaphore bajar;
    private Semaphore subir;

    public Coche(int capcac, int duracionPaseo) {
        paseando = false;
        numClientes = 0;

        tiempoEspera = duracionPaseo;
        capcacidad = capcac;
        mutex = new Semaphore(1, true);
        bajar = new Semaphore(0, true);
        subir = new Semaphore(1, true);
    }
    public Coche(int capac) {
        paseando = false;
        generador = new Random();
        numClientes = 0;

        tiempoEspera = generador.nextInt(30)+10;
        capcacidad = capac;
        mutex = new Semaphore(1, true);
        bajar = new Semaphore(0, true);
        subir = new Semaphore(1, true);
    }

    public void entraCliente(int id) throws InterruptedException {
        mutex.acquire();
        numClientes++;
        System.out.println("Llega el cliente con pid: "+id);
        if (numClientes == capcacidad) {
            //Si se llena el coche, no permitas que se suba nadie, pero permite que se puedan bajar
            subir.acquire();
            bajar.release();
            System.out.println("\t YA SE PUEDE PASEAR");
        }
        mutex.release();
    }


    public void pasearClientes(int id) throws InterruptedException {
        mutex.acquire();
        numClientes--;
        System.out.println("TREMENDO PASEO SE HA PEGADO LA HEBRA "+id);
        if (numClientes == 0) {
            //Si bacíio el cochecito dejo que suban, e impido que se baje nadie; Lo contrario al otro método
            subir.release();
            bajar.acquire();
        }
        mutex.release();
    }


    public static void main(String[] args) {
        Coche car = new Coche(4);
        Pasajero clientes[] = new Pasajero[4];
        for (int i = 0; i < clientes.length; i++) {
            clientes[i] = new Pasajero(i, car);
            clientes[i].start();
        }
    }
}
