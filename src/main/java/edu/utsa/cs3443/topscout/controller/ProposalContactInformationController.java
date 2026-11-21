package edu.utsa.cs3443.topscout.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controls the Contact Information screen (proposal-contact-information.fxml):
 * the Scout, the unit, and the people involved in the project.
 */
public class ProposalContactInformationController extends TaskScreenController {

    // Eagle Scout Candidate
    @FXML private TextField ESCname, ESCdob, ESCemail, ESCbsa, ESCadd, ESCcity, ESCstate, ESCzip, ESCemail1, ESClbrd;

    // Current Unit Information
    @FXML private ToggleGroup unitType1;
    @FXML private TextField CUIunitNum, CUInameDistrict, CUInameCouncil;

    // Unit Leader
    @FXML private ToggleGroup unitType;
    @FXML private TextField ULname, ULtel, ULemail, ULadd, ULcity, ULstate, ULzip;

    // Unit Committee Chair
    @FXML private TextField UCCname, UCCtel, UCCemail, UCCadd, UCCcity, UCCstate, UCCzip;

    // Unit Advancement Coordinator
    @FXML private TextField UDCname, UACtel, UACemail, UACadd, UACcity, UACstate, UACzip;

    // Project Beneficiary
    @FXML private TextField PBname, PBtel, PBemail, PBadd, PBcity, PBstate, PBzip;

    // Project Beneficiary Representative
    @FXML private TextField PBRname, PBRtel, PBRemail, PBRadd, PBRcity, PBRstate, PBRzip;

    // Council Service Center (its email box is named UACemail111 in the FXML)
    @FXML private TextField YCSCname, YCSCtel, UACemail111, YCSCadd, YCSCcity, YCSCstate, YCSCzip;

    // Council or District Project Approval Representative
    @FXML private TextField CDPname, CDPtel, CDPemail, CDPadd, CDPcity, CDPstate, CDPzip;

    // Project Coach
    @FXML private TextField PCname, PCtel, PCemail, PCadd, PCcity, PCstate, PCzip;

    /** Runs when the FXML finishes loading. Links each box to its field. */
    @FXML
    public void initialize() {
        showProjectName();

        // Eagle Scout Candidate (ESCemail1 is the Preferred Telephone box)
        bindText(ESCname, "name");
        bindText(ESCdob, "dob");
        bindText(ESCemail, "email");
        bindText(ESCbsa, "bsa-pid");
        bindText(ESCadd, "address");
        bindText(ESCcity, "city");
        bindText(ESCstate, "state");
        bindText(ESCzip, "zip");
        bindText(ESCemail1, "phone");
        bindText(ESClbrd, "board-of-review-date");

        // Current Unit Information
        bindChoice(unitType1, "unit-type");
        bindText(CUIunitNum, "unit-number");
        bindText(CUInameDistrict, "district");
        bindText(CUInameCouncil, "council");

        // Contacts: order is name, phone, email, street, city, state, zip
        bindChoice(unitType, "unit-leader-role");
        bindContact("unit-leader", ULname, ULtel, ULemail, ULadd, ULcity, ULstate, ULzip);
        bindContact("committee-chair", UCCname, UCCtel, UCCemail, UCCadd, UCCcity, UCCstate, UCCzip);
        bindContact("advancement-coordinator", UDCname, UACtel, UACemail, UACadd, UACcity, UACstate, UACzip);
        bindContact("beneficiary", PBname, PBtel, PBemail, PBadd, PBcity, PBstate, PBzip);
        bindContact("beneficiary-rep", PBRname, PBRtel, PBRemail, PBRadd, PBRcity, PBRstate, PBRzip);
        bindContact("council-service-center", YCSCname, YCSCtel, UACemail111, YCSCadd, YCSCcity, YCSCstate, YCSCzip);
        bindContact("approval-rep", CDPname, CDPtel, CDPemail, CDPadd, CDPcity, CDPstate, CDPzip);
        bindContact("project-coach", PCname, PCtel, PCemail, PCadd, PCcity, PCstate, PCzip);
    }
}