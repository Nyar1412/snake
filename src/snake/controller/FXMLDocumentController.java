/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import snake.clases.Escenario;
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
    @FXML
    private Button btnEjecutar;
    @FXML
    private Canvas canvasEscenario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        escenario = new Escenario(new Punto(0, 0)
                , new Punto(canvasEscenario.getWidth()
                        , canvasEscenario.getHeight()));
        serpiente = new Serpiente(new Punto(100, 100));
        serpiente.setEsc(escenario);
        canvasEscenario.setOnKeyPressed(event -> mover());
        gcSnake = canvasEscenario.getGraphicsContext2D();
        gcSnake.setStroke(Color.BLACK);
        int velocidad = 100;
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(velocidad), accion-> mover()
        ));  
        timeLine.setCycleCount(Animation.INDEFINITE);
    }    
    
    

    @FXML //método que usamos al pulsar en el botón
    private void ejecutar(ActionEvent event) {
        if( timeLine.getStatus() == Timeline.Status.RUNNING ){
            timeLine.stop();
        }else{
            timeLine.play();
        }
    }
    
    
    //método a ejecutar periódidicamente
    void mover(){
        int paso = 1;//cantidad que vamos a desplazar
        
        double posX = serpiente.getCabeza().getX();
        double posY = serpiente.getCabeza().getY();
        
        double ancho = canvasEscenario.getWidth() - serpiente.getCabeza().getX();
        double alto = canvasEscenario.getHeight() - serpiente.getCabeza().getY();
        posX += paso;
        posY += paso;
        resetCanvas();
        gcSnake.strokeOval(posX, posY, 1, 1);
        //controlamos que no se exceda el escenario. Si ocurre Empezamos de cero
        posX %= ancho;
        posY %= alto;
       // serpiente.mover();
        serpiente.setCabeza(new Punto(posX,posY));
        //serpiente.cambiarMovimiento(alto);
    }
    private void resetCanvas(){
        gcSnake.clearRect(0, 0, canvasEscenario.getWidth(), canvasEscenario.getHeight());
    }
}
