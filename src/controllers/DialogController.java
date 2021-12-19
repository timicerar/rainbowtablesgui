package controllers;

import javafx.scene.control.Alert;

public class DialogController {
    public static void SHOW_ALERT(final String message, final Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        String title = alertType.toString().substring(0, 1).toUpperCase() + alertType.toString().substring(1).toLowerCase();
        alert.setTitle(title + " dialog");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
