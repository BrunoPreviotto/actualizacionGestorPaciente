package com.act.actualizador;

import com.act.actualizador.dao.Consultas;
import com.act.actualizador.servicios.ActualizarApp;
import com.act.actualizador.servicios.ConexionMariadb;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import org.eclipse.jgit.api.Git;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.utils.Os;
/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    
    
    
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("user.dir", "C:\\Users\\Public\\App_gestor_pacientes\\Act\\Actualizador\\actualizacionGestorPaciente\\target");
        
        
        FXMLLoader Loader = new FXMLLoader(App.class.getResource("primary.fxml"));
        

        Parent root = Loader.load();
        scene = new Scene(root);

        //scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);

        //scenePrincipal.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.isResizable();
        // Image imagenIocono = new Image(getClass().getResourceAsStream("/com/pacientes/gestor_pacientes/img/icono.png"));

        stage.setOnCloseRequest(event -> Platform.exit());
        // stage.getIcons().add(imagenIocono);
        stage.show();
        PrimaryController ps = Loader.getController();
        ps.iniciar();
        iniciarTarea(ps.getBarraProgreso(), ps, stage);
        
        
    }
    
    public void actualizarAppDesdeMain(PrimaryController ps, Stage stage) {

        String texto = "";

        try {
            if(Os.isFamily(Os.FAMILY_WINDOWS)){
                ActualizarApp actualizar = new ActualizarApp();
                Consultas consulta = new Consultas();

                String carpetaDestino = consulta.obtenerRutaActualizarApp();
                String urlRepositorio = "https://github.com/BrunoPreviotto/Gestor_Pacientes.git";

                Thread.sleep(5000);
                texto = actualizar.mvnClean(carpetaDestino + "\\gestor_pacientes");
                ps.rellerarMensajeTextArea(texto);

                texto = actualizar.VaciarDirectorio(carpetaDestino + "\\gestor_pacientes", "target");
                ps.rellerarMensajeTextArea(texto);

                texto = "\n" + actualizar.clonarRepositorio(urlRepositorio, carpetaDestino + "\\paso\\gestor_pacientes");
                ps.rellerarMensajeTextArea(texto);

                texto = "\n" + actualizar.construir(carpetaDestino + "\\paso\\gestor_pacientes");
                ps.rellerarMensajeTextArea(texto);

                texto = "\n" + actualizar.MoverContenidoCarpeta(carpetaDestino + "\\paso\\gestor_pacientes\\target",
                        carpetaDestino + "\\gestor_pacientes\\target", List.of(""));
                ps.rellerarMensajeTextArea(texto);

                texto = "\n" + actualizar.MoverContenidoCarpeta(carpetaDestino + "\\paso\\gestor_pacientes",
                        carpetaDestino + "\\gestor_pacientes", List.of("target", ".git"));
                ps.rellerarMensajeTextArea(texto);

                texto = "\n" + actualizar.VaciarDirectorio(carpetaDestino + "\\paso\\gestor_pacientes", ".git");
                ps.rellerarMensajeTextArea(texto);
            }
            
            
            
        } catch (Exception e) {
            mensajeError(e.getMessage());
            e.printStackTrace();

            ps.rellerarMensajeTextArea(e.getMessage());
        }

        //ps.iniciar(stage, texto);
    }
    
    public void mensajeError(String textoError) {
        try {
            FXMLLoader loaderError = new FXMLLoader(App.class.getResource("secondary.fxml"));
            Parent rootError = loaderError.load();
            Scene sceneDos = new Scene(rootError);
            Stage stage = new Stage();
            //scene = new Scene(loadFXML("primary"), 640, 480);
            stage.setScene(sceneDos);
            stage.show();

            //scenePrincipal.setFill(Color.TRANSPARENT);
            stage.setScene(sceneDos);
            //stage.initStyle(StageStyle.TRANSPARENT);

            SecondaryController sc = new SecondaryController();
            sc.iniciarTextoErro(textoError + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    
    
    private void iniciarTarea(ProgressBar progressBar, PrimaryController ps, Stage stage) {
        // Crear una tarea (Task) para realizar la función en segundo plano
        Task<Void> tarea = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Llamar a la función cuyo tiempo de ejecución deseas medir
                actualizarAppDesdeMain(ps, stage);
               
                Thread.sleep(15000);
                return null;
            }
        };

        // Vincular la propiedad de progreso de la barra de progreso con la tarea
        progressBar.progressProperty().bind(tarea.progressProperty());

        // Configurar la tarea para ejecutarse en un nuevo hilo
        Thread thread = new Thread(tarea);
        thread.start();

        // Manejar el cierre de la aplicación después de que la tarea haya terminado
        tarea.setOnSucceeded(event -> Platform.exit());
        
    }
    
    
    

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}