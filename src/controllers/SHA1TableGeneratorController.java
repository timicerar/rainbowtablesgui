package controllers;

import implementation.MeasureRainbowTable;
import implementation.TableGenerator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import utils.CharsetUtils;
import utils.ConsoleColorsUtils;
import utils.FileUtils;
import validators.NumberValidator;
import validators.StringValidator;

import java.io.File;
import java.io.IOException;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class SHA1TableGeneratorController {

    @FXML
    private Button backButton;

    @FXML
    private TextField chainLengthTextField;

    @FXML
    private TextField chainsPerTableTextField;

    @FXML
    private ChoiceBox<String> charsetChoice;

    @FXML
    private Button crackModeButton;

    @FXML
    private Button createTableButton;

    @FXML
    private Button createTableModeButton;

    @FXML
    private TextField maxPasswordTextField;

    @FXML
    private TextField minPasswordTextField;

    @FXML
    private Button saveMeasurementsButton;

    @FXML
    private ProgressBar tableGenerationProgressBar;

    private final StageController stageController = new StageController();

    private TableGenerator tableGenerator;

    public void initialize() {
        this.saveMeasurementsButton.setDisable(true);
        CharsetUtils.setCharsetTypes(this.charsetChoice);
    }

    @FXML
    void handleCreateTableModeButton(ActionEvent event) {
        System.out.println("Stage is " + ConsoleColorsUtils.GREEN + "ACTIVE" + ConsoleColorsUtils.RESET + "!");
    }

    @FXML
    void handleCrackModeButton(ActionEvent event) throws IOException {
        this.stageController.switchSHA1(StageController.SHA1_CRACK, event);
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        this.stageController.goBackToMainMenu(event);
    }

    @FXML
    void handleCreateTableButton(ActionEvent event) {
        if (StringValidator.isBlank(this.minPasswordTextField.getText()) || StringValidator.isBlank(this.maxPasswordTextField.getText()) || StringValidator.isBlank(this.chainsPerTableTextField.getText()) || StringValidator.isBlank(this.chainLengthTextField.getText())) {
            DialogController.SHOW_ALERT("Fill in required fields.", Alert.AlertType.WARNING);
            return;
        }

        if (!NumberValidator.isNumeric(this.minPasswordTextField.getText())) {
            DialogController.SHOW_ALERT("Minimum password length field must be numeric.", Alert.AlertType.WARNING);
            return;
        }

        if (!NumberValidator.isNumeric(this.maxPasswordTextField.getText())) {
            DialogController.SHOW_ALERT("Maximum password length field must be numeric.", Alert.AlertType.WARNING);
            return;
        }

        if (!NumberValidator.isNumeric(this.chainsPerTableTextField.getText())) {
            DialogController.SHOW_ALERT("Chains per table field must be numeric.", Alert.AlertType.WARNING);
            return;
        }

        if (!NumberValidator.isNumeric(this.chainLengthTextField.getText())) {
            DialogController.SHOW_ALERT("Chain length field must be numeric.", Alert.AlertType.WARNING);
            return;
        }

        String charsetType = this.charsetChoice.getValue();
        int minPasswordLength = NumberValidator.parseInt(this.minPasswordTextField.getText());
        int maxPasswordLength = NumberValidator.parseInt(this.maxPasswordTextField.getText());
        int chainsPerTable = NumberValidator.parseInt(this.chainsPerTableTextField.getText());
        int chainLength = NumberValidator.parseInt(this.chainLengthTextField.getText());

        if (NumberValidator.minValueCheck(minPasswordLength, 1)) {
            DialogController.SHOW_ALERT("Minimum password length field must set to at least 1.", Alert.AlertType.WARNING);
            return;
        }

        if (NumberValidator.minValueCheck(maxPasswordLength, 1)) {
            DialogController.SHOW_ALERT("Maximum password length field must set to at least 1.", Alert.AlertType.WARNING);
            return;
        }

        if (NumberValidator.minValueCheck(chainsPerTable, 1)) {
            DialogController.SHOW_ALERT("Chains per table field must set to at least 1.", Alert.AlertType.WARNING);
            return;
        }

        if (NumberValidator.minValueCheck(chainLength, 1)) {
            DialogController.SHOW_ALERT("Chain length field must set to at least 1.", Alert.AlertType.WARNING);
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());

        if (selectedDirectory == null) {
            return;
        }

        try {
            this.tableGenerator = new TableGenerator(TableGenerator.SHA1_ALGORITHM, charsetType, minPasswordLength, maxPasswordLength, chainsPerTable, chainLength);

            new Thread(() -> {
                try {
                    this.createTableButton.setDisable(true);
                    this.backButton.setDisable(true);
                    this.saveMeasurementsButton.setDisable(true);
                    this.crackModeButton.setDisable(true);

                    String info = this.tableGenerator.generateRainbowTable(selectedDirectory.getAbsolutePath(), this.tableGenerationProgressBar);

                    this.createTableButton.setDisable(false);
                    this.backButton.setDisable(false);
                    this.crackModeButton.setDisable(false);

                    this.saveMeasurementsButton.setDisable(false);

                    this.tableGenerationProgressBar.setProgress(0.0);

                    Platform.runLater(() -> DialogController.SHOW_ALERT(info, Alert.AlertType.INFORMATION));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            DialogController.SHOW_ALERT("Oops. Something went wrong.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleSaveMeasurementsButton(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());

        if (selectedDirectory == null) {
            return;
        }

        MeasureRainbowTable measureRainbowTable = this.tableGenerator.getMeasureRainbowTable();
        FileUtils.saveMeasurements(selectedDirectory.getAbsolutePath(), measureRainbowTable);
    }
}
