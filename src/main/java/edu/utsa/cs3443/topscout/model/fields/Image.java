package edu.utsa.cs3443.topscout.model.fields;

public class Image extends TextArea {

    public Image(String id, String label, String hint, boolean required, String value, boolean completed) {
        super(id, label, hint, required, value, completed);
    }

    public String toString() {
        return "Image{" +
                "imagePath='" + getValue() + '\'' +
                "} " + super.toString();
    }

    @Override 
    public boolean setValue(String imagePath) {
        return super.setValue(imagePath);
    }
}
