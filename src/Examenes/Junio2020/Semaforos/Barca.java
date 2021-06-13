package Examenes.Junio2020.Semaforos;

import java.util.concurrent.Semaphore;

public class Barca {
	private int numPasajeros, capacidad;
	private int posBarca;//0 - SUR; 1 - NORTE
	private boolean finTraecto;

	private Semaphore mutex;
	private Semaphore esperaSubirBarca, esperaBajarBarca;
	private Semaphore esperaLlenarBarca, esperaDesalojo;

	public Barca(){
		numPasajeros = 0;
		capacidad = 3;
		posBarca = 0;
		finTraecto = false;

		mutex = new Semaphore(1, true);
		esperaSubirBarca = new Semaphore(1, true);
		esperaBajarBarca = new Semaphore(0, true);
		esperaLlenarBarca = new Semaphore(0, true);
		esperaDesalojo = new Semaphore(0, true);
	}

	/*
	 * El Pasajero id quiere darse una vuelta en la barca desde la orilla pos
	 */
	public  void subir(int id,int pos) throws InterruptedException{
		//Si no está en mi orilla o no hay asiento libre, ESPERO
		while (posBarca != pos || numPasajeros == capacidad)
			esperaSubirBarca.acquire();
		mutex.acquire();
		numPasajeros++;
		System.out.println("\tEl pasajero de id "+id+" sube a la barca. Hay "+numPasajeros);
		if (numPasajeros < capacidad){
			esperaSubirBarca.release();
		} else if ( numPasajeros == capacidad){
			esperaLlenarBarca.release();
		}
		mutex.release();
	}
	
	/*
	 * Cuando el viaje ha terminado, el Pasajero que esta en la barca se baja
	 */
	public int bajar(int id) throws InterruptedException{
		//TODO
		while (!finTraecto)
			esperaBajarBarca.acquire();
		mutex.acquire();
		numPasajeros--;
		System.out.println("\tEl pasajero de id "+id+" se baja de la barca. Hay "+numPasajeros+", posicion de la barca "+posBarca);
		finTraecto = true;
		esperaBajarBarca.release();
		if (numPasajeros == 0) {
			esperaDesalojo.release();
		}
		return posBarca;
	}
	/*
	 * El Capitan espera hasta que se suben 3 pasajeros para comenzar el viaje
	 */
	public  void esperoSuban() throws InterruptedException{
		//TODO
		while (numPasajeros < capacidad)
			esperaLlenarBarca.acquire();
		System.out.println("YA HAY 3 PASAJEROS, PUEDE COMENZAR EL VIAJE");
	}
	/*
	 * El Capitan indica a los pasajeros que el viaje ha terminado y tienen que bajarse
	 */
	public  void finViaje() throws InterruptedException{
		//TODO
		System.out.println("FIN DEL VIAJE");
		mutex.acquire();
		finTraecto = true;
		mutex.release();
		esperaBajarBarca.release();
		while (numPasajeros > 0)
			esperaDesalojo.acquire();
		mutex.acquire();
		System.out.println("YA HAN CRUZADO TODOS");
		if (posBarca == 0)
			posBarca = 1;
		else
			posBarca = 0;

		mutex.release();
	}

}
