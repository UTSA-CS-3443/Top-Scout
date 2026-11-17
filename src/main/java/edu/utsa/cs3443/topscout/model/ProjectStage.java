package edu.utsa.cs3443.topscout.model;
import java.util.ArrayList;

/**
 * One of the three stages (The Proposal, Before The Project, After The Project). Holds a list of tasks and reports its own progress.
 */
public class ProjectStage {
    // TODO: add the attributes and methods from the UML design
    private String id;
    private String title;
    private ArrayList<Task> tasks;

    public ProjectStage(String id, String title) {
        this.id = id;
        this.title = title;
        this.tasks = new ArrayList<>();
    }
    public int getTrackedTaskCount() {
        int count = 0;
        for (Task task : tasks) {
            if (task.tracked) {
                count++;
            }
        }
        return count;
    }
    public int getCompletedTaskCount() {
        int count = 0;
        for (Task task : tasks) {
            if (task.isComplete()) {
                count++;
            }
        }
        return count;
    }
    public int getProgressPercent() {
        int trackedTaskCount = getTrackedTaskCount();
        if (trackedTaskCount == 0) {
            return 100; // If there are no tracked tasks, consider the stage complete
        }
        return (int) Math.round((double) getCompletedTaskCount() / trackedTaskCount * 100);
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
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public void addTask(Task task) {
        tasks.add(task);
    }
    @Override 
    public String toString() {
        return "ProjectStage{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tasks=" + tasks +
                '}';
    }

}
