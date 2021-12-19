package validators;

public class NumberValidator {
    public static boolean isNumeric(final String s) {
        if (s == null) {
            return false;
        }

        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static int parseInt(final String s) {
        if (!NumberValidator.isNumeric(s)) {
            return 0;
        }

        return Integer.parseInt(s);
    }

    public static boolean minValueCheck(final int number, final int minValue) {
        return number < minValue;
    }

    public static boolean minMaxValueCheck(final int number, final int minValue, final int maxValue) {
        return number < minValue || number > maxValue;
    }
}
