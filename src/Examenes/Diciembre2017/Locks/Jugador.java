package Examenes.Diciembre2017.Locks;

import java.util.Random;

public class Jugador extends Thread{
	private static Random r = new Random();
	private int id;
	private Mesa mesa;
	public Jugador(int id, Mesa mesa){
		this.id = id;
		this.mesa = mesa;
	}
	
	public void run(){
	    int aux = 0,ganador;
	    try{
			do{
				aux = r.nextInt(3);
			
				ganador = mesa.nuevaJugada(id, aux);
			} while (ganador == -1);
			if (aux == ganador) 
				System.out.println("El ganador he sido yo "+id);
			else 
				System.out.println("He perdido "+id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
