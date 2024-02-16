/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.act.actualizador.servicios;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
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
    
    
    /* public void limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists()) {
            FileUtils.deleteDirectory(directorio);
            System.out.println("Directorio limpiado: " + directorio);
        } else {
            System.out.println("El directorio no existe: " + directorio);
        }
    }*/
     
     public String limpiarDirectorio(String rutaDirectorio) throws IOException {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists() && directorio.isDirectory()) {
            limpiarContenidoDirectorio(directorio);
            return "Contenido del directorio limpiado: " + directorio;
        } else {
            return "El directorio no existe o no es un directorio válido: " + directorio;
        }
    }
     
     public void limpiarContenidoDirectorio(File directorio) throws IOException {
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
    
    public void clonarRepositorio(String urlRepositorio, String carpetaDestino) throws Exception {
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
    
   
}
