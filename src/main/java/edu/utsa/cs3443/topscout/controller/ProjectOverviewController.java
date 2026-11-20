package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import edu.utsa.cs3443.topscout.model.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

/**
 * Controls the project overview screen (project-overview-view.fxml). It shows
 * the project name, the progress of The Proposal, and one button per Proposal
 * task with its own progress circle. Clicking a task opens that task's screen.
 */
public class ProjectOverviewController extends BaseScreenController implements TraversalController {

    /** The id of The Proposal stage in the template. */
    private static final String PROPOSAL = "proposal";

    private static final String HOME_SCREEN = "main-view.fxml";

    @FXML private Label nameOfProject;
    @FXML private ProgressBar proposalProgress;

    @FXML private Button contactInfo1;
    @FXML private Button projDescBenefits;
    @FXML private Button suppliesCosts;
    @FXML private Button logisticsSafety;
    @FXML private Button projectNotes;

    /**
     * Runs automatically when the FXML finishes loading. Shows the project name,
     * the overall Proposal progress, and the progress of each task button.
     */
    @FXML
    public void initialize() {
        currentStage = currentProject.getStage(PROPOSAL);
        currentTask = null;

        nameOfProject.setText(currentProject.getTitle());
        proposalProgress.setProgress(currentStage.getProgressPercent() / 100.0);

        // Task id (the same as its screen's file name) and the button that opens it
        Map<String, Button> taskButtons = new LinkedHashMap<>();
        taskButtons.put("proposal-contact-information", contactInfo1);
        taskButtons.put("proposal-project-description", projDescBenefits);
        taskButtons.put("proposal-supplies-and-cost", suppliesCosts);
        taskButtons.put("proposal-logistics-and-safety", logisticsSafety);
        taskButtons.put("proposal-project-notes", projectNotes);

        for (Map.Entry<String, Button> entry : taskButtons.entrySet()) {
            Task task = findTask(entry.getKey());
            if (task != null && entry.getValue().getGraphic() instanceof ProgressIndicator circle) {
                circle.setProgress(task.getCompletionPercent() / 100.0);
            }
        }
    }

    @FXML
    public void goToContactInfo(ActionEvent event) {
        openTask("proposal-contact-information");
    }

    @FXML
    public void goToProjDescBenefits(ActionEvent event) {
        openTask("proposal-project-description");
    }

    @FXML
    public void goToSuppliesCost(ActionEvent event) {
        openTask("proposal-supplies-and-cost");
    }

    @FXML
    public void goToLogisticsSafety(ActionEvent event) {
        openTask("proposal-logistics-and-safety");
    }

    @FXML
    public void goToProjNotes(ActionEvent event) {
        openTask("proposal-project-notes");
    }

    /**
     * Called when the user clicks Delete Project. Asks first, then deletes the
     * project and returns to the home screen.
     *
     * @param event the click event
     */
    @FXML
    public void deleteProject(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + currentProject.getTitle() + "\" and everything in it? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Delete Project");
        confirm.setHeaderText(null);
        Optional<ButtonType> answer = confirm.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            try {
                manager.deleteProject(currentProject.getId());
                switchScreen(HOME_SCREEN);
            } catch (IOException e) {
                showError("The project could not be deleted: " + e.getMessage());
            }
        }
    }

    /** Home button: returns to the project list. */
    @FXML
    @Override
    public void homeButtonPressed(ActionEvent event) {
        switchScreen(HOME_SCREEN);
    }

    /** Back arrow: also returns to the project list, since this is the first level below it. */
    @FXML
    @Override
    public void backButtonPressed(ActionEvent event) {
        switchScreen(HOME_SCREEN);
    }

    /** Finds one of the Proposal's tasks by its id, or null if the template does not have it. */
    private Task findTask(String taskId) {
        for (Task task : currentStage.getTasks()) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    /** Makes a task the open one and shows its screen (the FXML file is named after the task id). */
    private void openTask(String taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            showError("This task is not in the project template: " + taskId);
            return;
        }
        currentTask = task;
        switchScreen(taskId + ".fxml");
    }
}
