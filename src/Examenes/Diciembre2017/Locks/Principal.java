package Examenes.Diciembre2017.Locks;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Mesa mesa = new Mesa();
		
		Jugador[] jug = new Jugador[3];
		for (int i = 0; i< jug.length; i++){
			jug[i] = new Jugador(i,mesa);
		}
		for (int i = 0; i< jug.length; i++){
			jug[i].start();
		}
		for (int i = 0; i< jug.length; i++){
			try {
				jug[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("fin del programa");
	}

}
