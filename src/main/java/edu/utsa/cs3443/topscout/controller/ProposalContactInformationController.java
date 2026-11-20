package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

/**
 * Controls the Contact Information screen (proposal-contact-information.fxml).
 * It collects the Eagle Scout candidate's details, the current unit information,
 * and the contact details for the unit leader, unit committee chair, unit
 * advancement coordinator, project beneficiary, beneficiary representative,
 * council service center, approval representative, and project coach.
 */
public class ProposalContactInformationController extends TaskScreenController {

    // ---- Eagle Scout candidate: one text box per question ----

    /** Linked to fx:id="nameField" in proposal-contact-information.fxml. */
    @FXML
    private TextField nameField;
    @FXML
    private TextField dobField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField bsaPidField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField zipField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField boardOfReviewField;

    // ---- Current unit information ----

    /** Radio buttons: Troop, Crew, Skipper. */
    @FXML
    private ToggleGroup unitTypeGroup;
    @FXML
    private TextField unitNumberField;
    @FXML
    private TextField districtField;
    @FXML
    private TextField councilField;

    // ---- Unit leader's role: Scoutmaster, Crew Advisor, Skipper ----

    @FXML
    private ToggleGroup unitLeaderRoleGroup;

    // ---- Contact blocks: each holds seven text fields ----

    @FXML
    private GridPane unitLeaderBox;
    @FXML
    private GridPane committeeChairBox;
    @FXML
    private GridPane advancementCoordinatorBox;
    @FXML
    private GridPane beneficiaryBox;
    @FXML
    private GridPane beneficiaryRepBox;
    @FXML
    private GridPane councilServiceCenterBox;
    @FXML
    private GridPane approvalRepBox;
    @FXML
    private GridPane projectCoachBox;

    /**
     * Runs automatically when the FXML finishes loading. Links every control to
     * its question, so the saved answers appear and are saved again when the
     * user leaves the screen.
     */
    @FXML
    public void initialize() {
        // Eagle Scout candidate
        bindText(nameField, "name");
        bindText(dobField, "dob");
        bindText(emailField, "email");
        bindText(bsaPidField, "bsa-pid");
        bindText(addressField, "address");
        bindText(cityField, "city");
        bindText(stateField, "state");
        bindText(zipField, "zip");
        bindText(phoneField, "phone");
        bindText(boardOfReviewField, "board-of-review-date");

        // Current unit information
        bindChoice(unitTypeGroup, "unit-type");
        bindText(unitNumberField, "unit-number");
        bindText(districtField, "district");
        bindText(councilField, "council");

        // Unit leader (a role plus a contact block)
        bindChoice(unitLeaderRoleGroup, "unit-leader-role");
        bindContact(unitLeaderBox, "unit-leader");

        // The other contact blocks
        bindContact(committeeChairBox, "committee-chair");
        bindContact(advancementCoordinatorBox, "advancement-coordinator");
        bindContact(beneficiaryBox, "beneficiary");
        bindContact(beneficiaryRepBox, "beneficiary-rep");
        bindContact(councilServiceCenterBox, "council-service-center");
        bindContact(approvalRepBox, "approval-rep");
        bindContact(projectCoachBox, "project-coach");
    }
}