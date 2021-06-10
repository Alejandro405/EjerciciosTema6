package Examenes.Diciembre2017.Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mesa {
	public static final int NUM_JUGADORES = 3;
	//0 - piedra, 1 - papel, 2 - tijeras
	private int jugadas[];
	private int numJugadas;
	private int ganador;

	private Lock l;
	private Condition esperaVeredicto;
	private boolean hayVeredicto;
	private Condition esperaJugar;
	private boolean finPartida;

	public Mesa()
	{
		numJugadas = 0;
		jugadas = new int[NUM_JUGADORES];
		ganador = -1;
		hayVeredicto = false;


		l = new ReentrantLock(true);
		esperaVeredicto = l.newCondition();
		esperaJugar = l.newCondition();
		finPartida = true;
	}

	/**
	 * 
	 * @param jug jugador que llama al m�todo (0,1,2)
	 * @param juego jugada del jugador (0-piedra,1-papel, 2-tijeras)
	 * @return  si ha habido un ganador en esta jugada se devuelve 
	 *          la jugada ganadora
	 *         o -1, si no ha habido ganador
	 * @throws InterruptedException
	 * 
	 * El jugador que llama a este m�todo muestra su jugada, y espera a que 
	 * est�n la de los otros dos. 
	 * Hay dos condiciones de sincronizaci�n
	 * CS1- Un jugador espera en el m�todo hasta que est�n las tres jugadas
	 * CS2- Un jugador tiene que esperar a que finalice la jugada anterior para
	 *     empezar la siguiente
	 * 
	 */
	public int nuevaJugada(int jug,int juego) throws InterruptedException{
		l.lock();
		try {

			//System.out.println("El jugador "+jug+" llega a la mesa");
			jugadas[juego] = juego;
			numJugadas++;
			System.out.println("El jugador "+jug+" juega "+juego);
			if (numJugadas == 3)
			{
				ganador = calcGanador(jugadas);
				muestraVeredicto(jug, jugadas, ganador);
				numJugadas--;
				hayVeredicto = true;
				esperaVeredicto.signalAll();
			}
			else
			{
				while (!hayVeredicto)
					esperaVeredicto.await();
				muestraVeredicto(jug, jugadas, ganador);
				numJugadas--;
				if (numJugadas == 0){//REiniciar la mesa
					hayVeredicto = false;
					esperaJugar.signalAll();
					ganador = -1;
				}
			}
		} finally {
			l.unlock();
		}

		return ganador;
	}

	private void muestraVeredicto(int jug, int[] juego, int ganador) {
		if (ganador == jug)
			System.out.println("\tEL JUGADOR "+jug+" HA GANDO con "+jugadas[jug]+"!!!!!");
		else if (ganador == -1)
			System.out.println("\tEMPATEEEE!!!");
		else
			System.out.println("\tEL JUGADOR "+jug+" HA PERDIDO 😥 con "+jugadas[jug]+"!!!!!");

	}

	private static int calcGanador(int[] jugadas) throws InterruptedException {
		int res = -1;
		if (jugadas[0] == jugadas[1] && jugadas[1] == 0 && jugadas[2] == 1) res = 1;
		else if (jugadas[0] == jugadas[1] && jugadas[1] == 1 && jugadas[2] == 2) res = 2;
		else if(jugadas[0] == jugadas[1] && jugadas[1] == 2 && jugadas[2] == 0) res = 0;
		return res;
	}
}
