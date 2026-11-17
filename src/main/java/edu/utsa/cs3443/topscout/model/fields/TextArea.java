package edu.utsa.cs3443.topscout.model.fields;

public class TextArea extends Field {
    private String value;

    public TextArea(String id, String label, String hint, boolean required, String value, boolean completed) {
        super(id, label, hint, required, completed);
        this.value = value;
    }

    public void clear() {
        super.clear();
        this.value = "";
    }

    public String toString() {
        return super.toString();
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        this.value = value;
        return true;
    }
}
