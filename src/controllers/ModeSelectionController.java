package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

@SuppressWarnings("unused")
public class ModeSelectionController {

    @FXML
    private Button md5ModeButton;

    @FXML
    private Button sha1ModeButton;

    private final StageController stageController = new StageController();

    @FXML
    void handleMD5ModeButton(ActionEvent event) throws IOException {
        this.stageController.selectMode(StageController.MD5_MODE, event);
    }

    @FXML
    void handleSHA1ModeButton(ActionEvent event) throws IOException {
        this.stageController.selectMode(StageController.SHA1_MODE, event);
    }
}
