/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class Escenario {

    ArrayList<localizable> coches;
    Punto origen;
    Punto fin;

    public Escenario(Punto origen, Punto fin) {
        coches = new ArrayList();
        this.origen = origen;
        this.fin = fin;
    }

    public void set(Punto origen, Punto fin) {
        this.origen = origen;
        this.fin = fin;
    }

    public void add(String cadena, int posX, int posY) {
        
    }

    

    public Stream<localizable> getStream() {
        return coches.stream();
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

    public boolean detectarChoque(localizable c, double distancia) {
        boolean res = false;
        localizable locA = coches.stream().filter(p -> p.getPosicion().equals(c.getPosicion())).findFirst().orElse(null);
        if (locA != null) {
            res = coches.stream().filter(p -> !p.equals(locA)).anyMatch(p -> {
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
}
