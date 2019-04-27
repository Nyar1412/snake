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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author carlos
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button btnEjecutar;
    @FXML
    private Pane escenario;
    @FXML
    private Label lblMovil;
    
    Timeline timeline;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        int velocidad = 1;
        timeline = new Timeline(new KeyFrame(
                Duration.millis(velocidad), accion-> mover()
        ));  
        timeline.setCycleCount(Animation.INDEFINITE);

    }    
    
    

    @FXML //método que usamos al pulsar en el botón
    private void ejecutar(ActionEvent event) {
        if( timeline.getStatus() == Timeline.Status.RUNNING ){
            timeline.stop();
        }else{
            timeline.play();
        }
        
        
    }
    
    
    //método a ejecutar periódidicamente
    void mover(){
        int paso = 1;//cantidad que vamos a desplazar
        
        double posX = lblMovil.getLayoutX();
        double posY = lblMovil.getLayoutY();
        
        double ancho = escenario.getWidth() - lblMovil.getWidth();
        double alto = escenario.getHeight() - lblMovil.getHeight();
        
        posX += paso;
        posY += paso;
        
        //controlamos que no se exceda el escenario. Si ocurre Empezamos de cero
        posX %= ancho;
        posY %= alto;
        
        lblMovil.setLayoutX(posX);
        lblMovil.setLayoutY(posY);
    }
    
}
