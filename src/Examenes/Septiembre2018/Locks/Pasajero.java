package Examenes.Septiembre2018.Locks;

import java.util.Random;

public class Pasajero extends Thread{
	private int id;
	private Parada parada;
	private static Random r = new Random();
	public Pasajero(Parada parada,int id){
		this.id = id;
		this.parada = parada;
	}
	
	public void run(){
		int j = 0;
		while(j < 1){
			try {
				Thread.sleep(r.nextInt(3000));
				int i=parada.esperoBus(id);
				Thread.sleep(r.nextInt(500));
				parada.subeAutobus(id, i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j++;
		}
	}

}
