package Examenes.Junio2017.Locks;


import java.time.LocalDate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Guarderia {

	private int numBebes;
	private int numAdultos;
	//private static boolean condicIntegridad = numBebes <= (3 * numAdultos);
	private Lock l;
	private Condition esperaEntrarBebe, esperaSalirAdulto;

	public Guarderia(){
		numAdultos = 0;
		numBebes = 0;

		l = new ReentrantLock(true);
		esperaEntrarBebe = l.newCondition();
		esperaSalirAdulto = l.newCondition();
	}

	/**
	 * Un bebe que quiere entrar en la guarderia llama a este metodo.
	 * Debe esperar hasta que sea seguro entrar, es decir, hasta que 
	 * cuado entre haya, al menos, 1 adulto por cada 3 bebes
	 * 
	 */
	public void entraBebe(int id) throws InterruptedException{
		l.lock();
		try {
			while (numBebes + 1 > numAdultos * 3)
				esperaEntrarBebe.await();
			numBebes++;
			System.out.println("\tEl bebe "+id+" acaba de ENTRAR. "+toString());
			if (esValido().compareTo("VALIDO") == 0)
				esperaEntrarBebe.signalAll();
		} finally {
			l.unlock();
		}
	}

	/**
	 * Un bebe que quiere irse de la guarderia llama a este metodo * 
	 */
	public void saleBebe(int id) throws InterruptedException{
		l.lock();
		try {
			numBebes--;
			System.out.println("El bebe "+id+" acaba de SALIER. "+toString());
			esperaEntrarBebe.signalAll();
		} finally {
			l.unlock();
		}
	}

	/**
	 * Un adulto que quiere entrar en la guarderia llama a este metodo * 
	 */
	public void entraAdulto(int id) throws InterruptedException{
		l.lock();
		try {
			numAdultos++;
			System.out.println("El adulto "+id+" acaba de entrar a guardería"+toString());
			esperaSalirAdulto.signalAll();
		} finally {
			l.unlock();
		}
	}
	
	/**
	 * Un adulto que quiere irse  de la guarderia llama a este metodo.
	 * Debe esperar hasta que sea seguro salir, es decir, hasta que
	 * cuando se vaya haya, al menos, 1 adulto por cada 3 bebes
	 * 
	 */
	public void saleAdulto(int id) throws InterruptedException{
		l.lock();
		try {
			while (numBebes > 3 * (numAdultos - 1))
				esperaSalirAdulto.await();
			numAdultos--;
			System.out.println("El dulto "+ id +" acaba de salir. "+toString());
			if (esValido().compareTo("VALIDO") == 0)
				esperaSalirAdulto.signalAll();
		} finally {
			l.unlock();
		}
	}

	public String toString()
	{
		return "ESTADO: "+numAdultos+"adultos, "+numBebes+" bebes; "+esValido();
	}

	private String esValido() {
		String res = "";
		if (numBebes <= 3 * numAdultos)
			res = "VALIDO";
		else
			res = "INCONSISTENTE";
		return res;
	}


}
