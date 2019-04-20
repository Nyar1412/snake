/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class consumable implements localizable{
    private Punto punto;
    private int radio;

    public consumable(Punto p, int radio) {
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
    
    
}
