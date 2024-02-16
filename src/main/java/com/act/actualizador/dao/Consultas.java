/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.act.actualizador.dao;

import com.act.actualizador.servicios.ConexionMariadb;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author previotto
 */
public class Consultas {
     ConexionMariadb conexion = ConexionMariadb.getInstacia();
     
     
     
      public String obtenerRutaActualizarApp(){
        String sql = "SELECT carpeta_actualizacion FROM actualizaciones a WHERE a.id_usuario = ?;";
        
        
        try {
            int usuarioActual = obtenerUsuarioActual();
            
            if(usuarioActual != 0){
                PreparedStatement pst = conexion.conexion().prepareStatement(sql);
                pst.setInt(1, usuarioActual);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    return rs.getString("carpeta_actualizacion");
                }
            }
            
            return "";
            
        } catch (Exception e) {
            return "";
        }
        
        
    }
     
      public void guardarRutaAppBD(String ruta, String numeroActualizacion) throws SQLException{
         String sqlActualizar = "UPDATE actualizaciones \n" +
                                "SET carpeta_actualizacion = ?,\n" +
                                "numero_actualizacion = ? \n" +
                                "WHERE  id_usuario = ?;";
        
        String sqlInsertar = "INSERT \n" +
                                "INTO actualizaciones (id_actualizaciones, carpeta_actualizacion, numero_actualizacion, id_usuario) \n" +
                                "VALUES (0, ?, ?, ?);";
        
        PreparedStatement pst;
        
        try {
            int usuaurioActual = obtenerUsuarioActual();
            
            if (usuaurioActual != 0) {
                if (obtenerRutaActualizarApp().equals("")) {

                    pst = conexion.conexion().prepareStatement(sqlInsertar);

                } else {
                    pst = conexion.conexion().prepareStatement(sqlActualizar);

                }
                pst.setString(1, ruta);
                pst.setString(2, numeroActualizacion);
                pst.setInt(3, usuaurioActual);
                pst.executeUpdate();

                pst.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
      
    public int obtenerUsuarioActual(){
        try{
            String sql = "SELECT id_usuario FROM usuarios WHERE es_ultima_sesion_iniciada = true;";
            PreparedStatement pst = conexion.conexion().prepareStatement(sql);
           
            ResultSet rs = pst.executeQuery();
            List<String> esUsuario = new ArrayList();
            if(rs.next()){
                return rs.getInt("id_usuario");
            }else{
                return 0;
            }
            
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
