package edu.utsa.cs3443.topscout.model;
import edu.utsa.cs3443.topscout.model.fields.Field;
import java.util.ArrayList;

/**
 * One checklist item, which is one screen in the app. Holds its sections and can tell whether it is complete.
 */
public class Task {
    
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
    public Field getField(String id) {
        for (Section section : sections) {
            Field field = section.getField(id);
            if (field != null) {
                return field;
            }
        }
        return null;
    }
    public boolean isComplete() {
        for (Section section : sections) {
            for (Field field : section.getFields()) {
                if (field.isRequired() && !field.isFilled()) {
                    return false;
                }
            }
        }
        return true;
    }
    public int getCompletionPercent() {
        int totalFields = 0;
        int completedFields = 0;

        // Only required fields count, the same rule isComplete() uses, so 100% means the task is complete
        for (Section section : sections) {
            for (Field field : section.getFields()) {
                if (!field.isRequired()) {
                    continue;
                }
                totalFields++;
                if (field.isFilled()) {
                    completedFields++;
                }
            }
        }

        if (totalFields == 0) {
            return 100; // If nothing is required, consider it complete
        }

        return (int) Math.round((double) (completedFields * 100) / totalFields);
    }


    public ArrayList<Field> getAllFields() {
        ArrayList<Field> allFields = new ArrayList<>();
        for (Section section : sections) {
            allFields.addAll(section.getFields());
        }
        return allFields;
    }


    @Override 
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tracked=" + tracked +
                ", sections=" + sections +
                '}';
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