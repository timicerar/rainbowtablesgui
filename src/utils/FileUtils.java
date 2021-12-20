package utils;

import controllers.DialogController;
import implementation.MeasureRainbowTable;
import implementation.hash.Bytes;
import implementation.hash.HashTable;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Date;

public class FileUtils {
    public static BufferedOutputStream getBufferedOutputStream(final File file) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public static BufferedInputStream getBufferedInputStream(final File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public static HashTable readFile(BufferedInputStream inputStream, final int chainsPerTable, final int minPasswordLength, final int maxPasswordLength) throws IOException {
        // Reads a previously opened table file into a hashmap.
        HashTable hashTable = new HashTable(chainsPerTable / 5);
        int i = 0, availableIterations;
        availableIterations = inputStream.available();

        while (true) {
            int passwordLength = i % (maxPasswordLength - minPasswordLength + 1) + minPasswordLength;
            byte[] startingPoint = new byte[passwordLength];
            byte[] endPoint = new byte[passwordLength];

            try {
                availableIterations -= (passwordLength * 2);

                if (availableIterations < 50) {
                    if (inputStream.available() < startingPoint.length + endPoint.length) {
                        break;
                    }
                }

                inputStream.read(startingPoint);
                inputStream.read(endPoint);

                hashTable.insert(new Bytes(endPoint), new Bytes(startingPoint));
                i++;
            } catch (EOFException e) {
                return hashTable;
            }
        }

        return hashTable;
    }

    public static void saveMeasurements(String path, final MeasureRainbowTable measureRainbowTable) {
        File file = new File(path + "\\" + new Date().getTime() + "-RainbowTable-measurements.txt");

        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("Rainbow Table Measurements:");
            printWriter.println("- Algorithm: " + measureRainbowTable.getAlgorithm());
            printWriter.println("- Charset type: " + measureRainbowTable.getCharsetType());
            printWriter.println("- Charset: " + measureRainbowTable.getCharset());
            printWriter.println("- Min password length: " + measureRainbowTable.getMinPasswordLength());
            printWriter.println("- Max password length: " + measureRainbowTable.getMaxPasswordLength());
            printWriter.println("- Chains per table: " + measureRainbowTable.getChainsPerTable());
            printWriter.println("- Chains length: " + measureRainbowTable.getChainLength());
            printWriter.println("- Generation time (in seconds): " + measureRainbowTable.getElapsedTimeInSeconds() + " s");
            printWriter.println("                  (in minutes): " + measureRainbowTable.getElapsedTimeInMinutes() + " min");
            printWriter.println("                  (in minutes and seconds): " + measureRainbowTable.getElapsedTimeInMinutesAndSeconds());
            printWriter.println("- Table size (in MB): " + measureRainbowTable.getRainbowTableSizeInMB() + " MB");
            printWriter.println("             (in GB): " + measureRainbowTable.getRainbowTableSizeInGB() + " GB");
            printWriter.close();
        } catch (Exception e) {
            DialogController.SHOW_ALERT("Oops. Something went wrong.", Alert.AlertType.WARNING);
        }
    }
}
