package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageController {
    public static final int MD5_MODE = 1;
    public static final int SHA1_MODE = 2;

    public static final int MD5_CREATE = 1;
    public static final int MD5_CRACK= 2;

    public static final int SHA1_CREATE = 1;
    public static final int SHA1_CRACK= 2;

    public StageController() {
    }

    public void selectMode(int mode, ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        switch (mode) {
            case MD5_MODE:
                root = this.getParent("../stages/MD5TableGeneratorStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 330, 365);
                stage.setScene(scene);
                stage.show();
                break;
            case SHA1_MODE:
                root = this.getParent("../stages/SHA1TableGeneratorStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 330, 365);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void switchSHA1(int mode, ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        switch (mode) {
            case SHA1_CREATE:
                root = this.getParent("../stages/SHA1TableGeneratorStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 330, 365);
                stage.setScene(scene);
                stage.show();
                break;
            case SHA1_CRACK:
                root = this.getParent("../stages/SHA1CrackStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 545, 365);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void switchMD5(int mode, ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        switch (mode) {
            case SHA1_CREATE:
                root = this.getParent("../stages/MD5TableGeneratorStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 330, 365);
                stage.setScene(scene);
                stage.show();
                break;
            case SHA1_CRACK:
                root = this.getParent("../stages/MD5CrackStage.fxml");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root, 545, 365);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void goBackToMainMenu(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        root = this.getParent("../stages/ModeSelectionStage.fxml");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 469, 283);
        stage.setScene(scene);
        stage.show();
    }

    private Parent getParent(final String location) throws IOException {
        return FXMLLoader.load(getClass().getResource(location));
    }
}
