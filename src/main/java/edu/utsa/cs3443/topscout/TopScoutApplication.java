package edu.utsa.cs3443.topscout;

import java.io.IOException;
import java.net.URL;

import edu.utsa.cs3443.topscout.controller.BaseScreenController;
import edu.utsa.cs3443.topscout.model.ProjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for Top Scout. Creates the ProjectManager, shares it with the
 * controllers, and opens the home screen (main-view.fxml) in the main window.
 */
public class TopScoutApplication extends Application {

    /** Location of the home screen inside src/main/resources. */
    private static final String HOME_SCREEN = "/edu/utsa/cs3443/topscout/view/main-view.fxml";

    /** The shared stylesheet used by every screen. */
    private static final String STYLESHEET = "/edu/utsa/cs3443/topscout/css/topscout.css";

    /** Starting size of the window, in pixels. */
    private static final double WINDOW_WIDTH = 1100;
    private static final double WINDOW_HEIGHT = 800;

    /**
     * Called by JavaFX once the app has launched. Builds the window and shows
     * the home screen.
     *
     * @param primaryStage the main window, created by JavaFX
     * @throws IOException if the home screen file cannot be found or loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create the one ProjectManager and share it, plus the window, with every controller
        ProjectManager manager = new ProjectManager();
        BaseScreenController.initShared(primaryStage, manager);

        // Load the home screen layout from its FXML file
        URL homeUrl = getClass().getResource(HOME_SCREEN);
        if (homeUrl == null) {
            throw new IOException("Could not find " + HOME_SCREEN);
        }
        Parent root = new FXMLLoader(homeUrl).load();

        // Put the layout in a scene and attach the shared stylesheet
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        URL css = getClass().getResource(STYLESHEET);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        primaryStage.setTitle("Top Scout");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starts the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}