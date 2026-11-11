package edu.utsa.cs3443.topscout.controller;

import javafx.event.ActionEvent;

/**
 * Navigation buttons shared by most screens: the home button and the back button.
 * Any controller that implements this interface promises to handle both buttons.
 */
public interface TraversalController {

    /**
     * Called when the user clicks the home icon. Should return to the home screen.
     *
     * @param event the click event that JavaFX passes in
     */
    void homeButtonPressed(ActionEvent event);

    /**
     * Called when the user clicks the back arrow. Should return to the previous screen.
     *
     * @param event the click event that JavaFX passes in
     */
    void backButtonPressed(ActionEvent event);
}