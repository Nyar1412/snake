/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public Escenario(Punto origen, Punto fin) {
        localizables = new ArrayList();
        this.origen = origen;
        this.fin = fin;
    }

    public void set(Punto origen, Punto fin) {
        this.origen = origen;
        this.fin = fin;
    }

    public void generarEscenario(int numObstaculos, int numConsumables) {
        int radio=3;
        this.localizables.add(new Serpiente(new Punto(this.origen.getX()+10,this.origen.getY()+10)));
        for (int i = 0; i < numConsumables; ) {
            int posY=(int)((Math.random()*(this.fin.getY()-this.origen.getY()))+this.origen.getY());
            int posX=(int)((Math.random()*(this.fin.getX()-this.origen.getX()))+this.origen.getX());
            Punto p=new Punto(posX,posY);
            Punto q= new Punto(p.getX()+4,p.getY()+2);
            if(this.estaEnEscenario(p) && this.estaEnEscenario(q)){
                if(isPuntoVacio(p) && isPuntoVacio(q)){
                    i++;
                    this.localizables.add(new Obstaculos(p,q));
                }
            }
        }
        for (int i = 0; i < numObstaculos; i++) {
            int posY=(int)((Math.random()*(this.fin.getY()-this.origen.getY()))+this.origen.getY());
            int posX=(int)((Math.random()*(this.fin.getX()-this.origen.getX()))+this.origen.getX());
            Punto p=new Punto(posX,posY);
            if(this.estaEnEscenario(p) && isPuntoVacio(p)){
                i++;
                this.localizables.add(new Consumable(p,radio));
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

    public boolean detectarChoque(Localizable c, double distancia) {
        boolean res = false;
        Localizable locA = localizables.stream().filter(p -> p.getPosicion().equals(c.getPosicion())).findFirst().orElse(null);
        if (locA != null) {
            res = localizables.stream().filter(p -> !p.equals(locA)).anyMatch(p -> {
                int modulo = (int) Math.sqrt(Math.pow((locA.getPosicion().getX() - p.getPosicion().getX()), 2)
                        + Math.pow((locA.getPosicion().getY() - p.getPosicion().getY()), 2));
                return modulo <= distancia;
            });

        }
        return res;

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
    
    public boolean isPuntoVacio(Punto p){
        return localizables.stream().flatMap(r->{
                    ArrayList<Punto> puntos=new ArrayList<>();
                    if(r instanceof Obstaculos){
                        puntos.add(r.getPosicion());
                        puntos.add(((Obstaculos) r).getPosicionFin());
                    }else{
                        puntos.add(r.getPosicion());
                    }
                    return puntos.stream();
                }).anyMatch(r-> ((r.getX()==p.getX())&&(r.getY()==p.getY())));
    }
}
