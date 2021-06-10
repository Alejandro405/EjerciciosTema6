package Examenes.Septiembre2017.Locks;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cuerda {
	private static final int MAX_CAPACITY = 3;

	private int capacity;
	private int numBabuinosNS, numBabuinosSN;

	private boolean hayCuerdaNS, hayCuerdaSN;

	private Lock l;
	private Condition esperaCuerdaNS, esperaCuerdaSN, esperaLLeno;

	public Cuerda(){
		capacity = MAX_CAPACITY;
		numBabuinosNS = 0;
		numBabuinosSN = 0;

		hayCuerdaNS = true;
			hayCuerdaSN = false;

		l = new ReentrantLock(true);
		esperaCuerdaNS = l.newCondition();
		esperaCuerdaSN = l.newCondition();
		esperaLLeno = l.newCondition();
	}
	/**
	 * Utilizado por un babuino cuando quiere cruzar el cañón colgándose de la
	 * cuerda en dirección Norte-Sur
	 * Cuando el método termina, el babuino está en la cuerda y deben satisfacerse
	 * las dos condiciones de sincronización
	 * @param id del babuino que entra en la cuerda
	 * @throws InterruptedException
	 */
	public  void entraDireccionNS(int id) throws InterruptedException{
		l.lock();
		try {
			while (!hayCuerdaNS)
				esperaCuerdaNS.await();
			numBabuinosNS++;
			System.out.println("El mono "+id+" llega a la cuerda");
			if (numBabuinosNS == capacity){
				System.out.println("Ya está llena la cuerda vamos a cruzar");
				esperaLLeno.signalAll();
				Thread.sleep(100);
				System.out.println("Fin del cruce");
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * Utilizado por un babuino cuando quiere cruzar el cañón  colgándose de la
	 * cuerda en dirección Norte-Sur
	 * Cuando el método termina, el babuino está en la cuerda y deben satisfacerse
	 * las dos condiciones de sincronización
	 * @param id del babuino que entra en la cuerda
	 * @throws InterruptedException
	 */
	public  void entraDireccionSN(int id) throws InterruptedException{
		l.lock();
		try {
			while(!hayCuerdaSN)
				esperaCuerdaSN.await();
			numBabuinosSN++;
			System.out.println("\tEl monoSN "+id+" se sube a la cuerda");
			if (numBabuinosSN == 3){//A cruzar to-do el mundo
				System.out.println("\tYa está llena la cuerda vamos a cruzar");
				esperaLLeno.signalAll();
				Thread.sleep(100);
				System.out.println("\tFin del cruce");
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * Utilizado por un babuino que termina de cruzar por la cuerda en dirección Norte-Sur
	 * @param id del babuino que sale de la cuerda
	 * @throws InterruptedException
	 */
	public  void saleDireccionNS(int id) throws InterruptedException{
		l.lock();
		try {
			while(numBabuinosNS < capacity)
				esperaLLeno.signalAll();
			numBabuinosNS--;
			System.out.println("El mono con id "+id+" se baja de la cuerda");
			if (numBabuinosNS == 0){//Pasamos la cuerda al otro lado
				hayCuerdaSN = true;
				hayCuerdaNS = false;
				esperaCuerdaSN.signalAll();
			}

		} finally {
			l.unlock();
		}
	}
	
	/**
	 * Utilizado por un babuino que termina de cruzar por la cuerda en dirección Sur-Norte
	 * @param id del babuino que sale de la cuerda
	 * @throws InterruptedException
	 */
	public  void saleDireccionSN(int id) throws InterruptedException{
		l.lock();
		try {
			while (numBabuinosSN < capacity)
				esperaLLeno.await();
			numBabuinosSN--;
			if (numBabuinosSN == 0){
				hayCuerdaSN = false;
				hayCuerdaNS = true;
				esperaCuerdaNS.signalAll();
			}
		} finally {
			l.unlock();
		}
	}	
		
}
