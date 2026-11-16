package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Logistics & Safety screen (proposal-logistics-and-safety.fxml),
 * with text boxes for how materials, tools, and helpers will be transported
 * and for the hazards and safety concerns to be aware of.
 */
public class ProposalLogisticsAndSafetyController extends TaskScreenController {

    /** Linked to fx:id="transportationBox" in proposal-logistics-and-safety.fxml. */
    @FXML
    private TextArea transportationBox;

    /** Linked to fx:id="hazardsBox". */
    @FXML
    private TextArea hazardsBox;

    /**
     * Runs automatically when the FXML finishes loading. Links each text box to
     * its question, so the saved answers appear and are saved again when the
     * user leaves the screen.
     */
    @FXML
    public void initialize() {
        bindText(transportationBox, "transportation");
        bindText(hazardsBox, "hazards");
    }
}