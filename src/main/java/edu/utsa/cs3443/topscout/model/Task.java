package edu.utsa.cs3443.topscout.model;
import java.util.ArrayList;

/**
 * One checklist item, which is one screen in the app. Holds its sections and can tell whether it is complete.
 */
public class Task {
    // TODO: add the attributes and methods from the UML design
    String id;
    String title;
    String description;
    boolean tracked;
    ArrayList<Section> sections;

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
        this.description = "";
        this.tracked = true;
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        sections.add(section);
    }
    public Section getSection(String title) {
        for (Section section : sections) {
            if (section.getTitle().equals(title)) {
                return section;
            }
        }
        return null;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isTracked() {
        return tracked;
    }
    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }
}
