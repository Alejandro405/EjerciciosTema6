package Semaforos.Ejercicio4;

public class Fumador {
    private int id;
    private int ingrediente;
    private Mesa mesa;

    public Fumador(int newId, Mesa newMesa, int newIngrediente)
    {
        id = newId;
        mesa = newMesa;
    }
    public int getIngrediente() {
        return ingrediente;
    }
    public int getId(){
        return id;
    }
}
