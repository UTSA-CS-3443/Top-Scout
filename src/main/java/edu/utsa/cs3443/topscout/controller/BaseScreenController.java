package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.net.URL;

import edu.utsa.cs3443.topscout.model.Project;
import edu.utsa.cs3443.topscout.model.ProjectManager;
import edu.utsa.cs3443.topscout.model.ProjectStage;
import edu.utsa.cs3443.topscout.model.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Parent of all screen controllers. Holds the objects every screen shares
 * (the window, the ProjectManager, and the project, stage, and task the user
 * has open) and provides the helpers for switching screens, drawing progress
 * blocks, and showing errors.
 */
public abstract class BaseScreenController {

    /** Folder inside src/main/resources that holds every FXML file. */
    private static final String VIEW_FOLDER = "/edu/utsa/cs3443/topscout/view/";

    /** The shared stylesheet used by every screen. */
    private static final String STYLESHEET = "/edu/utsa/cs3443/topscout/css/topscout.css";

    /** The one window the whole app lives in. */
    protected static Stage primaryStage;

    /** Reads and writes all project files. */
    protected static ProjectManager manager;

    /** The project the user currently has open (null on the home screen). */
    protected static Project currentProject;

    /** The stage (The Proposal, etc.) the user currently has open. */
    protected static ProjectStage currentStage;

    /** The task the user currently has open. */
    protected static Task currentTask;

    /**
     * Stores the window and the ProjectManager so every controller can use them.
     * TopScoutApplication calls this once when the app starts.
     *
     * @param stage   the main window JavaFX creates at launch
     * @param manager the ProjectManager that handles all file reading and writing
     */
    public static void initShared(Stage stage, ProjectManager manager) {
        primaryStage = stage;
        // The class name tells Java we mean the shared field, not this parameter
        BaseScreenController.manager = manager;
    }

    /**
     * Loads an FXML file and shows it in the main window.
     *
     * @param fxmlFile the file name only, such as "main-view.fxml"
     */
    protected void switchScreen(String fxmlFile) {
        URL fxmlUrl = getClass().getResource(VIEW_FOLDER + fxmlFile);
        if (fxmlUrl == null) {
            showError("Could not find the screen " + fxmlFile);
            return;
        }
        try {
            Parent root = new FXMLLoader(fxmlUrl).load();
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                // First screen: create the scene and attach the shared stylesheet once
                scene = new Scene(root);
                URL css = getClass().getResource(STYLESHEET);
                if (css != null) {
                    scene.getStylesheets().add(css.toExternalForm());
                }
                primaryStage.setScene(scene);
            } else {
                // Later screens: swap the content so the window keeps its size
                scene.setRoot(root);
            }
            primaryStage.show();
        } catch (IOException e) {
            showError("Could not open the screen " + fxmlFile);
        }
    }

    /**
     * Fills a row with one block per task, coloring the finished ones green.
     * The actual look (size and colors) comes from topscout.css.
     *
     * @param blocks    the HBox from the FXML file that holds the blocks
     * @param completed how many tasks are finished
     * @param total     how many tasks there are
     */
    protected void updateProgressBlocks(HBox blocks, int completed, int total) {
        blocks.getChildren().clear();
        for (int i = 0; i < total; i++) {
            Region block = new Region();
            block.getStyleClass().add("progress-block");
            if (i < completed) {
                block.getStyleClass().add("progress-block-done");
            }
            blocks.getChildren().add(block);
        }
    }

    /**
     * Shows a popup with an error message and waits for the user to close it.
     *
     * @param message what went wrong, in plain language
     */
    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Top Scout");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}