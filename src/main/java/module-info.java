module com.act.actualizador {
    
    //requires javafx.base;
     requires com.jfoenix;
    //requires com.jfoenix;
    requires javafx.fxml;
    requires javafx.web;
    
    requires java.base;
    requires java.sql;
    requires com.google.common;
    
    requires javafx.controls;
    
    requires org.apache.commons.io;
    
    requires org.eclipse.jgit;
  
    
    requires java.net.http;
    
    requires maven.invoker;
    
    requires maven.shared.utils;
    
    opens com.act.actualizador to javafx.fxml, javafx.controls;
    exports com.act.actualizador;
}
