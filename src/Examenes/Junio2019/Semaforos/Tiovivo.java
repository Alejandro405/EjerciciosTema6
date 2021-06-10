package Examenes.Junio2019.Semaforos;

import java.util.concurrent.Semaphore;

public class Tiovivo {
	private int numClientes;
	private int capacity;


	private Semaphore mutex;
	private Semaphore esperaEntrada, esperaBajar;//Semáforos para la sincronización de clientes

	private Semaphore esperaPaseo, esperaLleno;
	public Tiovivo(int capac)
	{
		numClientes = 0;
		capacity = capac;

		mutex = new Semaphore(1, true);
		esperaEntrada = new Semaphore(1, true);
		esperaPaseo = new Semaphore(0, true);
		esperaBajar = new Semaphore(0, true);
		esperaLleno = new Semaphore(0, true);
	}



	public void subir (int id) throws InterruptedException {
		esperaEntrada.acquire();
		mutex.acquire();
		numClientes++;
		System.out.println("La hebra cond pid: "+id+" entra del recurso compartido");
		if (numClientes == capacity){
			esperaLleno.release();
		} else {//Si lo lenamos no dejamos que se suba nadie
			esperaEntrada.release();
		}
		mutex.release();

	}

	public void bajar (int id) throws InterruptedException {
		esperaBajar.acquire();
		mutex.acquire();
		numClientes--;
		System.out.println("La hebra cond pid: "+id+" sale del recurso compartido");
		if (numClientes == 0) {
			esperaEntrada.release();
		} else {
			esperaBajar.release();//Si soy el último en bajar, dejo que suban y que no se baje nadie
		}
		mutex.release();

	}

	/*
		Permite que los pasajeros que esten esperando suban
		SUBIR
	 */
	public void esperaLleno () throws InterruptedException {
		//TODO
		this.esperaLleno.acquire();
		System.out.println("Nos vamos de paseo; hay "+numClientes);
	}

	/*
		Permite que empienzen a bajar
		BAJAR
	 */
	public void finViaje () throws InterruptedException {
		System.out.println("El paseo ha acabado, toca bajarse; hay "+numClientes);
		esperaBajar.release();
		//TODO

	}

	public static void main(String[] args) {
		Tiovivo atraccion = new Tiovivo(5);
		Operario oper = new Operario( atraccion);
		Pasajero clientes[] = new Pasajero[10];

		oper.start();
		for (int i = 0; i < clientes.length; i++) {
			clientes[i] = new Pasajero(i, atraccion);
			clientes[i].start();
		}

		for (int i = 0; i < clientes.length; i++) {
			Thread.yield();
		}
	}
}
