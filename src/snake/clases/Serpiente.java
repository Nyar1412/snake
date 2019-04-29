/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.clases;
import java.util.LinkedList;

/**
 *
 * @author Manuel Alejandro Perez Benitez
 */
public class Serpiente implements Localizable {

    private Estado movimiento;
    private Punto cabeza;
    private boolean choque;
    private Escenario esc;
    private int comida;

    private LinkedList<Punto> cuerpo;

    public Serpiente(Punto cabeza,int size) {
        this.movimiento = Serpiente.Estado.DERECHA;
        this.cabeza = cabeza;
        this.choque = false;
        this.esc = null;
        this.comida = 0;
        this.cuerpo = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            this.cuerpo.add(new Punto(cabeza.getX() - i, cabeza.getY()));
        }
    }

    private boolean detectarChoque() {
        this.choque = this.cuerpo.stream()
                .anyMatch(p ->this.cabeza.getX()==p.getX() && this.cabeza.getY()==p.getY());
                     
        if(choque)System.out.println("te has mordido");
        return choque;
    }

    public void comerConsumable() {
        this.comida += 30;
    }

    public enum Estado {
        IZQUIERDA
        , DERECHA
        , ARRIBA
        , ABAJO
    }

    public void cambiarMovimiento(Estado estado) {
        switch (this.movimiento) {
            case IZQUIERDA:
            case DERECHA:
                if (estado.equals(Estado.ABAJO) || estado.equals(Estado.ARRIBA)) {
                    this.movimiento = estado;
                }
                break;
            case ARRIBA:
            case ABAJO:
                if (estado.equals(Estado.IZQUIERDA) || estado.equals(Estado.DERECHA)) {
                    this.movimiento = estado;
                }
                break;
        }
    }

    public void mover(int paso) {
        Punto nuevo = Localizable.p;
        if (!esc.equals(null) && !choque) {
            switch (movimiento) {
                case IZQUIERDA:
                    nuevo = new Punto(cabeza.getX() - paso, cabeza.getY());
                    break;
                case ABAJO:
                    nuevo = new Punto(cabeza.getX(), cabeza.getY() + paso);
                    break;
                case ARRIBA:
                    nuevo = new Punto(cabeza.getX(), cabeza.getY() - paso);
                    break;
                case DERECHA:
                    nuevo = new Punto(cabeza.getX() + paso, cabeza.getY());
                    break;
            }
            if (esc.estaEnEscenario(nuevo)) {
                cuerpo.addFirst(cabeza);
                cabeza = nuevo;
                detectarChoque();
                if (comida != 0) {
                    comida -= 1;
                } else {
                    cuerpo.removeLast();
                }
            } else {
                choque = true;
                System.out.println("final del escenario");
            }
        }
    }

    @Override
    public Punto getPosicion() {
        return this.cabeza;
    }

    public Estado getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Estado movimiento) {
        this.movimiento = movimiento;
    }

    public Punto getCabeza() {
        return cabeza;
    }

    public void setCabeza(Punto cabeza) {
        this.cabeza = cabeza;
    }

    public boolean isChoque() {
        return choque;
    }

    public void setChoque(boolean choque) {
        this.choque = choque;
    }

    public Escenario getEsc() {
        return esc;
    }

    public void setEsc(Escenario esc) {
        this.esc = esc;
    }

    public int getComida() {
        return comida;
    }

    public void setComida(int comida) {
        this.comida = comida;
    }

    public LinkedList<Punto> getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(LinkedList<Punto> cuerpo) {
        this.cuerpo = cuerpo;
    }
}
