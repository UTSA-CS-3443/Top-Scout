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
    ArrayList<ProjectStage> stages;

    public Project(String title){

    }
    public ProjectStage getStage(String stageId){}
    public int getTrackedTaskCount(){}
    public int getCompletedTaskCount(){}
    public int getProgressPercent(){}

    @Override
    public String toString(){}


}
