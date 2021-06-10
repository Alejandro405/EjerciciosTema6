package Examenes.Septiembre2019.Locks;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Concurso {
	private int numTiradasA;
	private int numTiradasB;

	private int numCarasA;
	private int numCarasB;


	private Lock l;
	private Condition esperaVeredicto;

	public Concurso() {
		numTiradasA =  0;
			numTiradasB = 0;

		numCarasA = 0;
			numCarasB = 0;

		l = new ReentrantLock(true);
		esperaVeredicto = l.newCondition();
	}

	public void tirarMoneda(int id,boolean cara) throws InterruptedException {
		l.lock();
		try {
			if (id == 0){
				while (numTiradasA == 10)
					esperaVeredicto.await();

				numCarasA = cara ? numCarasA + 1 : numCarasA;
				numTiradasA++;
				System.out.println("\tEl jugador A ha tirado, numero de caras es: "+numCarasA+". TOTAL: "+numTiradasA);
			} else if (id == 1) {
				while (numTiradasB == 10)
					esperaVeredicto.await();

				numCarasB = cara ? numCarasB + 1 : numCarasB;
				numTiradasB++;

				System.out.println("El jugador B ha tirado, numero de caras es: "+numCarasB+". TOTAL: "+numTiradasB);
			}

			if (concursoTerminado()) {
				mostrarVeredito(numCarasB, numCarasA);
				numCarasA = 0;
				numCarasB = 0;
				numTiradasA = 0;
				numTiradasB = 0;
				esperaVeredicto.signalAll();
			}
		} finally {
			l.unlock();
		}
	}

	private static void mostrarVeredito(int numCarasB, int numCarasA) {
		String res = "\nEl juego ha terminado. EL GANADOR ES: ";

		if (numCarasA > numCarasB){
			res += "El jugador A de id: 0";
		} else if (numCarasA == numCarasB) {
			res += "NINGUNO hay empate";
		} else {
			res += "El jugador B de id: 1";
		}

		System.out.println(res+"\n");
	}

	public boolean concursoTerminado() {

		return numTiradasA + numTiradasB == 20; //borrar
	}
}