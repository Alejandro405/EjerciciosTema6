package Locks.Ejercicios.Ejercicio2;

public class EstudianteAndroid extends Thread{
    private static final int NUM_CUCES = 4;
    private int id;
    private Barca bote;

    public EstudianteAndroid(int newId, Barca newBote)
    {
        id =newId;
        bote = newBote;
    }

    @Override
    public void run()
    {
        try{
            bote.android(id);
            bote.saleAndroid(id);
        }catch (InterruptedException e ) {
            System.out.println("Error con la hebra estudiante Android de id: "+id+". MENSAJE: "+e.getMessage());
        }

    }
}
