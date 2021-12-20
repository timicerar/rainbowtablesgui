package implementation;

import implementation.reducer.Reducer;
import javafx.scene.control.ProgressBar;
import utils.CharsetUtils;
import utils.CommonUtils;
import utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TableGenerator {
    public static final String MD5_ALGORITHM = "MD5";
    public static final String SHA1_ALGORITHM = "SHA-1";

    private final String algorithm;
    private final String charset;
    private final int minPasswordLength;
    private final int maxPasswordLength;
    private final int chainsPerTable;
    private final int chainLength;

    private final Random random;
    private final Reducer reducer;
    private final MessageDigest messageDigest;

    private final MeasureRainbowTable measureRainbowTable;

    public TableGenerator(final String algorithm, final String charsetType, final int minPasswordLength, final int maxPasswordLength, final int chainsPerTable, final int chainLength) throws NoSuchAlgorithmException {
        this.algorithm = algorithm;
        this.charset = CharsetUtils.getCharsetByType(charsetType);
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainsPerTable = chainsPerTable;
        this.chainLength = chainLength;

        this.random = new Random(System.currentTimeMillis());
        this.reducer = new Reducer(charsetType, minPasswordLength, maxPasswordLength);
        this.messageDigest = CommonUtils.getMessageDigest(algorithm);

        this.measureRainbowTable = new MeasureRainbowTable(algorithm, charsetType, this.charset, minPasswordLength, maxPasswordLength, chainsPerTable, chainLength);
    }

    public String generateRainbowTable(String savePath, ProgressBar progressBar) throws IOException {
        // Generates chains for the rainbow table, and saves the starting and endpoints in a file.
        this.measureRainbowTable.setStartTime();

        File file = new File(savePath + "\\" + this.algorithm.replace("-", "") + "-RainbowTable-" + this.charset.length() + "-" + this.minPasswordLength + "-" + this.maxPasswordLength + "-" + this.chainsPerTable + "-" + this.chainLength + ".tbl");
        BufferedOutputStream outputStream = FileUtils.getBufferedOutputStream(file);
        int keyspaceId = 0;
        int[] keyspaceRatio = CommonUtils.calculateKeyspaceRatios(this.charset, this.minPasswordLength, this.maxPasswordLength, this.chainsPerTable);

        for (int i = 0; i < chainsPerTable; i++) {
            byte passwordLength = (byte) (keyspaceId + this.minPasswordLength);

            byte[] startingPoint = this.createRandomStartingPoint(random, passwordLength);
            byte[] endPoint = this.calculateChain(startingPoint, passwordLength);

            // Change keyspace when keyspace size is exceeded.
            if (i > keyspaceRatio[keyspaceId] && keyspaceId < keyspaceRatio.length - 1) {
                keyspaceId++;
            }

            outputStream.write(startingPoint);
            outputStream.write(endPoint);

            // Progress Bar - Java FX
            if (i != 0 && i % (chainsPerTable / 20) == 0) {
                progressBar.setProgress((double) i / (double) chainsPerTable);
            }
        }

        outputStream.close();

        progressBar.setProgress(1.0);

        this.measureRainbowTable.setEndTime();
        this.measureRainbowTable.setElapsedTimeInSeconds();
        this.measureRainbowTable.setRainbowTableSizeInMB(file);

        return this.tableGenerationDone(file);
    }

    private byte[] createRandomStartingPoint(final Random random, final byte passwordLength) {
        // Creates a random "password".
        byte[] startingPoint = new byte[passwordLength];
        random.nextBytes(startingPoint);

        for (int a = 0; a < startingPoint.length; a++) {
            startingPoint[a] = (byte) (Math.abs(startingPoint[a] % this.charset.length()));
        }

        return startingPoint;
    }

    private byte[] calculateChain(byte[] currentEndpoint, final byte passwordLength) {
        // Loops each column with different reducing function to produce an endpoint.
        byte[] hash;

        for (int i = 0; i < chainLength; i++) {
            hash = this.messageDigest.digest(currentEndpoint);
            currentEndpoint = this.reducer.reduce(hash, i, passwordLength);
        }

        return currentEndpoint;
    }

    private String tableGenerationDone(final File file) {
        return "Rainbow Table " + file.getName() + " was successfully generated.";
    }

    public MeasureRainbowTable getMeasureRainbowTable() {
        return measureRainbowTable;
    }
}
