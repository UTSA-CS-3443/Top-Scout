package edu.utsa.cs3443.topscout.model.fields;

public class Date extends TextArea {

    public Date(String id, String label, String hint, boolean required, String value) {
        super(id, label, hint, required, value);
    }

    public String toString() {
        return "Date{" +
                "date='" + getValue() + '\'' +
                "} " + super.toString();
    }

    @Override 
    public boolean setValue(String date) {
        if (date == null || !date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            return false;
        }
        return super.setValue(date);
    }
}
