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

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;

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
                primaryStage.sizeToScene();
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
     * Shows a short message in the middle of the current screen while the screen
     * behind it blurs, then fades both away. Used for "Saved" after the back or
     * home button.
     *
     * @param message the text to show
     */
    protected void showToast(String message) {
        if (!(primaryStage.getScene().getRoot() instanceof AnchorPane root)
                || root.getChildren().isEmpty()) {
            return; // only screens built on an AnchorPane can show it
        }
        Node content = root.getChildren().get(0); // the screen's layout, which gets blurred

        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: #0C256D; -fx-text-fill: white; -fx-font-size: 22px; "
                + "-fx-font-weight: bold; -fx-padding: 14 40 14 40; -fx-background-radius: 24;");

        StackPane overlay = new StackPane(toast);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(255,255,255,0.35);");
        overlay.setMouseTransparent(true); // clicks pass through while it fades
        AnchorPane.setTopAnchor(overlay, 0.0);
        AnchorPane.setBottomAnchor(overlay, 0.0);
        AnchorPane.setLeftAnchor(overlay, 0.0);
        AnchorPane.setRightAnchor(overlay, 0.0);

        GaussianBlur blur = new GaussianBlur(10);
        content.setEffect(blur);
        root.getChildren().add(overlay);

        // Hold for 0.9 seconds, then fade the blur and the notice out over 0.6 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.9),
                        new KeyValue(blur.radiusProperty(), 10),
                        new KeyValue(overlay.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(blur.radiusProperty(), 0),
                        new KeyValue(overlay.opacityProperty(), 0.0)));
        timeline.setOnFinished(e -> {
            content.setEffect(null);
            root.getChildren().remove(overlay);
        });
        timeline.play();
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