package com.act.actualizador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryController implements Initializable{

    
    private VBox vb;
    private TextArea ta;
    
    @FXML
    private void switchToSecondary() throws IOException {
       // App.setRoot("secondary");
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
    
}
