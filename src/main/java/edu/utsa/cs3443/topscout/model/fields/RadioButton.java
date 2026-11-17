package edu.utsa.cs3443.topscout.model.fields;

public class RadioButton {
    boolean selected;

    public RadioButton(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
