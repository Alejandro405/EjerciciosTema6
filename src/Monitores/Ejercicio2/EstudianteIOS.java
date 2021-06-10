package Monitores.Ejercicio2;

public class EstudianteIOS extends Thread{
    private static final int NUM_CUCES = 4;
    private int id;
    private Barca bote;

    public EstudianteIOS(int newId, Barca newBote)
    {
        bote = newBote;
        id = newId;
    }

    @Override
    public void run()
    {
        try {
            for (int i = 0; i < NUM_CUCES; i++) {
                bote.entraIOS(id);
            }
        } catch ( InterruptedException e ) {
            System.out.println("Error con la hebra estudiante IOS de id: "+id+". MENSAJE: "+e.getMessage());
        }
    }
}
