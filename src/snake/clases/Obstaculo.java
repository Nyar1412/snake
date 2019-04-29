/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.clases;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class Obstaculo implements Localizable{
    private Punto origen;
    private Punto fin;

    public Obstaculo(Punto origen, Punto fin) {
        this.origen = origen;
        this.fin = fin;
    }

    @Override
    public Punto getPosicion() {
        return this.origen; 
    }
    public Punto getPosicionFin() {
        return this.fin; 
    }
    
}
