package Examenes.Junio2018.Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mesa {
	private static final int MAX_PIZZA = 10;
	private int numPizza;

	private boolean hanLlamado, pagado;
	
	private Lock l;
	private Condition esperaReponer, esperaRepartidor;
	private Condition esperaPedido, esperaPago;
	private boolean llegaRepartidor;

	public Mesa()
	{
		numPizza = MAX_PIZZA;

		hanLlamado = false;
			pagado = false;
				llegaRepartidor = false;

		l = new ReentrantLock(true);
		esperaPago = l.newCondition();
		esperaPedido = l.newCondition();
		esperaRepartidor = l.newCondition();
		esperaReponer = l.newCondition();
	}

	/**
	 * COGER PIZZA
	 * @param id
	 * El estudiante id quiere una ración de pizza. 
	 * Si hay una ración la coge y sigue estudiante.
	 * Si no hay y es el primero que se da cuenta de que la mesa está vacía
	 * llama al pizzero y
	 * espera hasta que traiga una nueva pizza. Cuando el pizzero trae la pizza
	 * espera hasta que el estudiante que le ha llamado le pague.
	 * Si no hay pizza y no es el primer que se da cuenta de que la mesa está vacía
	 * espera hasta que haya un trozo para él.
	 * @throws InterruptedException 
	 * 
	 */
	public void nuevaRacion(int id) throws InterruptedException{
		l.lock();
		try {
			while (numPizza == 0)
				esperaReponer.await();
			numPizza--;
			System.out.println("Estudiante con id: "+id+". Coge pizza");
			//En caso de que deje la mesa sin pizza llama al pizzero ==> función extra
				if (numPizza == 0){
					System.out.println("NO HAY PIZZA, toca llamar, el estudiate "+id+" invita");
					hanLlamado = true;
					while (!this.llegaRepartidor)
						esperaRepartidor.await();

					System.out.println("ya llego el repartidor, lepaga el estudiate: "+id);
					numPizza = MAX_PIZZA;
					pagado = true;
					hanLlamado = false;
					esperaPago.signalAll();
					esperaReponer.signalAll();
				}
		} finally {
			l.unlock();
		}
	}


	/**
	 * El pizzero entrega la pizza y espera hasta que le paguen para irse
	 * @throws InterruptedException 
	 */
	public void nuevoCliente() throws InterruptedException{
		l.lock();
		try {
			System.out.println("\t\t Ya ha llegado el tio");
			llegaRepartidor = true;
			esperaRepartidor.signalAll();
			while (!pagado)
				esperaPago.await();
			System.out.println("Pedido pagado");
		} finally {
			l.unlock();
		}
	}

/**
	 * El pizzero espera hasta que algún cliente le llama para hacer una pizza y
	 * llevársela a domicilio
	 * @throws InterruptedException 
	 */
	public void nuevaPizza() throws InterruptedException{
		l.lock();
		try {
			while (!hanLlamado)
				esperaPedido.await();
			System.out.println("\t\t PEDIDO NUEVOOO; VOY VOLANDO");

		} finally {
			l.unlock();
		}
	}
}
