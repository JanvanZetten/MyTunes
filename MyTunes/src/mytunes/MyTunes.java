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
import javafx.stage.Stage;
import mytunes.gui.controller.MainWindowController;

/**
 *
 * @author janvanzetten
 */
public class MyTunes extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        //Get window.
        Stage newStage = new Stage();
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("gui/view/MainWindow.fxml"));
        Parent root = fxLoader.load();

        //Instanciate Scene.
        Scene scene = new Scene(root);

        //Set Stage properties.
        newStage.setScene(scene);
        newStage.setTitle("MyTunes");
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
    public static void main(String[] args)
    {
        launch(args);
    }

}
