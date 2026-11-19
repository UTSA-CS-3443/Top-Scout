package edu.utsa.cs3443.topscout;

import edu.utsa.cs3443.topscout.controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for Top Scout. Will create the ProjectManager, set up the main
 * window, and open the home screen (main-view.fxml).
 */
public class TopScoutApplication extends Application {

    //loads the main screen, setting up scene and stage
    @Override
    public void start(Stage primaryStage) throws IOException {
        // TODO: create the ProjectManager and show main-view.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(TopScoutApplication.class.getResource("view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainScreenController mainController = fxmlLoader.getController();
        mainController.setStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
