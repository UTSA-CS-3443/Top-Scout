package edu.utsa.cs3443.topscout.controller;

import edu.utsa.cs3443.topscout.model.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the home screen: shows the saved projects and lets the user create, open, and delete them.
 */
public class MainScreenController extends BaseScreenController implements Initializable {
    // TODO: add the attributes and methods from the UML design
    @FXML
    private ListView<String> projectsList;

    private ObservableList<String> projects =
            FXCollections.observableArrayList();

    @FXML
    private Button tutorialButton;

    @FXML
    private Button newProject;

    /*TODO: Change all this String to Project once that class is made.
    To make this more proper it would need a getter for the project name and
    able to connect the json file to the project
     */
    //Im realizing that having a delete option INSIDE every option would be torture to code.

    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){

        //TODO: When a new project is made, it should make a json file
        /*new project made = "New Project" selected
        then when saved, the project appears on the list with a name,
        inserted before the new project button.
         */

        // Dummy projects for testing the view
        projects.addAll("Project 1", "Project 2", "Project 3");

        projectsList.setItems(projects);

        projectsList.setCellFactory(listView -> new ListCell<String>() {

            @Override
            protected void updateItem(String project, boolean empty) {
                super.updateItem(project, empty);

                if (empty || project == null) {
                    setGraphic(null);
                } else {
                    setGraphic(buildProjectRow(project));
                }
            }
        });

    }

    private HBox buildProjectRow(String project) {

        HBox row = new HBox(15);

        //Label projectName = new Label(project);

        Button openButton = new Button(project);
        openButton.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-text-fill:  #0C256D;"
        );

        openButton.setOnAction(event -> {
            openProject(project);
        });

        row.getChildren().addAll(openButton);

        return row;
    }


    private void openProject(String project) {
        System.out.println("project opened");
        switchToProjectOverview();
    }

    @FXML
    void goToTutorial(ActionEvent event) {

    }

    @FXML
    void makeProject(ActionEvent event) {
        //TODO: have some kind of indicator that the project is new I guess?
        switchToProjectOverview();
    }

    public void switchToProjectOverview() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/project-overview-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MainScreenController mainScreenController = fxmlLoader.getController();
            mainScreenController.setStage(stage);

            stage.setScene(scene);
            stage.show();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
