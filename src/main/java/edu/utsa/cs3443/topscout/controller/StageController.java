package edu.utsa.cs3443.topscout.controller;

import edu.utsa.cs3443.topscout.model.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Controls the task checklist for one stage (stage-view.fxml). The same
 * controller is used for all three stages: it shows the stage title, the
 * progress blocks, and one row per task, and opens the task the user picks.
 */
public class StageController extends BaseScreenController implements TraversalController {

    /** The stage title at the top of the screen. */
    @FXML
    private Label titleLabel;

    /** One block per task, green for the finished ones. */
    @FXML
    private HBox progressBlocks;

    /** Holds one row per task. */
    @FXML
    private VBox taskListBox;

    /**
     * Runs automatically when stage-view.fxml finishes loading. Shows the
     * title, the progress, and the list of tasks for the stage the user opened.
     */
    @FXML
    public void initialize() {
        // The user is looking at the checklist, so no task is open
        currentTask = null;

        titleLabel.setText(currentStage.getTitle());
        updateProgressBlocks(progressBlocks,
                currentStage.getCompletedTaskCount(),
                currentStage.getTrackedTaskCount());

        taskListBox.getChildren().clear();
        for (Task task : currentStage.getTasks()) {
            taskListBox.getChildren().add(buildTaskRow(task));
        }
    }

    /**
     * Called when the user clicks the home icon. Returns to the project list.
     *
     * @param event the click event from the home icon
     */
    @Override
    @FXML
    public void homeButtonPressed(ActionEvent event) {
        switchScreen("main-view.fxml");
    }

    /**
     * Called when the user clicks the back arrow. Returns to the project
     * overview. The project stays open, so that screen knows what to show.
     *
     * @param event the click event from the back arrow
     */
    @Override
    @FXML
    public void backButtonPressed(ActionEvent event) {
        switchScreen("project-overview-view.fxml");
    }

    /**
     * Builds one row of the checklist: a status marker, the task title, and
     * the status text. Tasks that do not count toward progress (like Project
     * Notes) show only the title.
     *
     * @param task the task this row shows
     * @return the finished row, ready to add to the list
     */
    private HBox buildTaskRow(Task task) {
        Label titleText = new Label(task.getTitle());
        titleText.getStyleClass().add("task-title");
        VBox text = new VBox(titleText);

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);

        // Only tracked tasks get a marker and a status line
        if (task.isTracked()) {
            boolean done = task.isComplete();
            int percent = task.getCompletionPercent();

            // The marker is a small circle; topscout.css draws it based on these classes
            Label marker = new Label(done ? "\u2713" : ""); // \u2713 is a check mark
            marker.getStyleClass().add("task-marker");
            if (done) {
                marker.getStyleClass().add("task-marker-done");
            } else if (percent > 0) {
                marker.getStyleClass().add("task-marker-partial");
            }

            Label status = new Label(done ? "Completed" : percent + "%");
            status.getStyleClass().add("task-status");

            text.getChildren().add(status);
            content.getChildren().add(marker);
        }
        content.getChildren().add(text);

        // The whole row is a button so the user can click anywhere on it
        Button rowButton = new Button();
        rowButton.setGraphic(content);
        rowButton.setAlignment(Pos.CENTER_LEFT);
        rowButton.setMaxWidth(Double.MAX_VALUE);
        rowButton.getStyleClass().add("task-row-button");
        rowButton.setOnAction(event -> openTask(task));
        HBox.setHgrow(rowButton, Priority.ALWAYS); // stretch to fill the row

        HBox row = new HBox(rowButton);
        row.getStyleClass().add("task-row");
        return row;
    }

    /**
     * Remembers which task the user picked and opens its screen. Each task's
     * screen file is named after the task's id, such as
     * "proposal-contact-information.fxml".
     *
     * @param task the task to open
     */
    private void openTask(Task task) {
        currentTask = task;
        switchScreen(task.getId() + ".fxml");
    }
}