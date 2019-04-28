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

    public void set(Punto origen, Punto fin) {
        this.origen = origen;
        this.fin = fin;
    }

    public void generarEscenario(int numObstaculos, int numConsumables, Serpiente serpiente) {
        this.numConsumables = numConsumables;
        int radio = 10;
        this.localizables.add(serpiente);
        for (int i = 0; i < numObstaculos;) {
            int posY = (int) ((Math.random() * (this.fin.getY() - this.origen.getY())) + this.origen.getY());
            int posX = (int) ((Math.random() * (this.fin.getX() - this.origen.getX())) + this.origen.getX());
            Punto p = new Punto(posX, posY);
            Punto q = new Punto(p.getX() + 4, p.getY());
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

    public Stream<Localizable> getStream() {
        return localizables.stream();
    }

    public boolean estaEnEscenario(Punto p) {
        boolean res = false;
        if (p.getX() >= this.origen.getX() && p.getX() <= this.fin.getX()) {
            if (p.getY() >= this.origen.getY() && p.getY() <= this.fin.getY()) {
                res = true;
            }
        }
        return res;
    }

    public boolean detectarChoque(Serpiente Serp, double distancia) {
        boolean res = false;
        Serpiente snake = (Serpiente) localizables.stream().filter(p -> p.getPosicion().equals(Serp.getPosicion())).findFirst().orElse(null);
        if (snake != null) {

            localizables.stream().filter(p -> !p.equals(snake)).forEach(p -> {

                if (p instanceof Obstaculo) {

                    int modulo = (int) Math.sqrt(Math.pow((snake.getPosicion().getX() - p.getPosicion().getX()), 2)
                            + Math.pow((snake.getPosicion().getY() - p.getPosicion().getY()), 2));
                    if (modulo <= distancia) {
                        snake.setChoque(true);
                        System.out.println("obstaculo alcanzado");
                    }

                } else if (p instanceof Consumable) {
                    int modulo = (int) Math.sqrt(Math.pow((snake.getPosicion().getX() - p.getPosicion().getX()), 2)
                            + Math.pow((snake.getPosicion().getY() - p.getPosicion().getY()), 2));
                    if (modulo <= distancia) {
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

    public boolean isPartidaFinalizada() {
        return numConsumables == 0;
    }

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
