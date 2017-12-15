/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mytunes.gui.controller.MainWindowController;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class MyTunesApplication extends Application {
    private Scene scene;
    private FXMLLoader fxLoader;
    
    /**
     * this does the slow stuff because of preloader
     * @throws Exception 
     */
    @Override
    public void init() throws Exception {
        //load FXML
        fxLoader = new FXMLLoader(getClass().getResource("gui/view/MainWindow.fxml"));
        Parent root = fxLoader.load();
        
        //Instanciate Scene.
        scene = new Scene(root);
        
        //Get controller and run afterInitialize.
        MainWindowController cont = fxLoader.getController();
        cont.afterInitialize();
    }

    /**
     * shows main program window
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Get window.
        Stage newStage = new Stage();
        

        //Set Stage properties.
        newStage.setScene(scene);
        newStage.setTitle("MyTunes");
        newStage.getIcons().add(new Image("mytunes/gui/view/pictures/logo.png"));
        newStage.setMinWidth(836);
        newStage.setMinHeight(606);

        //Show Stage
        newStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
