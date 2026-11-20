package edu.utsa.cs3443.topscout.model.fields;

/**
 * A single-line text answer. A line break is not allowed in the answer.
 */
public class Text extends TextArea {

    public Text(String id, String label, String hint, boolean required, String value) {
        super(id, label, hint, required, value);
    }

    public String toString() {
        return "Text{" +
                "text='" + getValue() + '\'' +
                "} " + super.toStringWithoutValue();
    }

    @Override
    public boolean setValue(String text) {
        if (text == null || text.contains("\n") || text.contains("\r"))
            return false;
        return super.setValue(text);
    }
}
