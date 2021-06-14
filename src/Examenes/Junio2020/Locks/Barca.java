package Examenes.Junio2020.Locks;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barca {
	private int numPasajeros, capacidad;
	private int posBarca;//0 - SUR; 1 - NORTE
	private boolean finTraecto;

	private Lock l;
	private Condition esperaSubirBarca, esperaBajarBarca;
	private Condition esperaLlenarBarca, esperaDesalojo;

	public Barca(){
		numPasajeros = 0;
		capacidad = 3;
		posBarca = 0;
		finTraecto = false;

		l = new ReentrantLock(true);
		esperaSubirBarca = l.newCondition();
		esperaBajarBarca = l.newCondition();
		esperaLlenarBarca = l.newCondition();
		esperaDesalojo = l.newCondition();
	}

	/*
	 * El Pasajero id quiere darse una vuelta en la barca desde la orilla pos
	 */
	public  void subir(int id,int pos) throws InterruptedException{
		//TODO
		l.lock();
		try {
			while (pos != posBarca || numPasajeros >= capacidad)//No tiene en cuenta el trayecto, por eso se suben mientras se entán bajando
				esperaSubirBarca.await();
			numPasajeros++;
			System.out.println("\tEl pasajero de id "+id+" sube a la barca. Hay "+numPasajeros+" posicion de la barca "+posBarca);
			if (numPasajeros == capacidad && posBarca == pos) {//llama al capitan para que cruze
				esperaLlenarBarca.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
	
	/*
	 * Cuando el viaje ha terminado, el Pasajero que esta en la barca se baja
	 */
	public  int bajar(int id) throws InterruptedException{
		//TODO
		l.lock();
		try {
			while (!finTraecto)
				esperaBajarBarca.await();
			numPasajeros--;
			System.out.println("\tEl pasajero de id "+id+" se baja de la barca. Hay "+numPasajeros+", posicion de la barca "+posBarca);
			if (numPasajeros == 0) {
				esperaDesalojo.signalAll();
			}
		} finally {
			l.unlock();
		}
		return 0;
	}
	/*
	 * El Capitan espera hasta que se suben 3 pasajeros para comenzar el viaje
	 */
	public  void esperoSuban() throws InterruptedException{
		//TODO
		l.lock();
		try {
			while (numPasajeros < capacidad)
				esperaLlenarBarca.await();
			System.out.println("YA SE PUEDE CRUZAR");
		} finally {
			l.unlock();
		}
	}
	/*
	 * El Capitan indica a los pasajeros que el viaje ha terminado y tienen que bajarse
	 */
	public  void finViaje() throws InterruptedException{
		//TODO
		l.lock();
		try {
			finTraecto = true;//Permito que bajen
			System.out.println("YA HEMOS CRUZADO");
			esperaBajarBarca.signalAll();
			while (numPasajeros > 0)
				esperaDesalojo.await();
			posBarca = posBarca == 0 ? 1 : 0;//Cambio la barca de orilla

		} finally {
			l.unlock();
		}
	}

}
