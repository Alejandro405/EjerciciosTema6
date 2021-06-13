package Semaforos.Ejercicio4;

import java.util.concurrent.Semaphore;

/**
    Este problema puede verse como la unión entre un problema del tipo productor consumidor, y un problema del tipo de
    los filósofos.
    La solución trata de mezclar las soluciones a los problemas tipo (productor consumidor - Filósofos)
 */
public class Mesa {
    //El array de semaforo proviene de la solucion al problema de los filósofos
    private Semaphore ingredientes[];

    //CONSTANTES PARA MEJORAR LA LEJIBILIDAD DEL CÓDIGO
    private static final int TABACO = 0;
    private static final int PAPEL = 1;
    private static final int CERILLAS = 2;

    /*
        En un principio se asume que en la mesa no hay ingredientes
        Por tanto los semáforos deben inicializarse a 0 (cerrados)
     */
    public Mesa()
    {
        ingredientes = new Semaphore[3];
        for (int i = 0; i < ingredientes.length; i++) {
            ingredientes[i] = new Semaphore(0, true);
        }
    }

    /*
        La hebra cliente aporta de forma aleatoria dos ingredientes
        NO HAY CONTROL DE ERRORES, los parámetros han de casar con el valor de las constantes
     */
    public void aportaIngredientes(int ingredienteA, int ingredienteB)
    {
        ingredientes[ingredienteA].release();
        ingredientes[ingredienteB].release();
        System.out.println("Se ha aportado: "+int_String(ingredienteA)+", "+int_String(ingredienteB));
    }

    /*
        Necesitamos tener la referencia de la hebra fumadora que lanza el método
        Para saber que ingrdiente tiene y cual le falta
     */
    public void liaCigarrillo(Fumador fumador) throws InterruptedException {
        /*switch (fumador.getIngrediente()) {
            case TABACO -> {
                ingredientes[PAPEL].acquire();
                ingredientes[CERILLAS].acquire();
                System.out.println("Fumador " + fumador.getId() + " fuma");
                Thread.sleep(10);//Tiempo de reetraso pasa simular el tiempo que tarda en fumar
            }
            case PAPEL -> {
                ingredientes[TABACO].acquire();
                ingredientes[CERILLAS].acquire();
                System.out.println("Fumador " + fumador.getId() + " fuma");
                Thread.sleep(10);//Tiempo de reetraso pasa simular el tiempo que tarda en fumar
            }
            case CERILLAS -> {
                ingredientes[TABACO].acquire();
                ingredientes[PAPEL].acquire();
                System.out.println("Fumador " + fumador.getId() + " fuma");
                Thread.sleep(10);//Tiempo de reetraso pasa simular el tiempo que tarda en fumar
            }
        }*/
    }

    private String int_String(int ingrediente) {
        /*String res = switch (ingrediente) {
            case TABACO -> "TABACO";
            case PAPEL -> "PAPEL";
            case CERILLAS -> "CERILLAS";
            default -> null;
        };*/


        return "";
    }
}
