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
 * @author janvanzetten
 */
public class MyTunesStart extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LauncherImpl.launchApplication(MyTunesPreloader.class, args);
//            }
//        });
//        t.start();
//        LauncherImpl.launchApplication(MyTunesApplication.class, args);
        LauncherImpl.launchApplication(MyTunesApplication.class, MyTunesPreloader.class, args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
    }

}
