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
public interface Localizable {
    Punto p =new Punto(0, 0);
    default Punto getPosicion(){
        return Localizable.p;
    }
}
