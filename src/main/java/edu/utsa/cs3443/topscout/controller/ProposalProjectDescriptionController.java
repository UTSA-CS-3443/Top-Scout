package edu.utsa.cs3443.topscout.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * Controls the Project Description and Benefit screen
 * (proposal-project-description.fxml), with text boxes for the project summary
 * and how it helps the beneficiary, plus an upload area for images such as
 * sketches or project boards.
 */
public class ProposalProjectDescriptionController extends TaskScreenController {

    /** Linked to fx:id="summaryBox" in proposal-project-description.fxml. */
    @FXML
    private TextArea summaryBox;

    /** Linked to fx:id="benefitBox". */
    @FXML
    private TextArea benefitBox;

    /** Shows the images attached so far. Linked to fx:id="imageList". */
    @FXML
    private ListView<String> imageList;

    /**
     * Runs automatically when the FXML finishes loading. Links each control to
     * its question, so the saved answers and images appear and are saved again
     * when the user leaves the screen.
     */
    @FXML
    public void initialize() {
        bindText(summaryBox, "summary");
        bindImages(imageList, "images");
        bindText(benefitBox, "benefit");
    }

    /**
     * Called when the user clicks the upload button. Opens a file picker and
     * attaches the chosen images to the project.
     *
     * @param event the click event from the upload button
     */
    @FXML
    public void uploadButtonPressed(ActionEvent event) {
        uploadImages("images");
    }
}