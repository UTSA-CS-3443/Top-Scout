package edu.utsa.cs3443.topscout.model.fields;

import java.util.ArrayList;

/**
 * An image upload area. Stores the paths of the uploaded pictures, relative to
 * the project's folder (for example "attachments/sketch.png").
 */
public class Image extends Field {

    private ArrayList<String> paths;

    public Image(String id, String label, String hint, boolean required, ArrayList<String> paths) {
        super(id, label, hint, required);
        this.paths = (paths == null) ? new ArrayList<>() : paths;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = (paths == null) ? new ArrayList<>() : paths;
    }

    public void addPath(String path) {
        paths.add(path);
    }

    @Override
    public void clear() {
        super.clear();
        paths.clear();
    }

    @Override
    public boolean isFilled() {
        return super.isFilled() && !paths.isEmpty();
    }

    @Override
    public String toString() {
        return "Image{" +
                "paths=" + paths +
                "} " + super.toString();
    }
}
