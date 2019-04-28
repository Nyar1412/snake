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
public class Consumable implements Localizable{
    private Punto punto;
    private int radio;

    public Consumable(Punto p, int radio) {
        this.punto = p;
        this.radio = radio;
    }

    @Override
    public Punto getPosicion() {
        return this.punto; //To change body of generated methods, choose Tools | Templates.
    }

    public int getRadio() {
        return radio;
    }

    public Punto getPunto() {
        return punto;
    }

    public void setPunto(Punto punto) {
        this.punto = punto;
    }
    
    
    
}
