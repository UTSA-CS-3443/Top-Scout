package edu.utsa.cs3443.topscout.model.fields;

public class RadioButton extends Field {
    boolean selected;

    public RadioButton(String id, String label, String hint, boolean required, boolean selected) {
        super(id, label, hint, required);
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
