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
    boolean masRojo;
    int inbulneravilidad;

    @FXML
    private Button btnEjecutar;
    @FXML
    private Canvas canvasEscenario;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        masRojo = true;
        this.inbulneravilidad = 0;
        escenario = new Escenario(new Punto(0, 0),
                new Punto(canvasEscenario.getWidth() - 5,
                        canvasEscenario.getHeight() - 10));
        serpiente = new Serpiente(new Punto(100, 100), 30);
        serpiente.setEsc(escenario);
        escenario.generarEscenario(9, 16, serpiente);
        btnEjecutar.setText("Play");
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
                btnEjecutar.setText("Pause");
                timeLine.stop();
            } else {
                btnEjecutar.setText("Play");
                timeLine.play();
            }
        } else {
            reset();
            btnEjecutar.setText("Reset");
            if (timeLine.getStatus() == Timeline.Status.RUNNING) {
                timeLine.stop();
            }
        }

    }

    private void reset() {
        escenario = new Escenario(new Punto(0, 0),
                new Punto(canvasEscenario.getWidth() - 5,
                        canvasEscenario.getHeight() - 10));
        serpiente = new Serpiente(new Punto(100, 100), 30);
        serpiente.setEsc(escenario);
        escenario.generarEscenario(9, 16, serpiente);
        this.reiniciar=false;
        this.masRojo = true;
        this.inbulneravilidad = 0;
        btnEjecutar.setText("Play");

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
        if (!serpiente.isChoque()) {
            int paso = 1;
            serpiente.mover(paso);
            if (inbulneravilidad < 40) {
                inbulneravilidad++;
            } else {
                escenario.detectarChoque(serpiente, 10);
            }
            resetCanvas();
            if (escenario.isPartidaFinalizada()) {
                ganarLaPartida();
            }

        } else {
            reiniciar = true;
        }
    }

    private void ganarLaPartida() {
        reiniciar = true;
        if (timeLine.getStatus() == Timeline.Status.RUNNING) {
            timeLine.stop();
        }
    }

    private void resetCanvas() {
        boolean masRojo = true;
        gcSnake.setFill(Color.ANTIQUEWHITE);
        gcSnake.fillRect(0, 0, canvasEscenario.getWidth(), canvasEscenario.getHeight());
        //dibujar obstaculos
        gcSnake.setStroke(Color.RED);
        gcSnake.setLineWidth(10.0);
        this.escenario.getStream()
                .filter(p -> p instanceof Obstaculo)
                .map(p -> (Obstaculo) p).forEach(obstaculo -> {
            gcSnake.strokeLine(obstaculo.getPosicion().getX(),
                    obstaculo.getPosicion().getY(),
                    obstaculo.getPosicionFin().getX(),
                    obstaculo.getPosicionFin().getY());
        });

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

                if (masRojo) {
                    verde -= 2;
                    rojo += 2;
                    if (verde == 1) {
                        verde = 0;
                        rojo = 255;
                        masRojo = false;
                    }
                } else {
                    rojo -= 2;
                    verde += 2;
                    if (rojo == 1) {
                        rojo = 0;
                        verde = 255;
                        masRojo = true;
                    }
                }
                gcSnake.setStroke(Color.rgb(rojo, verde, 0));
                gcSnake.strokeLine(punto.getX() - 3, punto.getY(), punto.getX(), punto.getY());
            }

        }
        gcSnake.setStroke(Color.rgb(0, 255, 0));
        gcSnake.strokeLine(this.serpiente.getCabeza().getX() - 4,
                this.serpiente.getCabeza().getY(),
                this.serpiente.getCabeza().getX(),
                this.serpiente.getCabeza().getY());

    }
}
