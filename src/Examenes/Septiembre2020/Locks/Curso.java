package Examenes.Septiembre2020.Locks;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Curso {
	private final int MAX_ALUMNOS_INI = 10;//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int ALUMNOS_AV = 3;//Numero de alumnos por grupo en la parte avanzada

	private int numAlumnosInicial;
	private int numGrupoAvanzado, numAlumnosFinalizado;
	private boolean finGrupoAvanzado;

	private Lock l;
	private Condition esperaPlazaIniciacion;//Cariables de condicion para la parte avanzada
	private Condition esperaGrupoAvanzado, esperaCompletarGrupoAvanzado, esperaSalirCursoAvanzado;//Cariables de condicion para la parte avanzada

	public Curso()
	{
		numAlumnosFinalizado = 0;
		numAlumnosInicial = 0;
		numGrupoAvanzado = 0;
		finGrupoAvanzado = true;

		l = new ReentrantLock(true);
		esperaPlazaIniciacion = l.newCondition();
		esperaGrupoAvanzado = l.newCondition();
		esperaCompletarGrupoAvanzado = l.newCondition();
		esperaSalirCursoAvanzado = l.newCondition();
	}




	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		l.lock();
		try{
			//Espera si ya hay 10 alumnos cursando esta parte
			while (numAlumnosInicial == MAX_ALUMNOS_INI)
				esperaPlazaIniciacion.await();
			//Mensaje a mostrar cuando el alumno pueda conectarse y cursar la parte de iniciacion
			System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");
		} finally {
			l.unlock();
		}

	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		l.lock();
		try{
			//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
			System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
			numAlumnosInicial--;
			//Libera la conexion para que otro alumno pueda usarla
			esperaPlazaIniciacion.signalAll();
		} finally {
			l.unlock();
		}

	}

	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		l.lock();
		try{
			//Espera a que no haya otro grupo realizando esta parte
			while (!finGrupoAvanzado)
				esperaGrupoAvanzado.await();

			//Espera a que haya tres alumnos conectados
			System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");//Mensaje a mostrar si el alumno tiene que esperar al resto de miembros en el grupo
			numGrupoAvanzado++;
			if (numGrupoAvanzado == 3)
				esperaCompletarGrupoAvanzado.signalAll();

			//Mensaje a mostrar cuando el alumno pueda empezar a cursar la parte avanzada
			while (numGrupoAvanzado < 3)
				esperaCompletarGrupoAvanzado.await();
			System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
		} finally {
			l.unlock();
		}

	}

	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte
	 */
	public void finAvanzado(int id) throws InterruptedException{
		l.lock();
		try{
			numAlumnosFinalizado++;
			System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
			while (numAlumnosFinalizado < 3)
				esperaSalirCursoAvanzado.await();
			System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
			numGrupoAvanzado--;
			if (numGrupoAvanzado == 0){//Todos fuera
				finGrupoAvanzado = true;
				numAlumnosFinalizado = 0;
				esperaGrupoAvanzado.signalAll();
			} else {//Que se sigan desconectando
				esperaSalirCursoAvanzado.signalAll();
			}
		} finally {
			l.unlock();
		}
	}
}
