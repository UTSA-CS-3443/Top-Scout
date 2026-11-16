package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Supplies & Cost screen (proposal-supplies-and-cost.fxml), with
 * text boxes describing the materials, supplies, tools, other needs, and
 * permits and permissions the project will require.
 */
public class ProposalSuppliesAndCostController extends TaskScreenController {

    /** Linked to fx:id="materialsBox" in proposal-supplies-and-cost.fxml. */
    @FXML
    private TextArea materialsBox;

    /** Linked to fx:id="suppliesBox". */
    @FXML
    private TextArea suppliesBox;

    /** Linked to fx:id="toolsBox". */
    @FXML
    private TextArea toolsBox;

    /** Linked to fx:id="otherNeedsBox". */
    @FXML
    private TextArea otherNeedsBox;

    /** Linked to fx:id="permitsBox". */
    @FXML
    private TextArea permitsBox;

    /**
     * Runs automatically when the FXML finishes loading. Links each text box to
     * its question, so the saved answers appear and are saved again when the
     * user leaves the screen.
     */
    @FXML
    public void initialize() {
        bindText(materialsBox, "materials");
        bindText(suppliesBox, "supplies");
        bindText(toolsBox, "tools");
        bindText(otherNeedsBox, "other-needs");
        bindText(permitsBox, "permits");
    }
}