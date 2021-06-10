package Examenes.Septiembre2018.Semaforos;


import java.util.concurrent.Semaphore;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Aseo {
	private static final int MAX_ENTRADAS = 25;

	private boolean quiereEntrarMujer, quiereEntrarHombre;
	private int numHombres, numMujeres;

	private Semaphore mutex;
	private Semaphore entraAseoHombre, entraAseoMujer;

	public Aseo() {
		numHombres = 0 ;
		numMujeres = 0;
		quiereEntrarMujer = false;
		quiereEntrarHombre = false;

		mutex = new Semaphore(1, true);
		entraAseoHombre = new Semaphore(1, true);
		entraAseoMujer = new Semaphore(1, true);

	}

	
	/**
	 * El hombre id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay alguna mujer en ese
	 * momento en el aseo
	 */
	public void llegaHombre(int id) throws InterruptedException{
		quiereEntrarHombre = true;
		while (quiereEntrarMujer)
			entraAseoHombre.acquire();
		mutex.acquire();
		numHombres++;
		System.out.println("Entra al aseo cliente hombre: "+id+". "+this.toString());
		if (!quiereEntrarMujer)
			entraAseoHombre.release();
		mutex.release();
	}
	/**
	 * La mujer id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay algun hombre en ese
	 * momento en el aseo
	 */
	public void llegaMujer(int id) throws InterruptedException{
		quiereEntrarMujer = true;
		while (quiereEntrarHombre)
			entraAseoMujer.acquire();
		mutex.acquire();
		numMujeres++;
		System.out.println("Entra al aseo cliente mujer: "+id+". "+this.toString());
		if (!quiereEntrarHombre)
			entraAseoMujer.release();
		mutex.release();
	}
	/**
	 * El hombre id, que estaba en el aseo, sale
	 */
	public void saleHombre(int id)throws InterruptedException{
		mutex.acquire();
		numHombres--;
		System.out.println("\tSale del aseo el hombre con id "+id+". "+this.toString());
		if (numHombres == 0){
			entraAseoMujer.release();
			quiereEntrarHombre = false;
		}
		mutex.release();
	}
	/**
	 * La mujer id, que estaba en el aseo, sale
	 */
	public void saleMujer(int id)throws InterruptedException{
		mutex.acquire();
		numMujeres--;
		System.out.println("\tSale del aseo la mujer con id "+id+". "+this.toString());
		if (numMujeres == 0){
			entraAseoHombre.release();
			quiereEntrarMujer = false;
		}
		mutex.release();
	}

	public String toString(){
		return "Hay "+numHombres+" hombres, y "+numMujeres+" mujeres";
	}
}
