package Examenes.Junio2017.Semaforos;


import java.util.concurrent.Semaphore;

public class Guarderia {
	private int numBebes, numAdultos;

	private Semaphore mutex;
	private Semaphore esperaEntrarBebe, esperaSalirAdulto;

	public Guarderia()
	{
		numAdultos = 0;
		numBebes = 0;

		mutex = new Semaphore(1, true);
		esperaSalirAdulto = new Semaphore(0, true);//Necesita al menos 1 adulto
		esperaEntrarBebe = new Semaphore(0, true);//Necesita al menos 1 bebe para cuidar
	}

	/**
	 * Un bebe que quiere entrar en la guarderia llama a este metodo.
	 * Debe esperar hasta que sea seguro entrar, es decir, hasta que 
	 * cuado entre haya, al menos, 1 adulto por cada 3 bebes
	 * 
	 */
	public void entraBebe(int id) throws InterruptedException{
		while (numBebes + 1 > 3 * numAdultos)
			esperaEntrarBebe.acquire();//Entrarán de 1 en 1
		mutex.acquire();
		numBebes++;
		System.out.println("\tEl bebe "+id+" acaba de ENTRAR");
		if (numBebes > 3 * numAdultos)
			esperaEntrarBebe.release();
		mutex.release();
	}
	/**
	 * Un bebe que quiere irse de la guarderia llama a este metodo * 
	 */
	public void saleBebe(int id) throws InterruptedException{
		mutex.acquire();
		numBebes--;
		System.out.println("El bebe "+id+" acaba de SALIER. "+toString());
		esperaSalirAdulto.release();//Tiene que estar dentro de un bucle
		mutex.release();
	}
	/**
	 * Un adulto que quiere entrar en la guarderia llama a este metodo * 
	 */
	public void entraAdulto(int id) throws InterruptedException{
		mutex.acquire();
		numAdultos++;
		System.out.println("El adulto "+id+" acaba de entrar a guardería"+toString());
		esperaEntrarBebe.release();
		mutex.release();
	}
	
	/**
	 * Un adulto que quiere irse  de la guarderia llama a este metodo.
	 * Debe esperar hasta que sea seguro salir, es decir, hasta que
	 * cuando se vaya haya, al menos, 1 adulto por cada 3 bebes
	 * 
	 */
	public void saleAdulto(int id) throws InterruptedException{
		while (numBebes > 3 * (numAdultos - 1))
			esperaSalirAdulto.acquire();
		mutex.acquire();
		System.out.println("El dulto "+ id +" acaba de salir");
		numAdultos--;
		if (numBebes <= 3 * numAdultos)
			esperaSalirAdulto.release();
		mutex.release();
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
