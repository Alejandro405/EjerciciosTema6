package Examenes.Junio2019.Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tiovivo {
	private int capacity;
	private int numClients;

	private boolean finDelPaseo;
	private Lock l;
	private Condition colaEntrada, esperaBajar;//Variables condicion para la sincronización de los clientes
	private Condition esperaLleno, esperaDesalojo;

	public Tiovivo(int newCapacity) {
		numClients = 0;
		capacity = newCapacity;
		l = new ReentrantLock(true);

		finDelPaseo = false;
		colaEntrada = l.newCondition();
		esperaBajar = l.newCondition();
		esperaLleno = l.newCondition();
		esperaDesalojo = l.newCondition();
	}
	
	public void subir(int id) throws InterruptedException {
		//TODO
		l.lock();
		try {
			while (numClients == capacity)
				this.colaEntrada.await();

			numClients++;
			System.out.println("Llega el cliente "+id+". Hay "+numClients);

			if (numClients == capacity) {
				esperaLleno.signalAll();
			} else {
				colaEntrada.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
	
	public void bajar(int id) throws InterruptedException {
		//TODO
		l.lock();
		try {
			while (!finDelPaseo)
				esperaBajar.await();

			numClients--;
			System.out.println("Se baja el cliente con id "+id+", quedan: "+numClients);

			if (numClients == 0) {
				esperaDesalojo.signalAll();
			}

		} finally {
			l.unlock();
		}
	}
	
	public void esperaLleno () throws InterruptedException {
		//TODO
		l.lock();
		try {

			while (numClients < capacity)
				esperaLleno.await();

			System.out.println("Ya se puede pasear");//que no se suba nadie

		} finally {
			l.unlock();
		}
	}
	
	public void finViaje () throws InterruptedException {
		//TODO
		l.lock();
		try {
			finDelPaseo = true;
			System.out.println("Fin del viaje, procedemos a desalojar");
			esperaBajar.signalAll();

			while (numClients > 0)
				esperaDesalojo.await();

			System.out.println("Ya está todo el mundo fuera");
			finDelPaseo = false;//Comienza otro ciclo
			this.colaEntrada.signalAll();
		}finally {
			l.unlock();
		}
	}
}
