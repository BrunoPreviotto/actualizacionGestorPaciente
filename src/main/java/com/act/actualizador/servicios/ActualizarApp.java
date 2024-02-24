/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.act.actualizador.servicios;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.utils.Os;
import org.eclipse.jgit.api.Git;

/**
 *
 * @author previotto
 */
public class ActualizarApp {
    
   
    
    public String construir(String ruta) {
        // Configurar la solicitud de invocación
        InvocationRequest request = new DefaultInvocationRequest();
        
        
        
        
        request.setBaseDirectory(new File(ruta));
        //request.setPomFile(new File(ruta + "pom.xml")); // Ruta al archivo pom.xml de tu proyecto
        request.setGoals(Arrays.asList("clean", "install")); // Definir las metas Maven: clean install

        // Configurar el invocador
        DefaultInvoker invoker = new DefaultInvoker();
        
        if(Os.isFamily(Os.FAMILY_WINDOWS)){
            invoker.setMavenHome(new File("C:\\Program Files\\apache-maven-3.9.6")); 
        }else{
            invoker.setMavenHome(new File("/usr/share/maven")); 
        }
        
        
        try {
            // Invocar Maven y obtener el resultado
            InvocationResult result = invoker.execute(request);
            
            // Verificar el resultado
            if (result.getExitCode() == 0) {
                return result.toString();
            } else {
                return result.toString() + "\n Exit code: " + result.getExitCode() + "\n Exeption: " + result.getExecutionException();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
     public String mvnClean(String ruta) {
        // Configurar la solicitud de invocación
        /*InvocationRequest request = new DefaultInvocationRequest();
        
        
        
        
        request.setBaseDirectory(new File(ruta));
        //request.setPomFile(new File(ruta + "pom.xml")); // Ruta al archivo pom.xml de tu proyecto
        request.setGoals(Arrays.asList("clean")); // Definir las metas Maven: clean install

        // Configurar el invocador
        DefaultInvoker invoker = new DefaultInvoker();
        
        if(Os.isFamily(Os.FAMILY_WINDOWS)){
            invoker.setMavenHome(new File("C:\\Program Files\\apache-maven-3.9.6")); 
        }else{
            invoker.setMavenHome(new File("/usr/share/maven")); 
        }
        
        
        try {
            // Invocar Maven y obtener el resultado
            InvocationResult result = invoker.execute(request);
            
            // Verificar el resultado
            if (result.getExitCode() == 0) {
                return result.getExecutionException().toString();
            } else {
                return result.toString() + "\n Exit code: " + result.getExitCode() + "\n Exeption: " + result.getExecutionException().toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }*/
        
         // Crear una solicitud de invocación
        InvocationRequest request = new DefaultInvocationRequest();
        
        request.setBaseDirectory(new File(ruta));
        request.setGoals(Collections.singletonList("clean")); // Establecer el objetivo como "clean"
        request.setBatchMode(true);
                
        // Crear un invocador
         Invoker invoker = new DefaultInvoker();
         
         
         // Configurar un manejador de salida personalizado
        StringBuilder outputBuilder = new StringBuilder();
        invoker.setOutputHandler((line) -> {
            outputBuilder.append(line).append(System.lineSeparator());
            //System.out.println(line); // También puedes imprimir la salida en la consola si lo deseas
        });

       
            String output = outputBuilder.toString();
           
        
         

        if(Os.isFamily(Os.FAMILY_WINDOWS)){
            invoker.setMavenHome(new File("C:\\Program Files\\apache-maven-3.9.6")); 
        }else{
            invoker.setMavenHome(new File("/usr/share/maven")); 
        }
        try {
            // Ejecutar la solicitud de invocación
                
            InvocationResult resultado = invoker.execute(request);
            return "Clean ejecutado con éxito. " + output + " PrintStack: " +  resultado.getExecutionException();
        } catch (Exception e) {
            return "Error al ejecutar clean: " + e.getMessage() + " Salida: " + output;
        }
    }
     
     public String MoverContenidoCarpeta(String carpetaOrigen, String carpetaDestino, List<String> carpetaEvitar) {

        File directorioOrigen = new File(carpetaOrigen);
        File directorioDestino = new File(carpetaDestino);

        // Verificar si el directorio de destino existe, si no, créalo
        if (!directorioDestino.exists()) {
            directorioDestino.mkdirs();
        }

        // Obtener la lista de archivos en el directorio de origen
        File[] archivos = directorioOrigen.listFiles();
       
        String txtCarpetaMovida = "";
        
        // Mover cada archivo al directorio de destino
        if (archivos != null) {
            
            for (File archivo : archivos) {
                
                boolean evitarCarpeta = false;
                // Verificar si el archivo es una carpeta a evitar
               
                for (String carpeta : carpetaEvitar) {
                    if (archivo.isDirectory() && archivo.getName().equals(carpeta)) {
                        txtCarpetaMovida += "Carpeta omitida: " + archivo.getName() + "\n";
                        evitarCarpeta = true; // Omitir esta carpeta y continuar con la siguiente iteración
                       // continue;
                 }
                }
                
                if(evitarCarpeta){
                    continue;
                }
                
                 
                
                Path origenPath = archivo.toPath();
                Path destinoPath = Paths.get(directorioDestino.getAbsolutePath(), archivo.getName());
                 
                try {
                    // Mover el archivo con reemplazo si ya existe en el destino
                    Files.move(origenPath, destinoPath, StandardCopyOption.ATOMIC_MOVE);
                    if (!txtCarpetaMovida.isBlank()) {
                        txtCarpetaMovida += " ," + archivo.getName();
                    }

                } catch (Exception e) {
                    txtCarpetaMovida += "Error al mover " + archivo.getName() + ": " + e.getMessage() + "\n";
                }
            }
        }
        return txtCarpetaMovida;
    }
     
     /*public String MoverContenidoCarpeta(String carpetaOrigen, String carpetaDestino) {

        File directorioOrigen = new File(carpetaOrigen);
        File directorioDestino = new File(carpetaDestino);

        // Verificar si el directorio de destino existe, si no, créalo
        if (!directorioDestino.exists()) {
            directorioDestino.mkdirs();
        }

        // Obtener la lista de archivos en el directorio de origen
        File[] archivos = directorioOrigen.listFiles();
        String txtCarpetaMovida = "";    
        // Mover cada archivo al directorio de destino
        if (archivos != null) {
            for (File archivo : archivos) {
                Path origenPath = archivo.toPath();
                Path destinoPath = Paths.get(directorioDestino.getAbsolutePath(), archivo.getName());

                try {
                    // Mover el archivo con reemplazo si ya existe en el destino
                    Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                    if(!txtCarpetaMovida.isBlank()){
                       txtCarpetaMovida +=  " ," + archivo.getName(); 
                    }
                    
                } catch (Exception e) {
                    txtCarpetaMovida += "Error al mover " + archivo.getName() + ": " + e.getMessage();
                }
            }

        }
        return txtCarpetaMovida;
     }*/
    /* public void limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists()) {
            FileUtils.deleteDirectory(directorio);
            System.out.println("Directorio limpiado: " + directorio);
        } else {
            System.out.println("El directorio no existe: " + directorio);
        }
    }*/
     
    /* public String limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists() && directorio.isDirectory()) {
            limpiarContenidoDirectorio(directorio);
            return "Contenido del directorio limpiado: " + directorio;
        } else {
            return "El directorio no existe o no es un directorio válido: " + directorio;
        }
    }*/
     
     
      public String limpiarDirectorio(String rutaDirectorio) {
        /*Path carpetaPath = FileSystems.getDefault().getPath(rutaDirectorio);
        
         // String txt = cambiarPermisosRecursivamente(carpetaPath);
        
          File carpeta = new File(rutaDirectorio);
          if (carpeta.exists()) {
            File[] archivos = carpeta.listFiles();

            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isDirectory()) {
                        limpiarDirectorio(archivo.getAbsolutePath());
                    } else {
                        archivo.delete();
                    }
                }
            }

            carpeta.delete();
            
        }
          return   "\nCarpeta eliminada: " + carpeta.getAbsolutePath();*/
        
        
        String folderPath = rutaDirectorio; // Ruta de la carpeta a eliminar

        try {
            // Verificar si la carpeta existe antes de intentar eliminarla
            File folder = new File(folderPath);
            if (folder.exists()) {
                // Intentar forzar la eliminación de la carpeta
                FileUtils.forceDelete(folder);
                return "Carpeta eliminada con éxito.";
            } else {
                return "La carpeta no existe.";
            }
        } catch (Exception e) {
            return "Error al intentar eliminar la carpeta: " + 
                    e.toString() + " " + e.getLocalizedMessage();
            
        }
    }
      
   
    
     /* public String limpiarDirectorio(String rutaDirectorio) {
        try {
            FileUtils.deleteDirectory(new File(rutaDirectorio));
            File file = new File(rutaDirectorio);
            file.delete();
            return "Directory deleted successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }*/
      
      public String VaciarDirectorio(String rutaDirectorio, String carpetaAEvitar) {
        // Ruta del directorio que deseas vaciar
        File directorio = new File(rutaDirectorio);

        try {
            // Obtener lista de archivos y subdirectorios en el directorio
            File[] archivos = directorio.listFiles();
            if(archivos != null){
                // Verificar cada archivo/subdirectorio
                for (File archivo : archivos) {
                    // Verificar si el nombre del archivo/subdirectorio coincide con la carpeta a evitar
                    if (archivo.isDirectory() && archivo.getName().equals(carpetaAEvitar)) {
                        // Omitir la carpeta a evitar
                        continue;
                    }

                    // Eliminar el archivo/subdirectorio
                    FileUtils.forceDelete(archivo);
                }
            }
           

            return "Directorio vaciado exitosamente.";
        } catch (IOException e) {
            return "Error al vaciar el directorio: " + e.getMessage();
        }
    }
      
      
      /*public String VaciarDirectorio(String rutaDirectorio) {

        // Ruta del directorio que deseas vaciar
        File directorio = new File(rutaDirectorio);

        try {
            FileUtils.cleanDirectory(directorio);
            return "Directorio vaciado exitosamente.";
        } catch (IOException e) {
            return "Error al vaciar el directorio: " + e.getMessage();

        }
    }*
      
     public String CrearCarpetaConFileUtils(String rutaDirectorio) {

        File file = new File(rutaDirectorio);

        // Intenta crear la carpeta
        try {
            FileUtils.forceMkdir(new File(file.getAbsolutePath() + "\\paso\\gestor_pacientes"));
            return "Carpeta creada exitosamente con FileUtils.";
        } catch (IOException e) {
            return "Error al crear la carpeta con FileUtils: " + e.getMessage();
        }

    }
     
   

    public String MoverDirectorio(String rutaDirectorio) {
        File file = new File(rutaDirectorio);
        
        // Ruta del directorio de origen
        File directorioOrigen = new File(file.getAbsolutePath() + "\\paso\\gestor_pacientes");

        // Ruta del directorio de destino
        File directorioDestino = new File(file.getAbsolutePath() + "\\gestor_pacientes");

        try {
            FileUtils.moveDirectory(directorioOrigen, directorioDestino);
            return "Directorio movido exitosamente.";
        } catch (IOException e) {
            return "Error al mover el directorio: " + e.getMessage();
        }

    }

    
     
    /* public void limpiarContenidoDirectorio(File directorio) throws IOException {
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
    }*/
    
    public String clonarRepositorio(String urlRepositorio, String carpetaDestino)  {
        try {
            Path rutaDestino = Paths.get(carpetaDestino);

            File file = new File(carpetaDestino);

            Git.cloneRepository()
                    .setURI(urlRepositorio)
                    .setDirectory(new File(file.getAbsolutePath()))
                    .setBranch("master")
                    .setCloneAllBranches(true)
                    .setBranchesToClone(Collections.singletonList("refs/heads/master"))
                    .call();

            return  "Clonación exitosa del repositorio en la carpeta: " + rutaDestino;
        } catch (Exception e) {
            return "Error al clonar: " + e.getMessage();
        }
    }
    
   
}
