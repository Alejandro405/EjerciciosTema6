package Locks.Ejercicios.Ejercicio2;

public class Barca {
    private static final int MAX_BARCA = 4;
    private static final long TIEMPO_VIAJE = 100;//ms
    private int numAndroid;
    private int numIOS;

    public Barca(){
        numAndroid = 0;
        numIOS = 0;
    }

    public void entraAndroid(int id) throws InterruptedException {

    }

    public void entraIOS(int id) throws InterruptedException {

    }

    private boolean isEmpty() {
        return numAndroid == 0 && numIOS == 0;
    }

    public void cruzarRio(int id) throws InterruptedException {

    }

    private static boolean configIOSCorreta(int Android, int IOS) {
        return !(IOS == 0 && Android == 3);
    }

    private static boolean configAndroidCorreta( int Android, int IOS) {
        return !(Android == 0 && IOS == 3);
    }

    private boolean isFull(int numAndroid, int numIOS) {
        return (numAndroid + numIOS) == MAX_BARCA;
    }

}
