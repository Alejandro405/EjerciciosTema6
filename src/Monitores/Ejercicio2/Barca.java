package Monitores.Ejercicio2;

public class Barca {
    private static final int MAX_BARCA = 4;
    private static final long TIEMPO_VIAJE = 100;//ms
    private int numAndroid;
    private int numIOS;

    public Barca(){
        numAndroid = 0;
        numIOS = 0;
    }

    public synchronized void entraAndroid(int id) throws InterruptedException {
        while (!isFull(numAndroid, numIOS) && !configAndroidCorreta(numIOS, numAndroid))
        {
            wait();
        }
        System.out.println("Estudiante Android: "+id+" entra a la barca. Esperando para cruzar");
        numAndroid++;

        notifyAll();
        while (!isEmpty())
            wait(); //Me bloqueo hasta que no hallamos cruzado
    }

    public synchronized void entraIOS(int id) throws InterruptedException {
        while (!isFull(numAndroid, numIOS) && !configIOSCorreta(numIOS, numAndroid))
        {
            wait();
        }
        System.out.println("Estudiante IOS: "+id+" entra a la barca. Esperando para cruzar");
        numIOS++;
        notifyAll();
        while (!isEmpty())
            wait();
    }

    private boolean isEmpty() {
        return numAndroid == 0 && numIOS == 0;
    }

    public synchronized void cruzarRio(int id) throws InterruptedException {
        while (numIOS + numAndroid < MAX_BARCA)
            wait();
        System.out.println("\t\tYa están los tíos listos para el paseo!!");
        numAndroid = 0;
        numIOS = 0;
        Thread.sleep(TIEMPO_VIAJE);//Introducimos un delay para simular el cruce
        System.out.println("Paseo finalizado");
        notifyAll();
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
