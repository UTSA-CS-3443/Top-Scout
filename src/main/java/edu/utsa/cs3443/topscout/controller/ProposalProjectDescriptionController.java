package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Project Description and Benefit screen
 * (proposal-project-description.fxml): what the project is, and how it helps
 * the beneficiary.
 */
public class ProposalProjectDescriptionController extends TaskScreenController {

    /** The project summary box. Linked to fx:id="textEntry". */
    @FXML
    private TextArea textEntry;

    /** The box for how the project helps the beneficiary. Linked to fx:id="textEntry2". */
    @FXML
    private TextArea textEntry2;

    /** Runs when the FXML finishes loading. Links each box to its field. */
    @FXML
    public void initialize() {
        showProjectName();
        bindText(textEntry, "summary");
        bindText(textEntry2, "benefit");
    }
}
