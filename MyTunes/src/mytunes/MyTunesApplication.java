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

    @Override
    public void start(Stage stage) throws Exception {
        //Get window.
        Stage newStage = new Stage();
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("gui/view/MainWindow.fxml"));
        Parent root = fxLoader.load();
        
        notifyPreloader(ProgressMessage.SUCCESSFULLY_DONE);
        
        //Instanciate Scene.
        Scene scene = new Scene(root);

        //Set Stage properties.
        newStage.setScene(scene);
        newStage.setTitle("MyTunes");
        newStage.getIcons().add(new Image("mytunes/gui/view/pictures/logo.png"));
        newStage.setMinWidth(836);
        newStage.setMinHeight(606);

        //Get controller and run afterInitialize.
        MainWindowController cont = fxLoader.getController();
        cont.afterInitialize();
        

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
