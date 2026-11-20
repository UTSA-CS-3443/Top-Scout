package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Logistics &amp; Safety screen (proposal-logistics-and-safety.fxml):
 * the transportation question and the hazards question.
 */
public class ProposalLogisticsAndSafetyController extends TaskScreenController {

    /** Transportation answer. Linked to fx:id="textEntry". */
    @FXML
    private TextArea textEntry;

    /** Hazards answer. Linked to fx:id="textEntry2". */
    @FXML
    private TextArea textEntry2;

    /** Runs when the FXML finishes loading. Links each box to its field. */
    @FXML
    public void initialize() {
        showProjectName();
        bindText(textEntry, "transportation");
        bindText(textEntry2, "hazards");
    }
}
