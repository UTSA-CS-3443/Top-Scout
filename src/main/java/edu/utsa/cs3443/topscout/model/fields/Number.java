package edu.utsa.cs3443.topscout.model.fields;

public class Number extends Field {
    double value;

    public Number(String id, String label, String hint, boolean required, double value) {
        super(id, label, hint, required);
        this.value = value;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    @Override 
    public void clear() {
        super.clear();
        this.value = 0.0;
    }
    @Override 
    public boolean isFilled() {
        return super.isFilled() && (value > 0.0000001 || value < -0.0000001);
    }
    @Override 
    public String toString() {
        return "Number{" +
                "value=" + value +
                "} " + super.toString();
    }

}
