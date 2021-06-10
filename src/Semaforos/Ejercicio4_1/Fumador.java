package Semaforos.Ejercicio4_1;

public class Fumador extends Thread {
    private int id;
    private int ingrediente;
    private Mesa mesa;

    private static final int DELAY = 50;

    public Fumador(int newId, Mesa newMesa, int newIngrediente)
    {
        id = newId;
        mesa = newMesa;
        ingrediente = newIngrediente;
    }
    public int getIngrediente() {
        return ingrediente;
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                mesa.liaCigarrillo(id, ingrediente);
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
