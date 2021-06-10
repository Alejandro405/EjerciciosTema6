package Examenes.Junio2018.Semaforos;


import java.util.concurrent.Semaphore;

public class GestorAgua {

	private int numOxígeno;
	private int numHidrógeno;

	private Semaphore esperaPlazaOxigeno, esperaReactivosOxígeno;
	private Semaphore esperaPlazaHidrogeno, esperaReactivosHidrógeno;
	private Semaphore mutex;

	public GestorAgua()
	{
		numHidrógeno = 0;
		numOxígeno = 0;

		mutex = new Semaphore(1, true);
		esperaPlazaOxigeno = new Semaphore(1, true);
		esperaPlazaHidrogeno = new Semaphore(1, true);
		esperaReactivosHidrógeno = new Semaphore(0, true);
		esperaReactivosOxígeno = new Semaphore(0, true);
	}

	public void hListo(int id) throws InterruptedException{
		esperaPlazaHidrogeno.acquire();
		mutex.acquire();
		numHidrógeno++;
		System.out.println("Ha llegado el hidrógeno con id: "+id);
		if (numHidrógeno < 2) {
			esperaPlazaHidrogeno.release();
		}
		if (reaccionLista(numHidrógeno, numOxígeno)) {
			esperaReactivosHidrógeno.release();
			esperaReactivosOxígeno.release();
		}
		mutex.release();

		//Ya hemos entrado al gestor falta esperar a que se haga la reacción
			esperaReactivosHidrógeno.acquire();
			mutex.acquire();
			System.out.println("El átomo de hidrógeno con id: "+id+", ha reaccionado");
			numHidrógeno--;
			if (numHidrógeno > 0)
				esperaReactivosHidrógeno.release();
			if (numHidrógeno == 0)
				this.esperaPlazaHidrogeno.release();
			mutex.release();
	}
	
	public void oListo(int id) throws InterruptedException {
		esperaPlazaOxigeno.acquire();
		mutex.acquire();
		numOxígeno++;
		System.out.println("Lleaga oxígeno con id: " + id);
		if (numOxígeno < 1){
			esperaPlazaOxigeno.release();
		}
		if (reaccionLista(numHidrógeno, numOxígeno)) {
			System.out.println("Ya hay reactivos, se puede producir");
			esperaReactivosHidrógeno.release();
		}
		mutex.release();
		//Ya ha entrado; el código siguiente muestra la salida
			esperaReactivosOxígeno.acquire();
			mutex.acquire();

			System.out.println("\tHay reactivos suficientes, procedemos a hacer agua");
			System.out.println("El átomo de Oxígeno con id: "+id+", ha reaccionado");

			numOxígeno--;
			mutex.release();
			esperaPlazaOxigeno.release();
	}


	private static boolean reaccionLista(int numH, int numO)
	{
		return numH == 2 && numO == 1;
	}
}