package Examenes.Junio2016.Locks;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Aseos {
	private int numClientes;
	private boolean tocaLimpiar;

	private Lock l;
	private Condition esperaEntrarCliente, esperaEntrarLimpieza;

	public Aseos()
	{
		numClientes = 0;
		tocaLimpiar = false;

		l = new ReentrantLock(true);
		esperaEntrarCliente = l.newCondition();
		esperaEntrarLimpieza = l.newCondition();
	}
	
	/**
	 * Utilizado por el cliente id cuando quiere entrar en los aseos
	 * CS Version injusta: El cliente espera si el equipo de limpieza est� trabajando
	 * CS Version justa: El cliente espera si el equipo de limpieza est� trabajando o
	 * est� esperando para poder limpiar los aseos
	 * 
	 */
	public void entroAseo(int id) throws InterruptedException {
		l.lock();
		try{
			while(tocaLimpiar)
				esperaEntrarCliente.await();
			numClientes++;
			System.out.println("El cliente con id "+id+" entra al aseo. "+toString());
			if (!tocaLimpiar)
				esperaEntrarCliente.signalAll();
		} finally {
			l.unlock();
		}
	}

	/**
	 * Utilizado por el cliente id cuando sale de los aseos
	 * 
	 */
	public void salgoAseo(int id){
		l.lock();
		try{
			numClientes--;
			System.out.println("El cliente con id "+id+" salir del aseo. "+toString());
			if (tocaLimpiar && numClientes == 0)
				esperaEntrarLimpieza.signalAll();
		} finally {
			l.unlock();
		}
	}
	
	/**
	 * Utilizado por el Equipo de Limpieza cuando quiere entrar en los aseos 
	 * CS: El equipo de trabajo est� solo en los aseos, es decir, espera hasta que no
	 * haya ning�n cliente.
	 * 
	 */
    public void entraEquipoLimpieza() throws InterruptedException {
		l.lock();
		try{
			tocaLimpiar = true;
			while (numClientes > 0)
				esperaEntrarLimpieza.await();
			System.out.println("\tEl equipo de limpieza entra");
		} finally {
			l.unlock();
		}
	}
    
    /**
	 * Utilizado por el Equipo de Limpieza cuando  sale de los aseos 
	 * 
	 * 
	 */
    public void saleEquipoLimpieza() throws InterruptedException {
		l.lock();
		try{
			System.out.println("\tEl equipo de limpieza ha terminado de limpiar");
			tocaLimpiar = false;
			esperaEntrarCliente.signalAll();
			Thread.sleep(100);
		} finally {
			l.unlock();
		}
	}

	public String toString(){
    	return "ESTADO: "+numClientes;
	}
}
