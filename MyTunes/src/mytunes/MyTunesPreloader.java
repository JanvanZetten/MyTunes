/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author janvanzetten
 */
public class MyTunesPreloader extends Preloader {
        
        Stage newStage;
        

    @Override
    public void start(Stage stage) throws Exception {
        newStage = new Stage();
        
        FXMLLoader fxLoader;
        fxLoader = new FXMLLoader(getClass().getResource("SplashScreen.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        System.out.println("should work");
        
        newStage.setScene(scene);
        newStage.show();
        

    }  
    
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            newStage.hide();
        }
    }

}
