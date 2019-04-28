/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import snake.clases.Consumable;
import snake.clases.Escenario;
import snake.clases.Obstaculo;
import snake.clases.Punto;
import snake.clases.Serpiente;

/**
 *
 * @author carlos
 */
public class FXMLDocumentController implements Initializable {

    private Escenario escenario;
    private GraphicsContext gcSnake;
    private Serpiente serpiente;
    private Timeline timeLine;
    private boolean reiniciar;

    @FXML
    private Button btnEjecutar;
    @FXML
    private Canvas canvasEscenario;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        escenario = new Escenario(new Punto(0, 0),
                new Punto(canvasEscenario.getWidth() - 18,
                        canvasEscenario.getHeight() - 15));
        serpiente = new Serpiente(new Punto(100, 100), 30);
        serpiente.setEsc(escenario);
        escenario.generarEscenario(5, 7, serpiente);
        canvasEscenario.setOnKeyPressed(event -> mover());
        gcSnake = canvasEscenario.getGraphicsContext2D();
        gcSnake.setStroke(Color.BLACK);
        reiniciar = false;
        int velocidad = 10;
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(velocidad), accion -> mover()
        ));
        timeLine.setCycleCount(Animation.INDEFINITE);
        this.resetCanvas();

    }

    @FXML //método que usamos al pulsar en el botón
    private void ejecutar(ActionEvent event) {
        if (!reiniciar) {
            if (timeLine.getStatus() == Timeline.Status.RUNNING) {
                timeLine.stop();
            } else {
                timeLine.play();
            }
        } else {
            reset();
            if (timeLine.getStatus() == Timeline.Status.RUNNING) {
                timeLine.stop();
            }
        }

    }

    private void reset() {
        escenario = new Escenario(new Punto(0, 0),
                new Punto(canvasEscenario.getWidth() - 150,
                        canvasEscenario.getHeight() - 15));
        serpiente = new Serpiente(new Punto(100, 100), 30);
        serpiente.setEsc(escenario);
        escenario.generarEscenario(5, 7, serpiente);

        this.resetCanvas();
    }

    @FXML
    public void comenzarCanvas(KeyEvent event) {
        KeyCode kc = event.getCode();
        if (timeLine.getStatus() != Timeline.Status.RUNNING) {
            if (kc == KeyCode.UP
                    || kc == KeyCode.DOWN
                    || kc == KeyCode.W
                    || kc == KeyCode.S) {

                this.cambiarMovimiento(event);
            }
            timeLine.play();
        }

    }

    @FXML
    public void cambiarMovimiento(KeyEvent event) {
        KeyCode kc = event.getCode();
        switch (kc) {
            case UP:;
            case W:
                this.serpiente.cambiarMovimiento(Serpiente.Estado.ARRIBA);
                break;
            case LEFT:;
            case A:
                this.serpiente.cambiarMovimiento(Serpiente.Estado.IZQUIERDA);
                break;
            case RIGHT:;
            case D:
                this.serpiente.cambiarMovimiento(Serpiente.Estado.DERECHA);
                break;
            case DOWN:;
            case S:
                this.serpiente.cambiarMovimiento(Serpiente.Estado.ABAJO);
                break;
        }
    }

    //método a ejecutar periódidicamente
    void mover() {
        int paso = 1;
        serpiente.mover(paso);
        escenario.detectarChoque(serpiente, 10);
        if (serpiente.isChoque()) {
            reiniciar = true;
        }
        resetCanvas();
        if (escenario.isPartidaFinalizada()) {
            ganarLaPartida();
        }

        //serpiente.cambiarMovimiento(alto);
    }

    private void ganarLaPartida() {
        reiniciar = true;
        if (timeLine.getStatus() == Timeline.Status.RUNNING) {
            timeLine.stop();
        }
    }

    private void resetCanvas() {
        gcSnake.setFill(Color.ANTIQUEWHITE);
        gcSnake.fillRect(0, 0, canvasEscenario.getWidth(), canvasEscenario.getHeight());
        gcSnake.setFill(Color.RED);
        ArrayList<Obstaculo> obstaculos = (ArrayList) this.escenario.getStream()
                .filter(p -> p instanceof Obstaculo)
                .map(p -> (Obstaculo) p)
                .collect(Collectors.toList());
        for (Obstaculo obstaculo : obstaculos) {
            int ancho=(int)(obstaculo.getPosicion().getX()-obstaculo.getPosicionFin().getX());
            int alto=(int)(obstaculo.getPosicion().getY()-obstaculo.getPosicionFin().getY());
            gcSnake.fillRect(obstaculo.getPosicion().getX(),
                    obstaculo.getPosicion().getY(), ancho, alto);
        }
        ArrayList<Consumable> consumables = (ArrayList) this.escenario.getStream()
                .filter(p -> p instanceof Consumable)
                .map(p -> (Consumable) p)
                .collect(Collectors.toList());
        gcSnake.setFill(Color.CYAN);
        for (Consumable consumable : consumables) {
            gcSnake.fillOval(consumable.getPosicion().getX(), consumable.getPosicion().getY(), consumable.getRadio(), consumable.getRadio());
        }
        int verde = 255;
        int rojo = 0;
        for (Punto punto : this.serpiente.getCuerpo()) {
            if (!punto.equals(this.serpiente.getCabeza())) {
                gcSnake.setFill(Color.rgb(rojo, verde, 0));
                gcSnake.fillRect(punto.getX(), punto.getY(), 15, 15);
                rojo += 1;
                verde += 1;
                rojo %= 255;
                verde %= 255;
            }

        }
        gcSnake.setFill(Color.DARKGREEN);
        gcSnake.fillRect(this.serpiente.getCabeza().getX(), this.serpiente.getCabeza().getY(), 15, 15);

    }
}
