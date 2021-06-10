package Examenes.Diciembre2017.Semaforos;

import java.util.concurrent.*;
public class Mesa {
	public static final int NUM_JUGADORES = 3;
	//0 - piedra, 1 - papel, 2 - tijeras
	private int jugadas[];
	private int numJugadores;
	private int numJugadas;
	private int ganador;


	private Semaphore mutex;
	private Semaphore esperaVeredicto;

	public Mesa()
	{
		numJugadas = 0;//Al crear la mesa, no hay ganadores
		numJugadores = NUM_JUGADORES;
		jugadas = new int[numJugadores];
		mutex = new Semaphore(1, true);
		esperaVeredicto = new Semaphore(0, true);
	}
	/**
	 * 
	 * @param jug jugador que llama al método (0,1,2)
	 * @param juego jugada del jugador (0-piedra,1-papel, 2-tijeras)
	 * @return  si ha habido un ganador en esta jugada se devuelve 
	 *          la jugada ganadora
	 *         o -1, si no ha habido ganador
	 * @throws InterruptedException
	 * 
	 * El jugador que llama a este método muestra su jugada, y espera a que
	 * est�n la de los otros dos. 
	 * Hay dos condiciones de sincronizaci�n
	 * CS1- Un jugador espera en el método hasta que est�n las tres jugadas
	 * CS2- Un jugador tiene que esperar a que finalice la jugada anterior para
	 *     empezar la siguiente
	 * 
	 */
	public int nuevaJugada(int jug,int juego) throws InterruptedException{
		ganador = -1;
		mutex.acquire();
		jugadas[jug] = juego;
		numJugadas++;
		System.out.println("El jugador "+jug+" juega "+juego);
		if (numJugadas == 3)
		{
			ganador = calcGanador(jugadas);
			numJugadas--;
			esperaVeredicto.release();
			mutex.release();
		}
		else
		{
			mutex.release();
			esperaVeredicto.acquire();
			numJugadas--;
			if (numJugadas > 0)
				esperaVeredicto.release();
			mutex.release();
		}

		return ganador;
	}

	private static int calcGanador(int[] jugadas) throws InterruptedException {
		int res = -1;
		//O es una opción ganadora, una de estas, o empate.
		if (jugadas[0] == jugadas[1] && jugadas[1] == 0 && jugadas[2] == 1) res = 1;
			else if (jugadas[0] == jugadas[1] && jugadas[1] == 1 && jugadas[2] == 2) res = 2;
				else if(jugadas[0] == jugadas[1] && jugadas[1] == 2 && jugadas[2] == 0) res = 0;


		return res;
	}
}
