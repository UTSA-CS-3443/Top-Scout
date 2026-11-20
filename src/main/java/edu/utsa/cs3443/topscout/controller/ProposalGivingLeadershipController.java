package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controls the Giving Leadership screen in The Proposal
 * (proposal-giving-leadership.fxml), with the planned start and end dates, the
 * number of helpers needed, and text boxes for where helpers will be recruited
 * and what will be hardest about leading them.
 */
public class ProposalGivingLeadershipController extends TaskScreenController {

    /** Linked to fx:id="startDateField" in proposal-giving-leadership.fxml. */
    @FXML
    private TextField startDateField;

    /** Linked to fx:id="endDateField". */
    @FXML
    private TextField endDateField;

    /** Linked to fx:id="helpersNeededField". */
    @FXML
    private TextField helpersNeededField;

    /** Linked to fx:id="recruitingBox". */
    @FXML
    private TextArea recruitingBox;

    /** Linked to fx:id="leadingDifficultyBox". */
    @FXML
    private TextArea leadingDifficultyBox;

    /**
     * Runs automatically when the FXML finishes loading. Links each control to
     * its question, so the saved answers appear and are saved again when the
     * user leaves the screen.
     */
    @FXML
    public void initialize() {
        bindText(startDateField, "start-date");
        bindText(endDateField, "end-date");
        bindText(helpersNeededField, "helpers-needed");
        bindText(recruitingBox, "recruiting");
        bindText(leadingDifficultyBox, "leading-difficulty");
    }
}