package Examenes.Septiembre2018.Locks;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Parada {
	private int numPrimarios, numSecundarios;

	private boolean hayBus, saleBus, finTrayecto, estaLleno;

	private Lock l;
	private Condition esperaSecundario, esperaPrimario, esperaFinTrayecto;//Condiciones de sincronizacion de los alumnos
	private Condition esperaSuban;//Condicion para la sincronizacion del bus
	
	public Parada(){
		numPrimarios = 0;
		numSecundarios = 0;
		hayBus = false;
		saleBus = false;
		finTrayecto = false;
		estaLleno = false;

		l = new ReentrantLock(true);
		esperaSecundario = l.newCondition();
		esperaPrimario = l.newCondition();
		esperaSuban = l.newCondition();
		esperaFinTrayecto = l.newCondition();
	}
	/**
	 * El pasajero id llama a este metodo cuando LLEGA A LA PARADA.
	 * Siempre tiene que esperar el siguiente autobus en uno de los
	 * dos grupos de personas que hay en la parada
	 * El metodo devuelve el grupo en el que esta esperando el pasajero
	 * 
	 */
	public int esperoBus(int id) throws InterruptedException{
		int res = 1;
		l.lock();
		try {
			if (hayBus) {
				res = 2;
				numSecundarios++;
				System.out.println("\tEl estudiante con id "+id+" llega al grupo secundario. Hay "+numSecundarios+" en el secundario");
				while (!saleBus)
					esperaSecundario.await();
			}
			numPrimarios++;
			System.out.println("\tEl estudiante "+id+" llega al grupo primario. Hay "+numPrimarios+" en el primario");
			while (!hayBus)
				esperaPrimario.await();
			System.out.println("\t YA NOS VAMOS!!");
		} finally {
			l.unlock();
		}
		return res; //comentar esta línea
	}
	/**
	 * Una vez que ha llegado el autobús, el pasajero id que estaba
	 * esperando en el grupo i se sube al autobus
	 *
	 */
	public void subeAutobus(int id,int i) throws InterruptedException {
		l.lock();
		try {
			numPrimarios--;
			System.out.println("\tEl estudiante con id "+id+" que llego en el grupo "+i+" se monta al bus: quedan "+numPrimarios);
			if (numPrimarios == 0){//Si nos hemos montado todos, que slaga el bus, y que los que están en el primario avanzen al secundario
				esperaSuban.signalAll();
			}
			while (!finTrayecto)
				esperaFinTrayecto.await();
			System.out.println("\tEl estudiante con id "+id+" que llego en el grupo "+i+" se baja del bus");
		} finally {
			l.unlock();
		}
	}
	/**
	 * El autobus llama a este metodo cuando llega a la parada
	 * Espera a que se suban todos los viajeros que han llegado antes
	 * que el, y se va
	 * 
	 */
	public void llegoParada() throws InterruptedException{
		l.lock();
		try {
			System.out.println("YA LLEGO EL BUS");
			hayBus = true;
			saleBus = false;
			finTrayecto = false;
			esperaPrimario.signalAll();//Que comienzen a subir
			while (numPrimarios > 0)
				esperaSuban.await();
			this.saleBus = true;
			hayBus = false;
			esperaSecundario.signalAll();
			numSecundarios = 0;
			esperaSecundario.signalAll();
			System.out.println("COMIENZA EL VIAJE");
			Thread.sleep(100);

			finTrayecto = true;
			esperaFinTrayecto.signalAll();
			System.out.println("YA LLEGAMOS AL DESTINO");
		} finally {
			l.unlock();
		}
	}
}
