package edu.utsa.cs3443.topscout.model.fields;

public class Text extends TextArea {

    public Text(String id, String label, String hint, boolean required, String value, boolean completed) {
        super(id, label, hint, required, value, completed);
    }

    public String toString() {
        return "Text{" +
                "text='" + getValue() + '\'' +
                "} " + super.toString();
    }

    @Override 
    public boolean setValue(String text) {
        if(text.matches(".*\n.*"))
            return false;
        return super.setValue(text);
    }
}
