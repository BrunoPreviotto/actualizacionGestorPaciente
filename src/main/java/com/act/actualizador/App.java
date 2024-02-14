package com.act.actualizador;

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
         FXMLLoader Loader = new FXMLLoader(App.class.getResource( "primary.fxml"));
         Parent root = Loader.load();
        scene = new Scene(root);
        
        //scene = new Scene(loadFXML("primary"), 640, 480);
       
       
        
        stage.setScene(scene);
        
        
        
        
        
        //scenePrincipal.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.isResizable();
       // Image imagenIocono = new Image(getClass().getResourceAsStream("/com/pacientes/gestor_pacientes/img/icono.png"));
       
       // stage.getIcons().add(imagenIocono);
         
        String texto;
        try {
            Stage primaryStage = new Stage();

            // Crear el selector de directorios
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Seleccionar Directorio");

            // Mostrar el diálogo y obtener el directorio seleccionado
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            String carpetaDestino = selectedDirectory.getPath();

            String urlRepositorio = "https://github.com/BrunoPreviotto/Gestor_Pacientes.git";
            stage.show();
            //Thread.sleep(5000);
            //texto = "Limpiar: " + limpiarDirectorio(carpetaDestino);
            //clonarRepositorio(urlRepositorio, carpetaDestino);
            //texto = "Bien";
            //Thread.sleep(5000);
            texto = "\n Contruir: " + construir(carpetaDestino);
            
        } catch (Exception e) {
            e.printStackTrace();
            texto = e.getMessage() + "\n";
        }
        
        PrimaryController ps = Loader.getController();
        ps.iniciar(stage, texto);
        
    }
    
    public String construir(String ruta) {
        // Configurar la solicitud de invocación
        InvocationRequest request = new DefaultInvocationRequest();
        
        System.out.println(ruta);
        
        
        request.setBaseDirectory(new File(ruta));
        //request.setPomFile(new File(ruta + "pom.xml")); // Ruta al archivo pom.xml de tu proyecto
        request.setGoals(Arrays.asList("clean", "install")); // Definir las metas Maven: clean install

        // Configurar el invocador
        DefaultInvoker invoker = new DefaultInvoker();
        
        invoker.setMavenHome(new File("C:\\Users\\previotto\\Documents\\apache-maven-3.9.6"));
        
        try {
            // Invocar Maven y obtener el resultado
            InvocationResult result = invoker.execute(request);

            // Verificar el resultado
            if (result.getExitCode() == 0) {
                return"Proyecto Maven limpio e instalado exitosamente.";
            } else {
                return "Error al limpiar e instalar el proyecto Maven. Código de salida: " + result.getExitCode() + "\n" + result.getExecutionException();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    
    /* private static void limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists()) {
            FileUtils.deleteDirectory(directorio);
            System.out.println("Directorio limpiado: " + directorio);
        } else {
            System.out.println("El directorio no existe: " + directorio);
        }
    }*/
     
     private String limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists() && directorio.isDirectory()) {
            limpiarContenidoDirectorio(directorio);
            return "Contenido del directorio limpiado: " + directorio;
        } else {
            return "El directorio no existe o no es un directorio válido: " + directorio;
        }
    }
     
     private static void limpiarContenidoDirectorio(File directorio) throws IOException {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    FileUtils.deleteDirectory(archivo);
                } else {
                    FileUtils.forceDelete(archivo);
                }
            }
        }
    }
    
    private static void clonarRepositorio(String urlRepositorio, String carpetaDestino) throws Exception {
       Path rutaDestino = Paths.get(carpetaDestino);

        Git.cloneRepository()
                    .setURI(urlRepositorio)
                    .setDirectory(rutaDestino.toFile())
                    .setBranch("master")
                    .setCloneAllBranches(true)
                    .setBranchesToClone(Collections.singletonList("refs/heads/master"))
                    .call();
     

        System.out.println("Clonación exitosa del repositorio en la carpeta: " + rutaDestino);
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