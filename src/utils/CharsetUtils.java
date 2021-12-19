package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;

public class CharsetUtils {
    public static final String LOWERCASE = "Letters (lowercase)";
    public static final String LOWER_UPPERCASE = "Letters (lower and uppercase)";
    public static final String ALPHANUMERICAL_LOWERCASE = "Alphanumerical (lowercase)";
    public static final String ALPHANUMERICAL_LOWER_UPPERCASE = "Alphanumerical (lower and uppercase)";

    public static String[] getCharsetTypes() {
        String[] charsets = new String[4];
        charsets[0] = LOWERCASE;
        charsets[1] = LOWER_UPPERCASE;
        charsets[2] = ALPHANUMERICAL_LOWERCASE;
        charsets[3] = ALPHANUMERICAL_LOWER_UPPERCASE;
        return charsets;
    }

    public static void setCharsetTypes(ChoiceBox<String> choiceBox) {
        ObservableList<String> options = FXCollections.observableArrayList(
                CharsetUtils.LOWERCASE,
                CharsetUtils.LOWER_UPPERCASE,
                CharsetUtils.ALPHANUMERICAL_LOWERCASE,
                CharsetUtils.ALPHANUMERICAL_LOWER_UPPERCASE
        );

        choiceBox.setTooltip(new Tooltip("Select charset"));
        choiceBox.setItems(options);
        choiceBox.setValue(CharsetUtils.LOWERCASE);
    }

    public static String getCharsetByType(final String charsetType) {
        switch (charsetType) {
            case CharsetUtils.LOWERCASE:
                return "qwertzuiopasdfghjklyxcvbnm";
            case CharsetUtils.LOWER_UPPERCASE:
                return "qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM";
            case CharsetUtils.ALPHANUMERICAL_LOWERCASE:
                return "qwertzuiopasdfghjklyxcvbnm1234567890";
            case CharsetUtils.ALPHANUMERICAL_LOWER_UPPERCASE:
                return "qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM1234567890";
            default:
                throw new UnsupportedOperationException("Charset " + ConsoleColorsUtils.GREEN + charsetType + ConsoleColorsUtils.RESET + " is not supported.");
        }
    }
}
