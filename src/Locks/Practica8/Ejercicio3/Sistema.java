package Locks.Practica8.Ejercicio3;

public class Sistema {

	public static void main(String[] args) {
		final int impA = 3, impB = 2; 
		Gestor gestor = new Gestor(impA, impB);
		
		Documento[] documentosA = new Documento[20];
		Documento[] documentosB = new Documento[20];
		Documento[] documentosAB = new Documento[20];
		for(int i=0; i<20; i++) {
			documentosA[i] = new Documento(i, gestor, 0);
			documentosB[i] = new Documento(i, gestor, 1);
			documentosAB[i] = new Documento(i, gestor, 2);
		}
		for(int i=0; i<20; i++) {
			documentosA[i].start();
			documentosB[i].start();
			documentosAB[i].start();
		}

	}

}
