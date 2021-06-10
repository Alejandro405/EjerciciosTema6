package Examenes.Septiembre2019.Semáforos;


import java.util.concurrent.Semaphore;

public class Concurso {
	private int numTiradasA;
	private int numTiradasB;

	private int numCarasA;
	private int numCarasB;

	//private Semaphore esperaVeredito;
	private Semaphore mutex;

	public Concurso()
	{
		numTiradasA = 0;
		numTiradasB = 0;
		numCarasA = 0;
		numCarasB = 0;

		//esperaVeredito = new Semaphore(0);
		mutex = new Semaphore(1);
	}
	
	public void tirarMoneda(int id,boolean cara) throws InterruptedException {
		mutex.acquire();
		if (id == 0 && numTiradasA < 10 ){
			numCarasA = cara ? numCarasA + 1 : numCarasA;
			numTiradasA++;

			System.out.println("\tEl jugador A ha tirado, numero de caras es: "+numCarasA+". TOTAL: "+numTiradasA);
		} else if (id == 1 && numTiradasB < 10){
			numCarasB = cara ? numCarasB + 1 : numCarasB;
			numTiradasB++;

			System.out.println("El jugador B ha tirado, numero de caras es: "+numCarasB+". TOTAL: "+numTiradasB);
		}

		if (concursoTerminado()) {
			muestraResultados(numCarasA, numCarasB);
			numCarasA = 0;
			numCarasB = 0;
			numTiradasA = 0;
			numTiradasB = 0;
		}
		mutex.release();
	}

	private static  void muestraResultados(int numCarasA, int numCarasB) {
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