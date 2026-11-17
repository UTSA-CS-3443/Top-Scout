package edu.utsa.cs3443.topscout.model.fields;

public class Field {
    private String id;
    private String label;
    private String hint;
    private boolean required;
    private boolean completed;

    public Field(String id, String label, String hint, boolean required, boolean completed) {
        this.id = id;
        this.label = label;
        this.hint = hint;
        this.required = required;
        this.completed = completed;
    }

    public void clear() {
        this.id = null;
        this.label = null;
        this.hint = null;
        this.required = false;
        this.completed = false;
    }

    public String toString() {
        return "Field{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", hint='" + hint + '\'' +
                ", required=" + required +
                ", completed=" + completed +
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
    public boolean isCompleted() {
        return completed;
    }
    public  setCompleted(boolean completed) {
        this.completed = completed;
    }

}
