package edu.utsa.cs3443.topscout.model.fields;

public class TextArea extends Field {
    private String value;

    public TextArea(String id, String label, String hint, boolean required, String value) {
        super(id, label, hint, required);
        this.value = value;
    }

    @Override 
    public void clear() {
        super.clear();
        this.value = "";
    }
    @Override 
    public boolean isFilled() {
        return super.isFilled() && value != null && !value.isEmpty();
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        this.value = value;
        return true;
    }
    
    @Override 
    public String toString() {
        return "TextArea{" +
                "text='" + value + '\'' +
                "} " + super.toString();
    }
    protected String toStringWithoutValue() {
        return super.toString();
    }
}
