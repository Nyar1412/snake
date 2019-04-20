/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class serpiente implements localizable {

    private boolean detectarChoque() {
        return this.cuerpo.stream().filter(p->!p.equals(this.cabeza)).anyMatch(p->((p.getX()==cabeza.getX())&&((p.getY()==cabeza.getY()))));
    }
    private void comerConsumable(){
        this.comida+=5;
    }

    enum Estado {
        IZQUIERDA, DERECHA, ARRIBA, ABAJO
    }

    private Estado movimiento;
    private Punto cabeza;
    private boolean choque;
    private Escenario esc;
    private int comida;

    private LinkedList<Punto> cuerpo;

    public serpiente(Punto cabeza) {
        this.movimiento = serpiente.Estado.DERECHA;
        this.cabeza = cabeza;
        this.choque = false;
        this.esc = null;
        this.comida=0;
        this.cuerpo = new LinkedList<>();
        this.cuerpo.add(cabeza);
        this.cuerpo.add(new Punto(cabeza.getX() - 1, cabeza.getY()));
        this.cuerpo.add(new Punto(cabeza.getX() - 2, cabeza.getY()));
        this.cuerpo.add(new Punto(cabeza.getX() - 3, cabeza.getY()));
        this.cuerpo.add(new Punto(cabeza.getX() - 4, cabeza.getY()));
    }
    
    
    public void cambiarMovimiento(Estado estado){
        switch(this.movimiento){
            case IZQUIERDA:
            case DERECHA:
                if (estado.equals(Estado.ABAJO) || estado.equals(Estado.ARRIBA) ) {
                    this.movimiento=estado;
                }
                break;
            case ARRIBA:
            case ABAJO:
                if (estado.equals(Estado.IZQUIERDA) || estado.equals(Estado.DERECHA) ) {
                    this.movimiento=estado;
                }
                break;
        }
    }

    public void mover() {
        Punto nuevo = localizable.p;
        if (!esc.equals(null) && !choque) {
            switch (movimiento) {
                case IZQUIERDA:
                    nuevo = new Punto(cabeza.getX()-1, cabeza.getY());
                    break;
                case ABAJO:
                    nuevo = new Punto(cabeza.getX(), cabeza.getY()+1);
                    break;
                case ARRIBA:
                    nuevo = new Punto(cabeza.getX(), cabeza.getY()-1);
                    break;
                case DERECHA:
                    nuevo = new Punto(cabeza.getX()-1, cabeza.getY());
                    break;
            }
            if (esc.estaEnEscenario(nuevo)) {
                cuerpo.addFirst(nuevo);
                cabeza = nuevo;
                detectarChoque();
                if(comida!=0){
                    comida-=1;
                }else{
                    cuerpo.removeLast();
                }
            }else{
                choque=true;
            }
        }
    }

    @Override
    public Punto getPosicion() {
        return this.cabeza;
    }
    
    

}
