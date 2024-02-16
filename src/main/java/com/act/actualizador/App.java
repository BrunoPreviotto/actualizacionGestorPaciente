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
        
        iniciarTarea(ps.getBarraProgreso(), ps, stage);
        
        
    }
    
    public void actualizarAppDesdeMain(PrimaryController ps, Stage stage){
        
        
       
        try {
            ActualizarApp actualizar = new ActualizarApp();
            Consultas consulta = new Consultas();

            String carpetaDestino = consulta.obtenerRutaActualizarApp();
            String urlRepositorio = "https://github.com/BrunoPreviotto/Gestor_Pacientes.git";

            Thread.sleep(5000);
            actualizar.limpiarDirectorio(carpetaDestino);
            actualizar.clonarRepositorio(urlRepositorio, carpetaDestino);
            
            Thread.sleep(5000);
            ps.rellerarMensajeTextArea(actualizar.construir(carpetaDestino));
            
            
          } catch (Exception e) {
            mensajeError(e.getMessage());
            e.printStackTrace();
            
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
    
   /* private void iniciarTarea(ProgressBar progressBar, PrimaryController ps, Stage stage) {
        // Crear una tarea (Task) para realizar la función en segundo plano
        Task<Void> tarea = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Obtener el tiempo de inicio
                long tiempoInicio = System.currentTimeMillis();

                // Llamar a la función cuyo tiempo de ejecución deseas medir
                actualizarAppDesdeMain(ps, stage);

                // Obtener el tiempo de finalización
                long tiempoFin = System.currentTimeMillis();

                // Calcular la diferencia de tiempo
                long tiempoTotal = tiempoFin - tiempoInicio;

                // Actualizar la barra de progreso de manera proporcional al tiempo total
                for (double progreso = 0.0; progreso <= 1.0; progreso += 0.01) {
                    updateProgress(progreso, 1.0);  // Actualizar el progreso
                    Thread.sleep((long) (tiempoTotal * 0.01));  // Ajustar la pausa según el tiempo total
                }

                // Ejecutar la operación de cierre en el hilo de JavaFX cuando la tarea haya terminado
                Platform.runLater(() -> {
                    // Cerrar la aplicación
                    ((Stage) progressBar.getScene().getWindow()).close();
                });

                return null;
            }
        };

        // Vincular la propiedad de progreso de la barra de progreso con la tarea
        progressBar.progressProperty().bind(tarea.progressProperty());

        // Configurar la tarea para ejecutarse en un nuevo hilo
        Thread thread = new Thread(tarea);
        thread.start();
        
        
        
    }*/
    
    
    private void iniciarTarea(ProgressBar progressBar, PrimaryController ps, Stage stage) {
        // Crear una tarea (Task) para realizar la función en segundo plano
        Task<Void> tarea = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Llamar a la función cuyo tiempo de ejecución deseas medir
                 actualizarAppDesdeMain(ps, stage);
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