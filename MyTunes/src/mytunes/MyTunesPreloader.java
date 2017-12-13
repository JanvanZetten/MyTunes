/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import java.io.IOException;
import java.net.URL;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author janvanzetten
 */
public class MyTunesPreloader extends Preloader {

    Stage stage;

    private Scene createPreloaderScene() throws IOException {
        FXMLLoader fxLoader;
        fxLoader = new FXMLLoader(getClass().getResource("SplashScreen.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        System.out.println("should work");
        
        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());
        stage.show();

    }

//    @Override
//    public void handleStateChangeNotification(StateChangeNotification evt) {
//        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
//            stage.hide();
//        }
//    }   
    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        ProgressMessage progressMessage = (ProgressMessage) info;

        if (progressMessage.isDone()) {
            stage.hide();
        }
    }

}
