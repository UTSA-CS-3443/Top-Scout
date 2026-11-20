package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import edu.utsa.cs3443.topscout.model.Project;
import edu.utsa.cs3443.topscout.model.ProjectStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Controls the home screen (main-view.fxml): shows the saved projects and lets
 * the user create, open, and delete them.
 */
public class MainScreenController extends BaseScreenController {

    /** The trash icon shown on each project row. */
    private static final String TRASH_ICON = "/edu/utsa/cs3443/topscout/images/trash_button.png";

    /** The screen that shows one project's Proposal tasks. */
    private static final String OVERVIEW_SCREEN = "project-overview-view.fxml";

    /** The list of saved projects. Linked to fx:id="projectsList". */
    @FXML
    private ListView<Project> projectsList;

    /**
     * Runs automatically when the FXML finishes loading. Forgets any project
     * that was open, then fills the list with the saved projects.
     */
    @FXML
    public void initialize() {
        currentProject = null;
        currentStage = null;
        currentTask = null;

        projectsList.setCellFactory(list -> new ProjectCell());
        refreshProjectList();
    }

    /**
     * Called when the user clicks New Project. Asks for a name, creates the
     * project from the template, and opens it.
     *
     * @param event the click event
     */
    @FXML
    public void makeProject(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Name your Eagle project");
        dialog.setContentText("Project name:");
        Optional<String> answer = dialog.showAndWait();

        if (answer.isEmpty() || answer.get().isBlank()) {
            return; // cancelled or left empty
        }
        try {
            currentProject = manager.createProject(answer.get().trim());
            switchScreen(OVERVIEW_SCREEN);
        } catch (IOException e) {
            showError("The project could not be created: " + e.getMessage());
        }
    }

    /**
     * Called when the user clicks Getting Started. Shows a short explanation.
     *
     * @param event the click event
     */
    @FXML
    public void goToTutorial(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Getting Started");
        alert.setHeaderText("Welcome to Top Scout");
        alert.setContentText("Top Scout walks you through your Eagle Scout service project workbook.\n\n"
                + "1. Click New Project and give it a name.\n"
                + "2. Open each Proposal task and fill in what you know.\n"
                + "3. Press the back arrow to save and return. Your answers are saved on this computer.");
        alert.showAndWait();
    }

    /** Reloads the saved projects into the list. */
    private void refreshProjectList() {
        try {
            projectsList.getItems().setAll(manager.listProjects());
        } catch (IOException e) {
            showError("The saved projects could not be read: " + e.getMessage());
        }
    }

    /** Opens a project from the list. */
    private void openProject(Project project) {
        currentProject = project;
        switchScreen(OVERVIEW_SCREEN);
    }

    /** Asks for confirmation, then deletes a project and its files. */
    private void deleteProject(Project project) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + project.getTitle() + "\" and everything in it? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Delete Project");
        confirm.setHeaderText(null);
        Optional<ButtonType> answer = confirm.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            try {
                manager.deleteProject(project.getId());
                refreshProjectList();
            } catch (IOException e) {
                showError("The project could not be deleted: " + e.getMessage());
            }
        }
    }

    /**
     * One row of the project list: the project name, how many tasks are done,
     * and a trash button. Clicking the row opens the project.
     */
    private class ProjectCell extends ListCell<Project> {

        @Override
        protected void updateItem(Project project, boolean empty) {
            super.updateItem(project, empty);
            if (empty || project == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            Label title = new Label(project.getTitle());
            title.setStyle("-fx-font-weight: bold; -fx-text-fill: #0C256D;");

            // The demo only has The Proposal, so count just that stage
            ProjectStage proposal = project.getStage("proposal");
            Label progress = new Label(proposal.getCompletedTaskCount() + " of "
                    + proposal.getTrackedTaskCount() + " tasks done");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button trash = new Button();
            trash.setStyle("-fx-background-color: transparent;");
            URL icon = MainScreenController.class.getResource(TRASH_ICON);
            if (icon != null) {
                ImageView view = new ImageView(new Image(icon.toExternalForm()));
                view.setFitHeight(20);
                view.setFitWidth(20);
                view.setPreserveRatio(true);
                trash.setGraphic(view);
            } else {
                trash.setText("Delete");
            }
            trash.setOnAction(e -> deleteProject(project));
            trash.setOnMouseClicked(e -> e.consume()); // do not also open the project

            HBox row = new HBox(10, title, spacer, progress, trash);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setOnMouseClicked(e -> openProject(project));

            setText(null);
            setGraphic(row);
        }
    }
}
