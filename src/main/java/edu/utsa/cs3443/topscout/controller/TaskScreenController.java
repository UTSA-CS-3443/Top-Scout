package edu.utsa.cs3443.topscout.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.utsa.cs3443.topscout.model.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

/**
 * Parent of all task screen controllers. Each task screen "binds" its controls
 * to template fields (for example bindText(recruitingBox, "recruiting")), and
 * this class does the shared work: it fills the controls with the Scout's saved
 * answers when the screen opens, checks what the user typed, copies the answers
 * back into the Task when the user leaves, saves the project, and handles the
 * image upload and the home and back buttons.
 *
 * After the demo, table support (bindTable and recalculateTotals) is added here.
 */
public abstract class TaskScreenController extends BaseScreenController implements TraversalController {

    /** The seven pieces of a contact block, in the order its text fields must appear in the FXML file. */
    private static final String[] CONTACT_KEYS =
            {"name", "telephone", "email", "street", "city", "state", "zip"};

    /** Dates are typed like 10/02/2026. STRICT rejects impossible dates such as 13/45/2026. */
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("M/d/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** Text boxes on this screen, keyed by the id of the question each one shows. */
    private final Map<String, TextInputControl> textBindings = new LinkedHashMap<>();

    /** Radio button groups on this screen, keyed by question id. */
    private final Map<String, ToggleGroup> choiceBindings = new HashMap<>();

    /** Image lists on this screen, keyed by question id. */
    private final Map<String, ListView<String>> imageBindings = new HashMap<>();

    /** Contact blocks on this screen, keyed by question id. */
    private final Map<String, Pane> contactBindings = new HashMap<>();

    // ------------------------------------------------------------------
    // Binding: each screen calls these from its initialize() method
    // ------------------------------------------------------------------

    /**
     * Links a text box to a question: shows the saved answer and remembers the
     * box so the answer can be read back when the user leaves.
     *
     * @param control the TextField or TextArea from the FXML file
     * @param fieldId the id of the question in eagle_template.json
     */
    protected void bindText(TextInputControl control, String fieldId) {
        Field field = findField(fieldId);
        if (field == null) {
            return;
        }
        String value = field.getValue();
        control.setText(value == null ? "" : value);
        textBindings.put(fieldId, control);
    }

    /**
     * Links a group of radio buttons to a question. The text of each radio
     * button must exactly match one of the question's options, such as "Troop".
     *
     * @param group   the ToggleGroup the radio buttons belong to
     * @param fieldId the id of the question in eagle_template.json
     */
    protected void bindChoice(ToggleGroup group, String fieldId) {
        Field field = findField(fieldId);
        if (field == null) {
            return;
        }
        for (Toggle toggle : group.getToggles()) {
            RadioButton button = (RadioButton) toggle;
            if (button.getText().equals(field.getValue())) {
                group.selectToggle(button);
            }
        }
        choiceBindings.put(fieldId, group);
    }

    /**
     * Links a list of image names to a question and shows the images saved so far.
     *
     * @param imageList the ListView from the FXML file
     * @param fieldId   the id of the image question in eagle_template.json
     */
    protected void bindImages(ListView<String> imageList, String fieldId) {
        Field field = findField(fieldId);
        if (field == null) {
            return;
        }
        if (field.getImages() != null) {
            imageList.getItems().setAll(field.getImages());
        }
        imageBindings.put(fieldId, imageList);
    }

    /**
     * Links a contact block to a question. The block must hold exactly seven
     * TextFields, in this order in the FXML file: name, telephone, email,
     * street, city, state, zip.
     *
     * @param block   the Pane (usually a GridPane) that holds the seven text fields
     * @param fieldId the id of the contact question in eagle_template.json
     */
    protected void bindContact(Pane block, String fieldId) {
        Field field = findField(fieldId);
        if (field == null) {
            return;
        }
        List<TextField> boxes = findTextFields(block);
        if (boxes.size() != CONTACT_KEYS.length) {
            showError("The contact block for \"" + fieldId + "\" needs "
                    + CONTACT_KEYS.length + " text fields but has " + boxes.size() + ".");
            return;
        }
        Map<String, String> contact = field.getContact();
        for (int i = 0; i < CONTACT_KEYS.length; i++) {
            String saved = (contact == null) ? null : contact.get(CONTACT_KEYS[i]);
            boxes.get(i).setText(saved == null ? "" : saved);
        }
        contactBindings.put(fieldId, block);
    }

    // ------------------------------------------------------------------
    // Image upload: a screen's upload button calls this
    // ------------------------------------------------------------------

    /**
     * Lets the user pick image files, copies them into the project's folder, and
     * adds them to the question's list of images.
     *
     * @param fieldId the id of the image question in eagle_template.json
     */
    protected void uploadImages(String fieldId) {
        Field field = findField(fieldId);
        if (field == null) {
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose images");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        List<File> files = chooser.showOpenMultipleDialog(primaryStage);
        if (files == null) {
            return; // the user closed the chooser without picking anything
        }

        try {
            ArrayList<String> images = new ArrayList<>();
            if (field.getImages() != null) {
                images.addAll(field.getImages());
            }
            for (File file : files) {
                // The manager copies the file into the project folder and returns its saved path
                images.add(manager.addAttachment(currentProject, file.toPath()));
            }
            field.setImages(images);

            ListView<String> imageList = imageBindings.get(fieldId);
            if (imageList != null) {
                imageList.getItems().setAll(images);
            }
        } catch (IOException e) {
            showError("Could not add the image.");
        }
    }

    // ------------------------------------------------------------------
    // Checking and saving
    // ------------------------------------------------------------------

    /**
     * Checks what the user typed. Empty boxes are fine (progress shows what is
     * missing), but dates must look like 10/02/2026 and numbers must be numbers.
     * Shows one popup listing every problem.
     *
     * @return true if everything typed is acceptable, false if the user must fix something
     */
    protected boolean validateInputs() {
        List<String> problems = new ArrayList<>();
        for (Map.Entry<String, TextInputControl> entry : textBindings.entrySet()) {
            Field field = currentTask.getField(entry.getKey());
            String text = entry.getValue().getText().trim();
            if (text.isEmpty()) {
                continue;
            }
            if ("date".equals(field.getType()) && !isValidDate(text)) {
                problems.add(field.getLabel() + ": enter a date like 10/02/2026.");
            } else if ("number".equals(field.getType()) && !text.matches("\\d+(\\.\\d+)?")) {
                problems.add(field.getLabel() + ": enter a number.");
            }
        }
        if (!problems.isEmpty()) {
            showError(String.join("\n\n", problems));
            return false;
        }
        return true;
    }

    /**
     * Copies every answer from the controls into the Task, then saves the whole
     * project to its file. Images were already added to the Task when uploaded.
     *
     * @return true if the project was saved, false if saving failed
     */
    protected boolean saveTask() {
        collectValues();
        try {
            manager.saveProject(currentProject);
            return true;
        } catch (IOException e) {
            showError("Could not save your answers. Please try again.");
            return false;
        }
    }

    // ------------------------------------------------------------------
    // Navigation (TraversalController)
    // ------------------------------------------------------------------

    /**
     * Called when the user clicks the home icon. Saves this task, then returns
     * to the project list. If something is wrong or the save fails, the user stays.
     *
     * @param event the click event from the home icon
     */
    @Override
    @FXML
    public void homeButtonPressed(ActionEvent event) {
        if (validateInputs() && saveTask()) {
            switchScreen("main-view.fxml");
        }
    }

    /**
     * Called when the user clicks the back arrow. Saves this task, then returns
     * to the stage checklist. If something is wrong or the save fails, the user stays.
     *
     * @param event the click event from the back arrow
     */
    @Override
    @FXML
    public void backButtonPressed(ActionEvent event) {
        if (validateInputs() && saveTask()) {
            switchScreen("stage-view.fxml");
        }
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    /**
     * Finds a question in the open task. Shows a message if the id is not in the
     * template, which usually means a typo in a screen's initialize() method.
     *
     * @param fieldId the id of the question
     * @return the question, or null if it does not exist
     */
    private Field findField(String fieldId) {
        Field field = currentTask.getField(fieldId);
        if (field == null) {
            showError("This screen is linked to a question that does not exist: " + fieldId);
        }
        return field;
    }

    /**
     * Reads every remembered control and writes its content into the matching question.
     */
    private void collectValues() {
        // Text boxes
        for (Map.Entry<String, TextInputControl> entry : textBindings.entrySet()) {
            currentTask.getField(entry.getKey()).setValue(entry.getValue().getText().trim());
        }

        // Radio buttons: the answer is the text of the selected button, or empty if none
        for (Map.Entry<String, ToggleGroup> entry : choiceBindings.entrySet()) {
            Toggle selected = entry.getValue().getSelectedToggle();
            String value = (selected == null) ? "" : ((RadioButton) selected).getText();
            currentTask.getField(entry.getKey()).setValue(value);
        }

        // Contact blocks: pair each text field with its key, in order
        for (Map.Entry<String, Pane> entry : contactBindings.entrySet()) {
            List<TextField> boxes = findTextFields(entry.getValue());
            HashMap<String, String> contact = new HashMap<>();
            for (int i = 0; i < CONTACT_KEYS.length; i++) {
                contact.put(CONTACT_KEYS[i], boxes.get(i).getText().trim());
            }
            currentTask.getField(entry.getKey()).setContact(contact);
        }
    }

    /**
     * Collects every TextField inside a container, in the order they appear in the FXML file.
     *
     * @param parent the container to search
     * @return the text fields found, in order
     */
    private List<TextField> findTextFields(Parent parent) {
        List<TextField> found = new ArrayList<>();
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof TextField) {
                found.add((TextField) child);
            } else if (child instanceof Parent) {
                found.addAll(findTextFields((Parent) child));
            }
        }
        return found;
    }

    /**
     * Checks whether text is a real date written like 10/02/2026.
     *
     * @param text the text to check
     * @return true if it is a valid date
     */
    private boolean isValidDate(String text) {
        try {
            LocalDate.parse(text, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}