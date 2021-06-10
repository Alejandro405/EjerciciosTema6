package Examenes.Junio2019;

import Examenes.Junio2019.Semaforos.Operario;
import Examenes.Junio2019.Semaforos.Pasajero;
import Examenes.Junio2019.Semaforos.Tiovivo;

public class Principal {

	public static void main(String[] args)
	{
		Tiovivo t = new Tiovivo(5);
		Operario o = new Operario(t);
		Pasajero[] personas = new Pasajero[7];
		for(int i =0; i<7; i++)
		{
			personas[i] = new Pasajero(i,t);
		}
		
		
		for(int i =0; i<7; i++)
		{
			personas[i].start();
		}
		o.start();
		
		
	}
}
