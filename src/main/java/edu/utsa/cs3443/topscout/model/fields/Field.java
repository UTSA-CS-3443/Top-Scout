package edu.utsa.cs3443.topscout.model.fields;

public class Field {
    private String id;
    private String label;
    private String hint;
    private boolean required;

    public Field(String id, String label, String hint, boolean required) {
        this.id = id;
        this.label = label;
        this.hint = hint;
        this.required = required;
   }

    /** Empties the Scout's answer. A field keeps its id, label, hint, and required flag. */
    public void clear() {
        // Subclasses clear their own answer. The question itself stays.
    }

    public boolean isFilled() {
        return id != null && !id.isEmpty() &&
               label != null && !label.isEmpty();
    }

    public String toString() {
        return "Field{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", hint='" + hint + '\'' +
                ", required=" + required +
                ", completed=" + isFilled() +
                '}';
    }




    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public boolean isRequired() {
        return required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }
}