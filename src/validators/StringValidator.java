package validators;

public class StringValidator {
    public static boolean isBlank(String s) {
        return s.isBlank() || s.isEmpty();
    }
}
