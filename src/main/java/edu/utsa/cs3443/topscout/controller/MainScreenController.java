package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import edu.utsa.cs3443.topscout.model.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Controls the home screen (main-view.fxml). Shows the saved projects with their
 * milestone counts and lets the user create, open, and delete them.
 */
public class MainScreenController extends BaseScreenController {

    /** Folder inside src/main/resources that holds the icons. */
    private static final String IMAGE_FOLDER = "/edu/utsa/cs3443/topscout/images/";

    /** Holds one row per saved project. Linked to fx:id="projectListBox" in main-view.fxml. */
    @FXML
    private VBox projectListBox;

    /**
     * Runs automatically when main-view.fxml finishes loading. Clears whatever
     * was open before and fills the screen with the saved projects.
     */
    @FXML
    public void initialize() {
        // Nothing is open while the user is on the home screen
        currentProject = null;
        currentStage = null;
        currentTask = null;
        refreshProjectList();
    }

    /**
     * Called when the user clicks New Project. Asks for a name, creates the
     * project from the template, and opens it.
     *
     * @param event the click event from the New Project button
     */
    @FXML
    public void newProjectButtonPressed(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Name your Eagle Scout project");
        dialog.setContentText("Project name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return; // the user closed the popup, so do nothing
        }

        String title = result.get().trim();
        if (title.isEmpty()) {
            showError("Please enter a name for the project.");
            return;
        }

        try {
            currentProject = manager.createProject(title);
            currentStage = null;
            currentTask = null;
            switchScreen("project-overview-view.fxml");
        } catch (IOException e) {
            showError("Could not create the project.");
        }
    }

    /**
     * Called when the user clicks a project row. Opens that project.
     *
     * @param event the click event; its source is the row's button, which remembers its project
     */
    public void openProjectButtonPressed(ActionEvent event) {
        Button source = (Button) event.getSource();
        Project project = (Project) source.getUserData();

        currentProject = project;
        currentStage = null;
        currentTask = null;
        switchScreen("project-overview-view.fxml");
    }

    /**
     * Called when the user clicks a trash icon. Asks for confirmation, then
     * deletes that project and redraws the list.
     *
     * @param event the click event; its source is the trash button, which remembers its project
     */
    public void deleteButtonPressed(ActionEvent event) {
        Button source = (Button) event.getSource();
        Project project = (Project) source.getUserData();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + project.getTitle() + "\"? This cannot be undone.",
                ButtonType.CANCEL, ButtonType.OK);
        confirm.setTitle("Delete Project");
        confirm.setHeaderText(null);

        Optional<ButtonType> answer = confirm.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            try {
                manager.deleteProject(project.getId());
            } catch (IOException e) {
                showError("Could not delete the project.");
            }
            refreshProjectList();
        }
    }

    /**
     * Rebuilds the list from the saved projects on disk.
     */
    private void refreshProjectList() {
        projectListBox.getChildren().clear();
        try {
            ArrayList<Project> projects = manager.listProjects();
            for (Project project : projects) {
                projectListBox.getChildren().add(buildProjectRow(project));
            }
        } catch (IOException e) {
            showError("Could not load your saved projects.");
        }
    }

    /**
     * Builds one row of the list: a clickable area with the project title and
     * milestone count, and a trash button on the right.
     *
     * @param project the project this row shows
     * @return the finished row, ready to add to the list
     */
    private HBox buildProjectRow(Project project) {
        // Title over the milestone count, e.g. "3/17 Milestones Completed"
        Label titleLabel = new Label(project.getTitle());
        titleLabel.getStyleClass().add("project-title");
        Label progressLabel = new Label(project.getCompletedTaskCount() + "/"
                + project.getTrackedTaskCount() + " Milestones Completed");
        progressLabel.getStyleClass().add("project-progress");
        VBox text = new VBox(titleLabel, progressLabel);

        // The text sits inside a button so the whole area is clickable
        Button openButton = new Button();
        openButton.setGraphic(text);
        openButton.setAlignment(Pos.CENTER_LEFT);
        openButton.setMaxWidth(Double.MAX_VALUE);
        openButton.getStyleClass().add("project-row-button");
        openButton.setUserData(project); // lets the click handler know which project this is
        openButton.setOnAction(this::openProjectButtonPressed);
        HBox.setHgrow(openButton, Priority.ALWAYS); // stretch to fill the row

        // Trash button on the right
        Button deleteButton = new Button();
        deleteButton.getStyleClass().add("project-row-button");
        deleteButton.setAccessibleText("Delete " + project.getTitle());
        deleteButton.setUserData(project);
        deleteButton.setOnAction(this::deleteButtonPressed);
        InputStream iconStream = getClass().getResourceAsStream(IMAGE_FOLDER + "trash_button.png");
        if (iconStream != null) {
            ImageView icon = new ImageView(new Image(iconStream));
            icon.setFitWidth(28);
            icon.setPreserveRatio(true);
            deleteButton.setGraphic(icon);
        } else {
            deleteButton.setText("Delete"); // fallback if the image is missing
        }

        HBox row = new HBox(openButton, deleteButton);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("project-row");
        return row;
    }
}