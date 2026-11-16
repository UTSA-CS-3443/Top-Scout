package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.util.Optional;

import edu.utsa.cs3443.topscout.model.ProjectStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

/**
 * Controls the project overview screen (project-overview-view.fxml). Shows the
 * project title, shows progress for the three stages, opens each stage, and
 * lets the user rename the project.
 */
public class ProjectOverviewController extends BaseScreenController implements TraversalController {

    /** Stage ids exactly as they appear in eagle_template.json. */
    private static final String PROPOSAL_ID = "proposal";
    private static final String PRE_PROJECT_ID = "before-project";
    private static final String POST_PROJECT_ID = "after-project";

    /** The project title at the top of the screen. */
    @FXML
    private Label titleLabel;

    /** One row of progress blocks for each stage. */
    @FXML
    private HBox proposalBlocks;
    @FXML
    private HBox preProjectBlocks;
    @FXML
    private HBox postProjectBlocks;

    /**
     * Runs automatically when the FXML finishes loading. Fills in the title and
     * the progress blocks for the project the user opened.
     */
    @FXML
    public void initialize() {
        // The user is looking at the whole project, so no stage or task is open
        currentStage = null;
        currentTask = null;

        titleLabel.setText(currentProject.getTitle());
        refreshStageBlocks(PROPOSAL_ID, proposalBlocks);
        refreshStageBlocks(PRE_PROJECT_ID, preProjectBlocks);
        refreshStageBlocks(POST_PROJECT_ID, postProjectBlocks);
    }

    /**
     * Called when the user clicks the Proposal stage. Opens its task checklist.
     *
     * @param event the click event from the button
     */
    @FXML
    public void proposalTasksButtonPressed(ActionEvent event) {
        openStage(PROPOSAL_ID);
    }

    /**
     * Called when the user clicks the Before The Project stage.
     * DEMO ONLY: this stage is not built yet, so it shows a message.
     * After the demo, replace the body with: openStage(PRE_PROJECT_ID);
     *
     * @param event the click event from the button
     */
    @FXML
    public void preProjectTasksButtonPressed(ActionEvent event) {
        showComingSoon();
    }

    /**
     * Called when the user clicks the After The Project stage.
     * DEMO ONLY: this stage is not built yet, so it shows a message.
     * After the demo, replace the body with: openStage(POST_PROJECT_ID);
     *
     * @param event the click event from the button
     */
    @FXML
    public void postProjectTasksButtonPressed(ActionEvent event) {
        showComingSoon();
    }

    /**
     * Called when the user clicks the edit button next to the title. Asks for a
     * new name and renames the project.
     *
     * @param event the click event from the edit button
     */
    @FXML
    public void editTitleButtonPressed(ActionEvent event) {
        // Start the popup with the current title already filled in
        TextInputDialog dialog = new TextInputDialog(currentProject.getTitle());
        dialog.setTitle("Rename Project");
        dialog.setHeaderText("Rename your project");
        dialog.setContentText("Project name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return; // the user closed the popup, so do nothing
        }

        String newTitle = result.get().trim();
        if (newTitle.isEmpty()) {
            showError("Please enter a name for the project.");
            return;
        }

        try {
            manager.renameProject(currentProject, newTitle);
            titleLabel.setText(currentProject.getTitle());
        } catch (IOException e) {
            showError("Could not rename the project.");
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
     * Called when the user clicks the back arrow. The previous screen is the
     * project list, the same place the home icon goes.
     *
     * @param event the click event from the back arrow
     */
    @Override
    @FXML
    public void backButtonPressed(ActionEvent event) {
        switchScreen("main-view.fxml");
    }

    /**
     * Remembers which stage the user picked and opens its checklist.
     *
     * @param stageId the id of the stage, such as "proposal"
     */
    private void openStage(String stageId) {
        currentStage = currentProject.getStage(stageId);
        if (currentStage == null) {
            showError("Could not find that stage in this project.");
            return;
        }
        switchScreen("stage-view.fxml");
    }

    /**
     * Fills one row of progress blocks using the stage's finished and total task counts.
     *
     * @param stageId the id of the stage to show
     * @param blocks  the HBox from the FXML file that holds the blocks
     */
    private void refreshStageBlocks(String stageId, HBox blocks) {
        ProjectStage stage = currentProject.getStage(stageId);
        if (stage != null) {
            updateProgressBlocks(blocks, stage.getCompletedTaskCount(), stage.getTrackedTaskCount());
        }
    }

    /**
     * Tells the user a stage is not available yet (demo only).
     */
    private void showComingSoon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "This stage is coming soon.", ButtonType.OK);
        alert.setTitle("Top Scout");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}