package edu.utsa.cs3443.topscout.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.utsa.cs3443.topscout.model.fields.Contact;
import edu.utsa.cs3443.topscout.model.fields.Date;
import edu.utsa.cs3443.topscout.model.fields.Field;
import edu.utsa.cs3443.topscout.model.fields.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * Parent of all task screen controllers. Does the shared work: filling the
 * controls with the Scout's saved answers when a screen opens, copying the
 * answers back into the Task when the user leaves, checking what was typed,
 * saving the project, and handling the home and back buttons.
 *
 * A screen controller links each control to a field by the field's id, for
 * example bindText(summaryBox, "summary"). The model decides what kind of
 * answer each field holds (Text, TextArea, Date, Number, RadioButton, Contact),
 * so this class asks the field itself instead of keeping its own type list.
 */
public abstract class TaskScreenController extends BaseScreenController
        implements TraversalController {

    /** The screen that shows after the back arrow (the project overview with the Proposal tasks). */
    private static final String BACK_SCREEN = "project-overview-view.fxml";

    /** The home screen. */
    private static final String HOME_SCREEN = "main-view.fxml";

    /** Text boxes and the id of the field each one shows. */
    private final Map<TextInputControl, String> textBindings = new LinkedHashMap<>();

    /** Radio button groups and the id of the field each one shows. */
    private final Map<ToggleGroup, String> choiceBindings = new LinkedHashMap<>();

    /** The seven boxes of each contact block and the id of its field. */
    private final Map<String, TextField[]> contactBindings = new LinkedHashMap<>();

    /** The project name in the header of the screen (fx:id nameOfProject). Not every screen has one. */
    @FXML
    protected Label nameOfProject;

    protected void showProjectName() {
        // The header label already shows this screen's title from the FXML file
    }

    /**
     * Links a text box (TextField or TextArea) to a field. The saved answer is
     * shown now, and the box is read again when the user leaves the screen.
     * Works for text, textarea, date, notepad, and number fields.
     *
     * @param control the text box from the FXML file
     * @param fieldId the id of the field in the template
     */
    protected void bindText(TextInputControl control, String fieldId) {
        Field field = currentTask.getField(fieldId);
        if (field instanceof TextArea area) {
            control.setText(area.getValue());
        } else if (field instanceof edu.utsa.cs3443.topscout.model.fields.Number number) {
            control.setText(formatNumber(number.getValue()));
        } else {
            showError("This screen is linked to a field that does not exist or is not a text field: " + fieldId);
            return;
        }
        textBindings.put(control, fieldId);
    }

    /**
     * Links a group of radio buttons to a radio field. The button whose text
     * matches the saved answer is selected now.
     *
     * @param group   the ToggleGroup from the FXML file
     * @param fieldId the id of the field in the template
     */
    protected void bindChoice(ToggleGroup group, String fieldId) {
        Field field = currentTask.getField(fieldId);
        if (!(field instanceof edu.utsa.cs3443.topscout.model.fields.RadioButton radio)) {
            showError("This screen is linked to a field that is not a radio group: " + fieldId);
            return;
        }
        for (Toggle toggle : group.getToggles()) {
            if (toggle instanceof RadioButton button && button.getText().equals(radio.getValue())) {
                group.selectToggle(toggle);
            }
        }
        choiceBindings.put(group, fieldId);
    }

    /**
     * Links the seven boxes of a contact block to a contact field.
     *
     * @param fieldId the id of the field in the template
     * @param name    box for the person's name
     * @param phone   box for the telephone number
     * @param email   box for the email address
     * @param street  box for the street address
     * @param city    box for the city
     * @param state   box for the state
     * @param zip     box for the zip code
     */
    protected void bindContact(String fieldId, TextField name, TextField phone, TextField email,
                               TextField street, TextField city, TextField state, TextField zip) {
        Field field = currentTask.getField(fieldId);
        if (!(field instanceof Contact contact)) {
            showError("This screen is linked to a field that is not a contact: " + fieldId);
            return;
        }
        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        email.setText(contact.getEmail());
        street.setText(contact.getStreet());
        city.setText(contact.getCity());
        state.setText(contact.getState());
        zip.setText(contact.getZip());
        contactBindings.put(fieldId, new TextField[] {name, phone, email, street, city, state, zip});
    }

    /**
     * Copies everything the user typed or picked back into the fields. Shows a
     * message and returns false if something is not allowed (such as a date
     * that is not a real date), so the screen stays open.
     *
     * @return true if every answer was accepted
     */
    protected boolean collectValues() {
        for (Map.Entry<TextInputControl, String> entry : textBindings.entrySet()) {
            Field field = currentTask.getField(entry.getValue());
            String text = entry.getKey().getText() == null ? "" : entry.getKey().getText().trim();

            if (field instanceof TextArea area) {
                if (!area.setValue(text)) {
                    if (field instanceof Date) {
                        showError("\"" + field.getLabel() + "\" must be a real date written like 03/07/2026.");
                    } else {
                        showError("\"" + field.getLabel() + "\" has text that cannot be saved.");
                    }
                    return false;
                }
            } else if (field instanceof edu.utsa.cs3443.topscout.model.fields.Number number) {
                if (text.isEmpty()) {
                    number.setValue(0.0);
                } else {
                    try {
                        number.setValue(Double.parseDouble(text));
                    } catch (NumberFormatException e) {
                        showError("\"" + field.getLabel() + "\" must be a number.");
                        return false;
                    }
                }
            }
        }

        for (Map.Entry<ToggleGroup, String> entry : choiceBindings.entrySet()) {
            Field field = currentTask.getField(entry.getValue());
            Toggle selected = entry.getKey().getSelectedToggle();
            String choice = (selected instanceof RadioButton button) ? button.getText() : "";
            if (field instanceof edu.utsa.cs3443.topscout.model.fields.RadioButton radio
                    && !radio.setValue(choice)) {
                showError("\"" + choice + "\" is not one of the choices for \"" + field.getLabel() + "\".");
                return false;
            }
        }

        for (Map.Entry<String, TextField[]> entry : contactBindings.entrySet()) {
            Contact contact = (Contact) currentTask.getField(entry.getKey());
            TextField[] boxes = entry.getValue();
            contact.setName(boxes[0].getText().trim());
            contact.setPhone(boxes[1].getText().trim());
            contact.setEmail(boxes[2].getText().trim());
            contact.setStreet(boxes[3].getText().trim());
            contact.setCity(boxes[4].getText().trim());
            contact.setState(boxes[5].getText().trim());
            contact.setZip(boxes[6].getText().trim());
        }
        return true;
    }

    /**
     * Writes the whole project to its file.
     *
     * @return true if it was saved, false if a message was shown instead
     */
    protected boolean saveTask() {
        try {
            manager.saveProject(currentProject);
            return true;
        } catch (IOException e) {
            showError("Your answers could not be saved: " + e.getMessage());
            return false;
        }
    }

    /**
     * Home button: saves this task, then returns to the project list.
     *
     * @param event the click event
     */
    @FXML
    @Override
    public void homeButtonPressed(ActionEvent event) {
        if (collectValues() && saveTask()) {
            switchScreen(HOME_SCREEN);
        }
    }

    /**
     * Back arrow: saves this task, then returns to the project overview.
     *
     * @param event the click event
     */
    @FXML
    @Override
    public void backButtonPressed(ActionEvent event) {
        if (collectValues() && saveTask()) {
            switchScreen(BACK_SCREEN);
        }
    }

    /** Shows 3 instead of 3.0, and nothing at all for 0 (an empty answer). */
    private static String formatNumber(double value) {
        if (value == 0.0) {
            return "";
        }
        return (value == Math.rint(value)) ? String.valueOf((long) value) : String.valueOf(value);
    }
}