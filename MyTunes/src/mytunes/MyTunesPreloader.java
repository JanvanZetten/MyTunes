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
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Alex, Asbjørn og Jan
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
        
        newStage.setScene(scene);
        newStage.setTitle("MyTunes");
        newStage.getIcons().add(new Image("mytunes/gui/view/pictures/logo.png"));
        newStage.setResizable(false);
        newStage.show();
    }  
    
    /**
     * Closes the window when application starts
     * @param scn 
     */
    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            newStage.hide();
        }
    }

}
