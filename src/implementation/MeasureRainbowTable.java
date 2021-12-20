package implementation;

import java.io.File;

public class MeasureRainbowTable {
    private long startTime;
    private long endTime;
    private double elapsedTimeInSeconds;
    private double rainbowTableSizeInMB;

    private final String algorithm;
    private final String charsetType;
    private final String charset;
    private final int minPasswordLength;
    private final int maxPasswordLength;
    private final int chainsPerTable;
    private final int chainLength;

    public MeasureRainbowTable(final String algorithm, final String charsetType, final String charset, final int minPasswordLength, final int maxPasswordLength, final int chainsPerTable, final int chainLength) {
        this.algorithm = algorithm;
        this.charsetType = charsetType;
        this.charset = charset;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainsPerTable = chainsPerTable;
        this.chainLength = chainLength;
    }

    public void setStartTime() {
        this.startTime = System.nanoTime();
    }

    public void setEndTime() {
        this.endTime = System.nanoTime();
    }

    public void setElapsedTimeInSeconds() {
        if (this.startTime == 0 && this.endTime == 0) {
            return;
        }

        this.elapsedTimeInSeconds = this.round((double) (this.endTime - this.startTime) / 1000000000, 2);
    }

    public double getElapsedTimeInSeconds() {
        return this.elapsedTimeInSeconds;
    }

    public double getElapsedTimeInMinutes() {
        return this.round(this.elapsedTimeInSeconds / 60, 2);
    }

    public String getElapsedTimeInMinutesAndSeconds() {
        double minutesInDouble = this.elapsedTimeInSeconds / 60;
        long minutes = (long) (this.elapsedTimeInSeconds / 60);
        double decimalPart = minutesInDouble - minutes;
        long seconds = (long) (decimalPart * 60);
        return minutes + " min " + seconds + " s";
    }

    public double getRainbowTableSizeInMB() {
        return this.rainbowTableSizeInMB;
    }

    public double getRainbowTableSizeInGB() {
        return this.round(this.rainbowTableSizeInMB / 1024, 3);
    }

    public void setRainbowTableSizeInMB(final File file) {
        long fileSizeInBytes = file.length();
        double fileSizeInKB = (double) fileSizeInBytes / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;
        this.rainbowTableSizeInMB = this.round(fileSizeInMB, 3);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);

        return (double) tmp / factor;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getCharsetType() {
        return charsetType;
    }

    public String getCharset() {
        return charset;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public int getMaxPasswordLength() {
        return maxPasswordLength;
    }

    public int getChainsPerTable() {
        return chainsPerTable;
    }

    public int getChainLength() {
        return chainLength;
    }
}
