import Locks.Practica8.Ejercicio2.GestorAgua;
import Locks.Practica8.Ejercicio2.Hidrogeno;
import Locks.Practica8.Ejercicio2.Oxigeno;

public class demo {
    public static void main(String[] args) throws InterruptedException {
       /** Coche bagon = new Coche(7, 10);
        System.out.println("Bagon creado");
        Pasajero pasajeros[] = new Pasajero[10];

        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Pasajero(i, bagon);
            pasajeros[i].start();
        }*/
        /*GestorAgua gestor = new GestorAgua();
        Hidrogeno atomosHidrogeno[] = new Hidrogeno[12];
        Oxigeno atomosOxigenos[] = new Oxigeno[4];
        for (int i = 0; i < atomosHidrogeno.length; i++) {
            if (i < 4)
                atomosOxigenos[i] = new Oxigeno(i, gestor);
            atomosHidrogeno[i] = new Hidrogeno(i, gestor);
        }

        for (int i = 0; i < atomosHidrogeno.length; i++) {
            if (i < 4)
                atomosOxigenos[i].start();
            atomosHidrogeno[i].start();
        }

        for (int i = 0; i < atomosHidrogeno.length; i++) {
            if (i < 4)
                atomosOxigenos[i].join();
            atomosHidrogeno[i].join();
        }*/

        //EJERCICIO DE MONITORES: BARCA DE ESTUDIANTES
        /*Barca bote = new Barca();
        Remero remero = new Remero(0, bote);
        EstudianteIOS ios [] = new EstudianteIOS[4];
        EstudianteAndroid andr[] = new EstudianteAndroid[4];
        for (int i = 0; i < ios.length ; i++)
        {
            ios[i] = new EstudianteIOS(i, bote);
            andr[i] =new EstudianteAndroid(i, bote);
        }

        remero.start();
        for (int i = 0; i < ios.length; i++) {
            ios[i].start();andr[i].start();
        }*/



        /*Coche bagon = new Coche(0);
        Pasajero[] pasajeros = new Pasajero[5];

        bagon.start();
        for (int i = 0; i < pasajeros.length; i++) {
            pasajeros[i] = new Pasajero(i, bagon);
            pasajeros[i].start();
        }*/


        GestorAgua gestor = new GestorAgua(0);
        Oxigeno oxigenos[] = new Oxigeno[5];
        Hidrogeno hidrogenos[] = new Hidrogeno[5];
        for(int i = 0 ; i < 5; i++) {
            oxigenos[i] = new Oxigeno(i, gestor);
            hidrogenos[i] = new Hidrogeno(i, gestor);
        }

        for(int i = 0 ; i < 5; i++) {
            oxigenos[i].start();
            hidrogenos[i].start();
        }
        try {
            Thread.sleep(5000);
            for(int i = 0 ; i < 5; i++) {
                oxigenos[i].interrupt();
                hidrogenos[i].interrupt();
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
