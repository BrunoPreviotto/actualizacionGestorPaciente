package com.act.actualizador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryController implements Initializable{

    
    private VBox vb;
   
    @FXML
    private ProgressBar barraProgreso;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonCerrar;
    @FXML
    private TextArea textAreaMensaje;

   

    public ProgressBar getBarraProgreso() {
        return barraProgreso;
    }
    
    public void rellerarMensajeTextArea(String texto){
        
        this.textAreaMensaje.appendText(texto);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        
    }
    
    public void iniciar(){
       textAreaMensaje.editableProperty().set(false);
       textAreaMensaje.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
    }

    @FXML
    private void minimizar(MouseEvent event) {
    }

    @FXML
    private void cerrar(MouseEvent event) {
        
        Platform.exit();
        System.exit(0);
    }
    
    
     
    
}
