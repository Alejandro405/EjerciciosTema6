package Locks.Ejercicios.Ejercicio2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barca {
    private static final int MAX_BARCA = 4;
    private static final int IOS = 0;
    private static final int ANDROID = 1;
    private int numAndroid;
    private int numIOS;
    private boolean finTrayecto, hayBarca;

    private Lock l;
    private Condition esperaEntradaIOS, esperaEntradaAndroid, esperaBajar;//Variables de condicion para los estudiantes
    private Condition esperaMontados, esperaDesalojarEstudiantes;//VAriables de condicion para el remero

    public Barca(){
        numAndroid = 0;
        numIOS = 0;
        finTrayecto = false;
        hayBarca = true;

        l = new ReentrantLock(true);
        esperaBajar = l.newCondition();
        esperaEntradaAndroid = l.newCondition();
        esperaEntradaIOS = l.newCondition();
        esperaMontados = l.newCondition();
        esperaDesalojarEstudiantes = l.newCondition();
    }

    /**
     *
     * @param id
     * @throws InterruptedException
     */
    public void entraAndroid(int id) throws InterruptedException {
        l.lock();
        try{
            while (!configAndroidCorreta(numAndroid + 1, numIOS) && !hayBarca)
            {
                esperaEntradaAndroid.await();
            }

            numAndroid++;
            System.out.println("El estudiante de Android "+id+" entra al fuckin bote");
            if (isFull(numAndroid, numIOS))
                esperaMontados.signalAll();
        }finally {
            l.unlock();
        }
    }

    /**
     *
     * @param id
     * @throws InterruptedException
     */
    public void entraIOS(int id) throws InterruptedException {
        l.lock();
        try{
           while (!hayBarca && !configIOSCorreta(numAndroid, numIOS + 1))
           {
               esperaEntradaIOS.await();
           }

           numIOS++;
           System.out.println("\tEl estudiante de IOS "+id+" entra al fuckin bote");
           if (isFull(numAndroid, numIOS)){
               esperaMontados.signalAll();
           }
        }finally {
            l.unlock();
        }
    }

    /**
     *
     * @param id
     * @param tipo: 0 IOS, 1 ANDROID
     * @throws InterruptedException
     */
    public void bajarBarca(int id, int tipo) throws InterruptedException {
        l.lock();
        try{
            while (!finTrayecto)
                esperaBajar.await();
            if (tipo == IOS){
                numIOS--;
                System.out.println("El estudiante con IOS, "+id+", sale de la barca");
            } else {
                numAndroid--;
                System.out.println("El estudiante con android, "+id+", sale de la barca");
            }
            if (isEmpty()) {//Pasamos la barca a la otra orilla, estado inicial
                hayBarca = true;
                finTrayecto = false;
                esperaDesalojarEstudiantes.signalAll();
            }
        } finally {
            l.unlock();
        }
    }

    /**
     *
     * @return
     */
    private boolean isEmpty() {
        return numAndroid == 0 && numIOS == 0;
    }

    /**
     *
     * @param id
     * @throws InterruptedException
     */
    public void cruzarRio(int id) throws InterruptedException {
        l.lock();
        try{
            while(numIOS + numAndroid < MAX_BARCA)//Espera a que se monten
                esperaMontados.await();
            hayBarca = false;
            System.out.println("\tVAMOS ACRUZAR");
            Thread.sleep(1000);
            finTrayecto = true;//Dejamos que cruzen
            System.out.println("\tHEMOS CRUZADO");
            esperaBajar.signalAll();

            while (numIOS + numAndroid > 0)//Espera a que se bajen
                esperaDesalojarEstudiantes.await();
            esperaEntradaAndroid.signalAll();
            esperaEntradaIOS.signalAll();
        }finally {
            l.unlock();
        }
    }

    /**
     *
     * @param Android
     * @param IOS
     * @return
     */
    private static boolean configIOSCorreta(int Android, int IOS) {
        return !(IOS == 1 && Android == 3);
    }

    /**
     *
     * @param Android
     * @param IOS
     * @return
     */
    private static boolean configAndroidCorreta( int Android, int IOS) {
        return !(Android == 1 && IOS == 3);
    }

    /**
     *
     * @param numAndroid
     * @param numIOS
     * @return
     */
    private static boolean isFull(int numAndroid, int numIOS) {
        return (numAndroid + numIOS) == MAX_BARCA;
    }

    public static void main(String[] args) {
        Barca bote = new Barca();
        Remero remero = new Remero(0, bote);
        EstudianteIOS estudianteIOS[] = new EstudianteIOS[8];
        EstudianteAndroid estudianteAndroid[] = new EstudianteAndroid[8];

        remero.start();
        for (int i = 0; i < estudianteAndroid.length; i++) {
            estudianteAndroid[i] = new EstudianteAndroid(i, bote);
            estudianteIOS[i] = new EstudianteIOS(i, bote);

            estudianteIOS[i].start();
            estudianteAndroid[i].start();
        }
    }
}
