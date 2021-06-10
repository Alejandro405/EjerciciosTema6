package Locks.Practica8.Ejercicio3;

import java.util.Random;

public class Documento extends Thread {
	private int id;
	private Gestor gestor;
	private int tipo;
	
	
	public Documento(int id, Gestor gestor, int tipo) {
		this.id = id;
		this.gestor = gestor;
		this.tipo = tipo;
		//0: tipo A, 1; tipo B, 2: tipo AB
	}
	
	public void run()
	{
			try {
				int impAsignada;
				if(tipo==0)
				{
					gestor.imprimirA(id);
					impAsignada = tipo;
				}else if(tipo== 1)
				{
					gestor.imprimirB(id);
					impAsignada = tipo;
				}else {
					impAsignada = gestor.imprimirAB(id);	
				}
				System.out.println("Documento "+id+" se imprime en "+impAsignada);
				Thread.sleep(200);
				System.out.println("Documento "+id+" termina de imprimirse en "+impAsignada);
				gestor.devolverImpresora(id, impAsignada);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}	
	}

}
