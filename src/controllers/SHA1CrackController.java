package controllers;

import implementation.PasswordCracker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import utils.CharsetUtils;
import utils.CommonUtils;
import utils.ConsoleColorsUtils;
import validators.FileValidator;
import validators.NumberValidator;
import validators.StringValidator;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings({"unused", "DuplicatedCode"})
public class SHA1CrackController {

    @FXML
    private Button backButton;

    @FXML
    private TextField chainLengthTextField;

    @FXML
    private TextField chainsPerTableTextField;

    @FXML
    private ChoiceBox<String> charsetChoice;

    @FXML
    private TextField crackHashTextField;

    @FXML
    private Button crackModeButton;

    @FXML
    private Button createTableModeButton;

    @FXML
    private TextField generateHashTextField;

    @FXML
    private Button hashButton;

    @FXML
    private Button crackHashButton;

    @FXML
    private TextField maxPasswordTextField;

    @FXML
    private TextField minPasswordTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button readTableButton;

    @FXML
    private Button selectFileButton;

    @FXML
    private Label selectedFileLabel;

    @FXML
    private Label isCrackedLabel;

    private final StageController stageController = new StageController();

    private MessageDigest messageDigest;

    private PasswordCracker passwordCracker;

    private File selectedFile;

    public void initialize() throws NoSuchAlgorithmException {
        this.messageDigest = CommonUtils.getMessageDigest("SHA-1");
        this.crackHashButton.setDisable(true);
        CharsetUtils.setCharsetTypes(this.charsetChoice);
    }

    @FXML
    void handleCreateTableModeButton(ActionEvent event) throws IOException {
        this.stageController.switchSHA1(StageController.SHA1_CREATE, event);
    }

    @FXML
    void handleCrackModeButton(ActionEvent event) {
        System.out.println("Stage is " + ConsoleColorsUtils.GREEN + "ACTIVE" + ConsoleColorsUtils.RESET + "!");
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        this.stageController.goBackToMainMenu(event);
    }

    @FXML
    void handleReadTableButton(ActionEvent event) {
        if (FileValidator.isNull(this.selectedFile)) {
            DialogController.SHOW_ALERT("File (rainbow table) must be selected.", Alert.AlertType.WARNING);
            return;
        }

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

        try {
            new Thread(() -> {
                try {
                    this.readTableButton.setDisable(true);
                    this.backButton.setDisable(true);
                    this.createTableModeButton.setDisable(true);
                    this.crackHashButton.setDisable(true);

                    this.passwordCracker = new PasswordCracker(PasswordCracker.SHA1_ALGORITHM, this.selectedFile, charsetType, minPasswordLength, maxPasswordLength, chainsPerTable, chainLength);

                    this.readTableButton.setDisable(false);
                    this.backButton.setDisable(false);
                    this.createTableModeButton.setDisable(false);
                    this.crackHashButton.setDisable(false);
                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            DialogController.SHOW_ALERT("Oops. Something went wrong.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleCrackHashButton(ActionEvent event) {
        if (StringValidator.isBlank(this.crackHashTextField.getText())) {
            DialogController.SHOW_ALERT("Please enter a hash you want to crack.", Alert.AlertType.WARNING);
            return;
        }

        if (this.passwordCracker == null) {
            DialogController.SHOW_ALERT("Please select a table before cracking a hash.", Alert.AlertType.WARNING);
            return;
        }

        try {
            new Thread(() -> {
                this.readTableButton.setDisable(true);
                this.createTableModeButton.setDisable(true);
                this.backButton.setDisable(true);
                this.crackHashButton.setDisable(true);

                boolean isPasswordCracked = this.passwordCracker.crackHash(this.crackHashTextField.getText());

                this.readTableButton.setDisable(false);
                this.createTableModeButton.setDisable(false);
                this.backButton.setDisable(false);
                this.crackHashButton.setDisable(false);

                Platform.runLater(() -> {
                    if (isPasswordCracked) {
                        this.isCrackedLabel.setText("Password was found." + "\n" + "Hash: " + this.crackHashTextField.getText() + "\n" + "Cracked password: " + this.passwordCracker.getPlainText());
                    } else {
                        this.isCrackedLabel.setText("Password was not found.");
                    }
                });
            }).start();
        } catch (Exception e) {
            DialogController.SHOW_ALERT("Oops. Something went wrong.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleHashButton(ActionEvent event) {
        if (StringValidator.isBlank(this.passwordTextField.getText())) {
            DialogController.SHOW_ALERT("Password field is required to generate a hash.", Alert.AlertType.WARNING);
            return;
        }

        this.messageDigest.update(this.passwordTextField.getText().getBytes());
        byte[] digest = this.messageDigest.digest();

        String hash = CommonUtils.bytesToHex(digest);

        this.generateHashTextField.setText(hash);
    }

    @FXML
    void handleSelectFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Table files (*.tbl)", "*.tbl");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());

        if (selectedFile == null) {
            return;
        }

        this.selectedFile = selectedFile;
        this.selectedFileLabel.setText(selectedFile.getName());
    }
}
