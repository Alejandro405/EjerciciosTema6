package Locks.Ejercicios.Ejercicio2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barca {
    private static final int MAX_BARCA = 4;

    private int numAndroid, numIOS;
    private boolean hayBarca, puedenBajar;

    private Lock l;

    private Condition esperaBajar;
    private Condition esperaSubirAndroid;
    private Condition esperaSubirIOS;

    private Condition esperaDesalojo, esperaLlenarBarca;

    public Barca(){
        numAndroid = 0;
        numIOS = 0;
        hayBarca = true;
        puedenBajar = false;

        l = new ReentrantLock(true);
        esperaDesalojo = l.newCondition();
        esperaLlenarBarca = l.newCondition();
        esperaBajar = l.newCondition();
        esperaSubirAndroid = l.newCondition();
        esperaSubirIOS = l.newCondition();
    }

    /**
     * método para que se suban los estudiante de IOS
     * @param id de estudiante de Android
     */
    public void android(int id) throws InterruptedException {
        l.lock();
        try{
            while(!(configuracionCorrectaAndroid(numAndroid + 1, numIOS)) && !hayBarca)
                esperaSubirAndroid.await();
            numAndroid++;
            System.out.println("\tEl estudiante "+id+" con Adroid se monta al bote. "+toString());
            if (isFull(numAndroid, numIOS)) {//Llama al remero
                esperaLlenarBarca.signalAll();
                hayBarca = false;
            }
        }finally{
            l.unlock();
        }
    }



    /**
     * método para que se suban los estudiante de IOS
     * @param id de estudiante de IOS
     */
    public void iphone(int id) throws InterruptedException {
        l.lock();
        try{
            while (!configuracionCorrectaIOS(numAndroid, numIOS + 1) && !hayBarca)
                esperaSubirIOS.await();
            numIOS++;
            System.out.println("El estudiante "+id+" con IOS se sube al bote. "+toString());
            if (isFull(numAndroid, numIOS)) {//Llama al remero
                esperaLlenarBarca.signalAll();
                hayBarca = false;
            }
        }finally{
            l.unlock();
        }
    }

    /**
     * método para que salgan de la barca los estudiantes con Android
     * @param id del estudiante Android
     */
    public void saleAndroid(int id) throws InterruptedException {
        l.lock();
        try{
            while(!puedenBajar)
                esperaBajar.await();
            numAndroid--;
            System.out.println("\tEl estudiante "+id+" con Adroid baja del bote. "+toString());
            if (isEmpty())
                esperaBajar.signalAll();
        }finally{
            l.unlock();
        }
    }

    /**
     * método para que salgan de la barca los estudiantes con IOS
     * @param id
     */
    public void saleIphone(int id) throws InterruptedException {
        l.lock();
        try{
            while (!puedenBajar) {
                esperaBajar.await();
            }
            numIOS--;
            System.out.println("El estudiante "+id+" con IOS se baja del bote. "+toString());
            if (isEmpty())
                esperaBajar.signalAll();
        }finally{
            l.unlock();
        }
    }

    /**
     *  Método que usa el remero para cruzar los alumnos
     * @param id del remero
     */
    public void cruzarRio(int id) throws InterruptedException {
        l.lock();
        try{
            while(!isFull(numAndroid, numIOS))
                esperaLlenarBarca.await();
            System.out.println("\n\tYA SE PUEDE CRUZAR. PROCEDEMOS A CRUZAR EL RÍO");
            hayBarca = false;

            Thread.sleep(1000);
            puedenBajar = true;
            System.out.println("\n\tFIN DEL CRUZE");
            esperaBajar.signalAll();

            while (!isEmpty())
                esperaDesalojo.await();
            //Cuando se bajan devolvemos el bote, y despertamos a los estudiantes que queden ==> Volvemos al estado inicial
            hayBarca = true;
            puedenBajar = false;
            esperaSubirIOS.signalAll();
            esperaSubirAndroid.signalAll();
        }finally{
            l.unlock();
        }
    }


    @Override
    public String toString()
    {
        return"ESTADO: "+numAndroid+" android, y "+numIOS+" IOS";
    }
    /**
     *
     * @return true sii la barca está vacía
     */
    private boolean isEmpty() {
        return numAndroid + numIOS == 0;
    }

    /**
     * El estudiante ha de estar incluido
     * @param Android número de estudiantes de Android
     * @param IOS número de estudiante de IOS
     * @return false si la suma es mayor a 4 o si no hya buena configuración para IOS 1 vs 3
     */
    private boolean configuracionCorrectaIOS(int Android, int IOS) {
        boolean res = true;
        if (IOS == 1 && Android == 3)
            res = false;
        if (Android + IOS >= MAX_BARCA)
            res = false;

        return res;
    }

    /**
     * El estudiante ha de estar incluido
     * @param Android número de estudiantes de Android
     * @param IOS numero de estudiante de IOS
     * @return false si la suma es mayor a 4 o si no hya buena configuración para Android 1 vs 3
     */
    private boolean configuracionCorrectaAndroid(int Android, int IOS) {
        boolean res = true;
        if (Android == 1 && IOS == 3)
            res = false;
        if (IOS + Android >= MAX_BARCA)
            res = false;

        return res;
    }

    /**
     *
     * @param numAndroid
     * @param numIOS
     * @return true sii la barca está llena
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
