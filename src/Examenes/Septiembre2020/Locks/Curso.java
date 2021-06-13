package Examenes.Septiembre2020.Locks;

import java.util.concurrent.Semaphore;

public class Curso {
	private final int MAX_ALUMNOS_INI = 10;//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int ALUMNOS_AV = 3;//Numero de alumnos por grupo en la parte avanzada

	private int numAlumnosInicial;
	private int numGrupoAvanzado, numAlumnosFinalizado;
	private boolean finGrupoAvanzado;

	private Semaphore mutex;
	private Semaphore esperaPlazaIniciacion;//Semáforos para la parte de iniciación
	private Semaphore esperaGrupoAvanzado, esperaCompletarGrupoAvanzado, esperaSalirCursoAvanzado;//Semáforos para la parte avanzada

	public Curso()
	{
		numAlumnosFinalizado = 0;
		numAlumnosInicial = 0;
		numGrupoAvanzado = 0;
		finGrupoAvanzado = true;

		mutex = new Semaphore(1, true);
		esperaGrupoAvanzado = new Semaphore(0, true);
		esperaPlazaIniciacion = new Semaphore(1, true);
		esperaSalirCursoAvanzado = new Semaphore(0, true);
		esperaCompletarGrupoAvanzado = new Semaphore(0, true);
	}




	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		//Espera si ya hay 10 alumnos cursando esta parte
		esperaPlazaIniciacion.acquire();
		mutex.acquire();
		numAlumnosInicial++;
		//Mensaje a mostrar cuando el alumno pueda conectarse y cursar la parte de iniciacion
		System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");
		if (numAlumnosInicial < this.MAX_ALUMNOS_INI) {
			esperaPlazaIniciacion.release();
		}
		mutex.release();
	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		mutex.acquire();
		//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
		System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
		//Libera la conexion para que otro alumno pueda usarla
		numAlumnosInicial--;
		esperaPlazaIniciacion.release();
		mutex.release();
	}

	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		//Espera a que no haya otro grupo realizando esta parte
		while(!finGrupoAvanzado)//No se libera el grupo
			esperaGrupoAvanzado.acquire();
		mutex.acquire();
		//Espera a que haya tres alumnos conectados
		numGrupoAvanzado++;
		if (numGrupoAvanzado == this.ALUMNOS_AV)
			finGrupoAvanzado = false;//El curso tiene que empezar
		//Mensaje a mostrar si el alumno tiene que esperar al resto de miembros en el grupo
		System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");
		mutex.release();
		while (numGrupoAvanzado < 3) {
			esperaCompletarGrupoAvanzado.acquire();
		}
		mutex.acquire();
		//Mensaje a mostrar cuando el alumno pueda empezar a cursar la parte avanzada
		System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
		esperaCompletarGrupoAvanzado.release();
		mutex.release();
	}

	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte
	 */
	public void finAvanzado(int id) throws InterruptedException{
		mutex.acquire();
		numAlumnosFinalizado++;
		System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
		mutex.release();
		while (numAlumnosFinalizado < 3)
			esperaSalirCursoAvanzado.acquire();
		mutex.acquire();
		numGrupoAvanzado--;
		if (numGrupoAvanzado == 0) {
			finGrupoAvanzado = true;
			numAlumnosFinalizado = 0;
			esperaGrupoAvanzado.release();
		} else{
			esperaSalirCursoAvanzado.release();
		}
		mutex.release();

		System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
	}
}
