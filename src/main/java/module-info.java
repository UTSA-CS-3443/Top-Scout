module edu.utsa.cs3443.topscout {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.utsa.cs3443.topscout.controller to javafx.fxml;
    exports edu.utsa.cs3443.topscout;
}