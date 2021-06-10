package Locks.Practica8.Ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Gestor {
    private static final int A = 0;
    private static final int B = 1;
    private int numImpresorasA, numImpresorasB;
    
    private Lock l;
    private Condition colaImpresionA, colaImpresionB, colaImpresionAB;

    public Gestor(int impA, int impB) {
        numImpresorasB = impB;
        numImpresorasA = impA;

        l = new ReentrantLock(true);
        colaImpresionAB = l.newCondition();
        colaImpresionB = l.newCondition();
        colaImpresionA = l.newCondition();
    }

    /**
     * Método entrada al rec compartido
     * @param id que doc quiere ser impreso
     */
    public void imprimirA(int id) throws InterruptedException {
        l.lock();
        try{
            while (numImpresorasA == 0)
                colaImpresionA.await();
            numImpresorasA--;
            System.out.println("\t\tEl documento "+id+" de tipo A, comienza a imprimirse");
        } finally {
            l.unlock();
        }
    }

    /**
     * Método entrada al rec compartido
     * @param id que doc quiere ser impreso
     */
    public void imprimirB(int id) throws InterruptedException {
        l.lock();
        try{
            while (numImpresorasB == 0)
                colaImpresionA.await();
            numImpresorasB--;
            System.out.println("\tEl documento "+id+" de tipo B, comienza a imprimirse");
        } finally {
            l.unlock();
        }
    }

    /**
     * Método entrada al rec compartido
     * @param id que doc quiere ser impreso
     * @return donde ha imprimido; 0 en A, o, 1 en B
     */
    public int imprimirAB(int id) throws InterruptedException {
        int res = -1;//Caso de error
        l.lock();
        try{
            while (numImpresorasB + numImpresorasA == 0)
                colaImpresionAB.await();
            if (numImpresorasA > 0){
                res = 0;
                numImpresorasA--;
                System.out.println("El documeto "+id+" de tipo AB, entra a imprimirse en una impresora A");
            } else {
                res = 1;
                numImpresorasB--;
                System.out.println("El documeto "+id+" de tipo AB, entra a imprimirse en una impresora A");
            }
        } finally {
            l.unlock();
        }
        return res;
    }

    /**
     * Una vez imprimido, sale del rec compartido, liberando impresorea
     * @param id que doc se ha impreso
     * @param impAsignada donde se ha impreso
     */
    public void devolverImpresora(int id, int impAsignada) {//Método salir
        l.lock();
        try{
            if (impAsignada == A) {
                numImpresorasA++;
                System.out.println("\n\tDOCUMENTO "+id+" LISTO!! IMPRESO EN A");
                colaImpresionA.signalAll();
            } else {
                numImpresorasB++;
                System.out.println("\n\tDOCUMENTO "+id+" LISTO!! IMPRESO EN b");
                colaImpresionB.signalAll();
            }
            colaImpresionAB.signalAll();
        } finally {
            l.unlock();
        }
    }
}
