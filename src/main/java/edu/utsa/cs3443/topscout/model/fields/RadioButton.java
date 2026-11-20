package edu.utsa.cs3443.topscout.model.fields;

import java.util.ArrayList;

/**
 * A group of radio buttons where the Scout picks one choice, such as Troop,
 * Crew, or Skipper. Stores the choices and the one that is selected.
 */
public class RadioButton extends Field {

    private ArrayList<String> options;
    private String value;

    public RadioButton(String id, String label, String hint, boolean required,
                       ArrayList<String> options, String value) {
        super(id, label, hint, required);
        this.options = (options == null) ? new ArrayList<>() : options;
        this.value = (value == null) ? "" : value;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getValue() {
        return value;
    }

    /**
     * Selects a choice. The value must be empty (nothing selected) or one of the options.
     *
     * @return true if accepted, false if the value is not one of the options
     */
    public boolean setValue(String value) {
        if (value == null || (!value.isEmpty() && !options.contains(value))) {
            return false;
        }
        this.value = value;
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        this.value = "";
    }

    @Override
    public boolean isFilled() {
        return super.isFilled() && !value.isEmpty();
    }

    @Override
    public String toString() {
        return "RadioButton{" +
                "options=" + options +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}
