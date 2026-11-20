package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Project Notes screen (proposal-project-notes.fxml), a simple
 * notepad for the Scout's own notes. This task is not tracked, so it does not
 * count toward progress.
 */
public class ProposalProjectNotesController extends TaskScreenController {

    /** The notepad box. Linked to fx:id="notesBox" in proposal-project-notes.fxml. */
    @FXML
    private TextArea notesBox;

    /**
     * Runs automatically when the FXML finishes loading. Links the notepad box
     * to the "notes" question, so its saved text appears and is saved again
     * when the user leaves the screen.
     */
    @FXML
    public void initialize() {
        bindText(notesBox, "notes");
    }
}