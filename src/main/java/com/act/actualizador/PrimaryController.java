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
    private TextArea ta;
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
        this.textAreaMensaje.setText(texto);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TextArea ta = new TextArea();
        
        
    }
    
    public void iniciar(Stage stage, String texto){
       vb = (VBox)stage.getScene().getRoot();
       ta = new TextArea(texto);
       vb.getChildren().add(ta);
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
