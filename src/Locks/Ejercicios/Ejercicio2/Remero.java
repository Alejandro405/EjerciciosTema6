package Locks.Ejercicios.Ejercicio2;

public class Remero extends Thread{
    private static final int MAX_CUCES = 4;
    private Barca bote;
    private int id;

    public Remero(int newId, Barca newBarca)
    {
        id = newId;
        bote = newBarca;
    }

    @Override
    public void run()
    {
        try{
            for (int i = 0; i < MAX_CUCES; i++) {
                bote.cruzarRio(id);
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR: fallo con la hebra remera "+id+". MENSAJE: "+e.getMessage());
        }
    }
}
