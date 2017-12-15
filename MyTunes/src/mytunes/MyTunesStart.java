/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class MyTunesStart extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LauncherImpl.launchApplication(MyTunesApplication.class, MyTunesPreloader.class, args); // not supportet in java 9

    }

    
    @Override
    public void start(Stage primaryStage) throws Exception { //JavaFX wants this before it can be standard applictaion start
        
    }

}
