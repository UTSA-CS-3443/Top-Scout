package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Project Notes screen (proposal-project-notes.fxml), a simple
 * notepad. It is not tracked, so it does not count toward progress.
 */
public class ProposalProjectNotesController extends TaskScreenController {

    /** The notepad. Linked to fx:id="textEntry". */
    @FXML
    private TextArea textEntry;

    /** Runs when the FXML finishes loading. Links the notepad to its field. */
    @FXML
    public void initialize() {
        showProjectName();
        bindText(textEntry, "notes");
    }
}
