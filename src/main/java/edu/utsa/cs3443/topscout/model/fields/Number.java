package edu.utsa.cs3443.topscout.model.fields;

public class Number extends Field {
    double value;

    public Number(String id, String label, String hint, boolean required, double value, boolean completed) {
        super(id, label, hint, required, completed);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
}
