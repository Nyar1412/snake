package snake.clases;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class Escenario {

    ArrayList<Localizable> localizables;
    Punto origen;
    Punto fin;
    int numConsumables;

    public Escenario(Punto origen, Punto fin) {
        localizables = new ArrayList();
        this.origen = origen;
        this.fin = fin;
        this.numConsumables = 0;
    }
    
    /**
     * Hace un set de los puntos de origen y fnal del escenario 
     * @param origen Punto de origen
     * @param fin Punto final
     */
    public void set(Punto origen, Punto fin) {
        this.origen = origen;
        this.fin = fin;
    }
    
    
    /**
     * Metodo que genera los elementos de dentro de un escenario a excepcion de
     * a serpiente , la cual se pasa como parametro , y se vincula al escenario
     * @param numObstaculos numero de obstaculos a crear
     * @param numConsumables numero de consuables a crear
     * @param serpiente serpiente a vincular
     */
    public void generarEscenario(int numObstaculos, int numConsumables, Serpiente serpiente) {
        this.numConsumables = numConsumables;
        int radio = 15;
        this.localizables.add(serpiente);
        serpiente.setEsc(this);
        for (int i = 0; i < numObstaculos;) {
            int posY = (int) ((Math.random() * (this.fin.getY() - this.origen.getY())) + this.origen.getY());
            int posX = (int) ((Math.random() * (this.fin.getX() - this.origen.getX())) + this.origen.getX());
            Punto p = new Punto(posX, posY);
            Punto q = new Punto(p.getX()+18, p.getY());
            if (this.estaEnEscenario(p) && this.estaEnEscenario(q)) {
                i++;
                this.localizables.add(new Obstaculo(p, q));

            }
        }
        for (int i = 0; i < numConsumables;) {
            int posY = (int) ((Math.random() * (this.fin.getY() - this.origen.getY())) + this.origen.getY());
            int posX = (int) ((Math.random() * (this.fin.getX() - this.origen.getX())) + this.origen.getX());
            Punto p = new Punto(posX, posY);
            if (this.estaEnEscenario(p)) {
                i++;
                this.localizables.add(new Consumable(p, radio));
            }
        }
    }
    /**
     * toma el stream para gestionar los elementos de escenario fuera del objeto
     * 
     * @return stream de Localizable
     */
    public Stream<Localizable> getStream() {
        return localizables.stream();
    }

    /**
     * Metodo que comprueba si un punto del escenario existe dento de el 
     * o se sae del mismo
     * @param p punto a gstionar
     * @return true si existe dentro del escenario , false si no
     */
    public boolean estaEnEscenario(Punto p) {
        boolean res = false;
        if (p.getX() >= this.origen.getX() && p.getX() <= this.fin.getX()) {
            if (p.getY() >= this.origen.getY() && p.getY() <= this.fin.getY()) {
                res = true;
            }
        }
        return res;
    }
    
    /**
     * Metodo que se ocupa de la gestion de golpes entre la cabeza de un Objeto 
     * del tipo Serpiente,y el resto de objetos en el escenario
     * 
     * @param Serp Objeto del tipo Serpiente a a que se aplicará el modelo. 
     * De estar entre 
     * @return true si hay un choque o false de lo contario
     */
    public boolean detectarChoque(Serpiente Serp) {
        boolean res = false;
        Serpiente snake = (Serpiente) localizables.stream().filter(p -> p.getPosicion().equals(Serp.getPosicion())).findFirst().orElse(null);
        if (snake != null) {

            localizables.stream().filter(p -> !p.equals(snake)).forEach(p -> {

                if (p instanceof Obstaculo) {
                    int modulo = hallarModulo(snake.getPosicion(),p.getPosicion());
                    int modulo2 = hallarModulo(snake.getPosicion(),((Obstaculo) p).getPosicionFin());
                    int modulo3 = hallarModulo(snake.getPosicion(),new Punto(
                            (p.getPosicion().getX()+(((Obstaculo) p).getPosicionFin().getX()))/2, 
                            (p.getPosicion().getY()+(((Obstaculo) p).getPosicionFin().getY()))/2)
                    );
                    
                    if (modulo<10||modulo2<10||modulo3<10) {
                        snake.setChoque(true);
                        System.out.println("obstaculo alcanzado");
                    }

                } else if (p instanceof Consumable) {
                    int modulo = hallarModulo(snake.getPosicion(),p.getPosicion()); 
                    if (modulo <= 9) {
                        snake.comerConsumable();
                        p = (Consumable) p;
                        ((Consumable) p).setPunto(new Punto(-100, -100));
                        numConsumables--;
                    }

                }
            });
            res = snake.isChoque();
        }
        return res;

    }
    /**
     * Metodo para adquirir el modulo de dos puntos dados
     * 
     * @param p punto origen
     * @param q punto fin
     * @return el modulo entero de ambos metodos
     */
    public int hallarModulo(Punto p,Punto q){
        return (int) Math.sqrt(Math.pow((p.getX() - q.getX()), 2)
                            + Math.pow(p.getY() - q.getY(), 2));
    }
    /**
     * Metodo a aplicar para ubiar un unto que tene alun elemento fuera del
     * escenario dentro del mismo
     * @param p Punto a reubicar
     * @return punto reubicado
     */
    public Punto ubicarEnEscenario(Punto p) {
        if (p.getX() > this.fin.getX()) {
            p.setX(this.fin.getX());
        } else if (p.getY() < this.origen.getY()) {
            p.setY(this.origen.getY());
        } else if (p.getY() > this.fin.getY()) {
            p.setY(this.fin.getY());
        } else if (p.getX() < this.origen.getX()) {
            p.setX(this.origen.getX());
        }
        return p;
    }
    
    /**
     * Metodo para saber si la patida se ha terminado
     * @return tru si la partida continua , false si no
     */
    public boolean isPartidaFinalizada() {
        return numConsumables == 0;
    }
    /**
     * Metodo que comprueba si el escenario tiene algun elemento en un punto 
     * dado
     * @param p punto a estionar
     * @return true si aun elemento pasa por ese punto , false si no
     */
    public boolean isPuntoVacio(Punto p) {
        return localizables.stream().flatMap(r -> {
            ArrayList<Punto> puntos = new ArrayList<>();
            if (r instanceof Obstaculo) {
                puntos.add(r.getPosicion());
                puntos.add(((Obstaculo) r).getPosicionFin());
            } else {
                puntos.add(r.getPosicion());
            }
            return puntos.stream();
        }).anyMatch(r -> ((r.getX() == p.getX()) && (r.getY() == p.getY())));
    }
}
