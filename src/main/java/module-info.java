module edu.utsa.cs3443.topscout {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens edu.utsa.cs3443.topscout.controller to javafx.fxml;
    opens edu.utsa.cs3443.topscout.model to com.google.gson;
    exports edu.utsa.cs3443.topscout;
}