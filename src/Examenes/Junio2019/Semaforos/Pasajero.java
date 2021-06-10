package Examenes.Junio2019.Semaforos;

import java.util.Random;

public class Pasajero extends Thread {

	private int id;
	private Tiovivo t;
	private Random r;
	
	public Pasajero(int id, Tiovivo t)
	{
		this.id = id;
		this.t = t;
		r = new Random();
	}
	
	public void run()
	{
		int i = 0;
		boolean fin = false;
		while(!this.isInterrupted() && i < 1 && !fin)
		{
			try {
			Thread.sleep(10+r.nextInt(50));	
			t.subir(id);
			t.bajar(id);
				i++;
			}catch(InterruptedException e)
			{
				fin = true;
			}

		}
	}
}
