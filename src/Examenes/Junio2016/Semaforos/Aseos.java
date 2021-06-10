package Examenes.Junio2016.Semaforos;


import java.util.concurrent.Semaphore;

public class Aseos {
	private int numClientes;
	private boolean tocaLimpiar;

	private Semaphore mutex;
	private Semaphore esperaEntrarCliente, esperaEntrarLimpieza;

	public Aseos()
	{
		numClientes = 0;
		tocaLimpiar = false;
		mutex = new Semaphore(1, true);
		esperaEntrarCliente = new Semaphore(1, true);
		esperaEntrarLimpieza = new Semaphore(1, true);
	}
	
	/**
	 * Utilizado por el cliente id cuando quiere entrar en los aseos
	 * CS Version injusta: El cliente espera si el equipo de limpieza est� trabajando
	 * CS Version justa: El cliente espera si el equipo de limpieza est� trabajando o
	 * est� esperando para poder limpiar los aseos
	 * 
	 */
	public void entroAseo(int id) throws InterruptedException {
		esperaEntrarCliente.acquire();
		mutex.acquire();
		numClientes++;
		System.out.println("\tEl cliente con id "+id+" entra al aseo. "+toString());
		mutex.release();
		if (!tocaLimpiar)
			esperaEntrarCliente.release();
	}

	/**
	 * Utilizado por el cliente id cuando sale de los aseos
	 * 
	 */
	public void salgoAseo(int id) throws InterruptedException {
		mutex.acquire();
		numClientes--;
		System.out.println("\tEl cliente con id "+id+" sale del aseo. "+toString());
		mutex.release();
	}
	
	/**
	 * Utilizado por el Equipo de Limpieza cuando quiere entrar en los aseos 
	 * CS: El equipo de trabajo est� solo en los aseos, es decir, espera hasta que no
	 * haya ning�n cliente.
	 * 
	 */
    public void entraEquipoLimpieza() throws InterruptedException {
		System.out.println("\nTOCA LIMPIAR");
    	tocaLimpiar = true;
		esperaEntrarLimpieza.acquire();
		System.out.println("El equipo de limpieza entra al baño");
	}
    
    /**
	 * Utilizado por el Equipo de Limpieza cuando  sale de los aseos 
	 * 
	 * 
	 */
    public void saleEquipoLimpieza() throws InterruptedException {
		System.out.println("Ya hemos terminado de limpiar");
		tocaLimpiar = false;
		esperaEntrarLimpieza.release();
		esperaEntrarCliente.release();
		Thread.sleep(1000);
	}

	public String toString()
	{
		return "ESTADO: "+numClientes;
	}
}
