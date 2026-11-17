package edu.utsa.cs3443.topscout.model;
import edu.utsa.cs3443.topscout.model.fields.*;
import java.util.ArrayList;

/**
 * A group of fields under one heading on a task screen.
 */
public class Section {
    // TODO: add the attributes and methods from the UML design
    private String title;
    private String hint;
    private ArrayList<Field> fields;


    public Section(String title) {
        this.title = title;
        this.hint = "";
        this.fields = new ArrayList<>();
    }

    public void addField(Field field) {
        fields.add(field);
    }
    public Field getField(String id) {
        for (Field field : fields) {
            if (field.getId().equals(id)) {
                return field;
            }
        }
        return null;
    }
   
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }

}
