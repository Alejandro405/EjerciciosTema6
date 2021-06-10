package Examenes.Septiembre2017.Semaforos;


import java.util.concurrent.Semaphore;

public class Cuerda {
	private static final int MAX_CAPACITY = 3;

	private int capacity;
	private int numBabuinosNS, numBabuinosSN;

	private Semaphore mutex;
	private Semaphore esperaCuerdaNS, esperaCuerdaSN;
	private Semaphore esperaLenarCuerda;

	public Cuerda(){
		capacity = MAX_CAPACITY;
		numBabuinosNS = 0;
		numBabuinosSN = 0;

		mutex = new Semaphore(1, true);
		esperaCuerdaNS = new Semaphore(1, true);
		esperaCuerdaSN = new Semaphore(0, true);
		esperaLenarCuerda = new Semaphore(0, true);
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
		esperaCuerdaNS.acquire();
		mutex.acquire();
		numBabuinosNS++;
		System.out.println("Llega el babuinoNS con id "+id);
		if (numBabuinosNS == capacity) { //Si lo lleanas cruza
			System.out.println("Ya están los monos listos");
			Thread.sleep(10);
			System.out.println("Ya hemos cruzado");
			this.esperaLenarCuerda.release();
		} else {
			esperaCuerdaNS.release();
		}

		mutex.release();
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
		esperaCuerdaSN.acquire();
		mutex.acquire();
		numBabuinosSN++;
		System.out.println("\tLlega el babuinoSN con id "+id);
		if (numBabuinosSN == capacity){
			System.out.println("\tYa están los monos listos");
			Thread.sleep(10);
			System.out.println("\tYa hemos cruzado");
			esperaLenarCuerda.release();
		} else {
			esperaCuerdaSN.release();
		}
		mutex.release();
	}
	/**
	 * Utilizado por un babuino que termina de cruzar por la cuerda en dirección Norte-Sur
	 * @param id del babuino que sale de la cuerda
	 * @throws InterruptedException
	 */
	public  void saleDireccionNS(int id) throws InterruptedException{
		esperaLenarCuerda.acquire();//Para que puedan bajar
		mutex.acquire();
		numBabuinosNS--;
		System.out.println("El monoNS con id "+id+" se ha bajado");
		if(numBabuinosNS == 0){//Ya han cruzado, pasamos la cuerda al otro lado, el último en cruzar
			esperaCuerdaSN.release();
		}
		esperaLenarCuerda.release();
		mutex.release();
	}
	
	/**
	 * Utilizado por un babuino que termina de cruzar por la cuerda en dirección Sur-Norte
	 * @param id del babuino que sale de la cuerda
	 * @throws InterruptedException
	 */
	public  void saleDireccionSN(int id) throws InterruptedException{
		esperaLenarCuerda.acquire();//Para que puedan bajar
		mutex.acquire();
		numBabuinosSN--;
		System.out.println("\tEl monoSN con id "+id+" se ha bajado");
		if(numBabuinosSN == 0){//Ya han cruzado, pasamos la cuerda al otro lado, el último en cruzar
			esperaCuerdaNS.release();
		}
		esperaLenarCuerda.release();
		mutex.release();
	}	
		
}
