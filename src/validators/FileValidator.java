package validators;

import java.io.File;

public class FileValidator {
    public static boolean isNull(final File file) {
        return file == null;
    }
}
