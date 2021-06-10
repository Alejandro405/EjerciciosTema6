package Locks.Practica8.Ejercicio2;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GestorAgua {
    private int numOxigeno;
    private int numHidrogeno;

    private Random rand;
    private Lock l;
    private Condition condEntradaO;
    private Condition condEntradaH;
    private Condition condLiberarRecurso;//Producción de agua
    private int numProducciones;

    public GestorAgua(int newId)
    {
        l = new ReentrantLock(true);
        condEntradaO = l.newCondition();
        condEntradaH = l.newCondition();
        condLiberarRecurso = l.newCondition();
        rand = new Random();
        numHidrogeno  = 0;
        numOxigeno = 0;
        numProducciones = 0;
    }

    /*
    Llega un hofrógeno con ganas de fiesta, necesita 1 más y un oxígeno
    Llega un productor, como no tenemos restricciones de la llegada de oxígenos no hace falta condiciones
     */
    public void hListo(int id) throws InterruptedException {
        l.lock();
        try {
            //Condiciones de entrada al recurso
            while (numHidrogeno >= 2)
                condEntradaH.await();
            System.out.println("\tLLega el hidrógeno con pid: "+id+". En el gestor hay, h = "+numHidrogeno+" o = "+numOxigeno);
            numHidrogeno++;
            condLiberarRecurso.signalAll();
            while (numOxigeno > 0)
                condLiberarRecurso.await();
        } finally {
            l.unlock();
        }
    }

    /*
    Llega un oxigeno con ganas de fiesta, si hay 2 de hidrógeno elabora, sino espera
     */
    public void oListo(int id) throws InterruptedException {
        l.lock();
        try {
            //Entrar al recurso para computar
            while (numOxigeno > 0)//No tengo espacion en el gestor
                condEntradaO.await();
            System.out.println("LLega el oígeno con pid: "+id+". En el gestor hay, h = "+numHidrogeno+" o = "+numOxigeno);
            numOxigeno = 1;
            while (numHidrogeno < 2)
                condLiberarRecurso.await();
            numOxigeno = 0;
            condLiberarRecurso.signalAll();
            condEntradaO.signalAll();
        } finally {
            l.unlock();
        }
    }
}
