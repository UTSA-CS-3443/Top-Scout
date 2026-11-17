package edu.utsa.cs3443.topscout.model;

import java.util.ArrayList;

/**
 * One Scout's saved Eagle project and the root of the model. Holds the stages and every answer the Scout enters.
 */
public class Project {
    // TODO: add the attributes and methods from the UML design
    private String id;
    private String title;
    private String templateId;
    private int schemaVersion;
    private String createdDate;
    private ArrayList<ProjectStage> stages;

    public Project(String title){
        this.id = title;
        this.title = title;
        this.stages = new ArrayList<>();
    }
    




    public void addStage(ProjectStage stage) {
        stages.add(stage);
    }

    // Getters and setters
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
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public int getSchemaVersion() {
        return schemaVersion;
    }
    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", templateId='" + templateId + '\'' +
                ", schemaVersion=" + schemaVersion +
                ", createdDate='" + createdDate + '\'' +
                ", stages=" + stages +
                '}';
    }

}
