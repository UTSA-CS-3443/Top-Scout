package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Controls the Supplies &amp; Cost screen (proposal-supplies-and-cost.fxml):
 * five text boxes for materials, supplies, tools, other needs, and permits.
 */
public class ProposalSuppliesAndCostController extends TaskScreenController {

    /** Materials. Linked to fx:id="textEntry2". */
    @FXML
    private TextArea textEntry2;

    /** Supplies. Linked to fx:id="textEntry21". */
    @FXML
    private TextArea textEntry21;

    /** Tools. Linked to fx:id="textEntry211". */
    @FXML
    private TextArea textEntry211;

    /** Other needs. Linked to fx:id="textEntry2111". */
    @FXML
    private TextArea textEntry2111;

    /** Permits. Linked to fx:id="textEntry21111". */
    @FXML
    private TextArea textEntry21111;

    /** Runs when the FXML finishes loading. Links each box to its field. */
    @FXML
    public void initialize() {
        showProjectName();
        bindText(textEntry2, "materials");
        bindText(textEntry21, "supplies");
        bindText(textEntry211, "tools");
        bindText(textEntry2111, "other-needs");
        bindText(textEntry21111, "permits");
    }
}
