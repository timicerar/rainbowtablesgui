package implementation;

import implementation.hash.Bytes;
import implementation.hash.HashTable;
import implementation.reducer.Reducer;
import utils.CharsetUtils;
import utils.CommonUtils;
import utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCracker {
    public static final String MD5_ALGORITHM = "MD5";
    public static final String SHA1_ALGORITHM = "SHA-1";

    private final String charset;
    private final int minPasswordLength;
    private final int maxPasswordLength;
    private final int chainLength;

    private final Reducer reducer;
    private final HashTable hashTable;
    private final MessageDigest messageDigest;

    private String plainText;

    public PasswordCracker(final String algorithm, final File file, final String charsetType, final int minPasswordLength, final int maxPasswordLength, final int chainsPerTable, final int chainLength) throws NoSuchAlgorithmException, IOException {
        this.charset = CharsetUtils.getCharsetByType(charsetType);
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainLength = chainLength;

        this.reducer = new Reducer(charsetType, minPasswordLength, maxPasswordLength);
        this.messageDigest = CommonUtils.getMessageDigest(algorithm);

        BufferedInputStream inputStream = FileUtils.getBufferedInputStream(file);
        this.hashTable = FileUtils.readFile(inputStream, chainsPerTable, minPasswordLength, maxPasswordLength);
    }

    public boolean crackHash(final String hash) {
        final byte[] hashBytes = CommonUtils.hexToBytes(hash);
        HashTable foundEndpoints = this.searchEndpoints(hashBytes, this.hashTable);

        return eliminateFalseAlarms(foundEndpoints, this.hashTable, hashBytes);
    }

    private HashTable searchEndpoints(byte[] hashBytes, HashTable hashTable) {
        // Loops through known password lengths, and tries to reduce the hash with different reduction function in order to find matching endpoints from the rainbow table.
        HashTable foundEndpoints = new HashTable(hashTable.size() / 11);

        byte[] reducedEndpoint = null;

        // Try every known password length.
        for (int passwordLength = this.minPasswordLength; passwordLength <= this.maxPasswordLength; passwordLength++) {
            // Calculate endpoints with all reduction functions.
            for (int i = chainLength - 1; i >= 0; i--) {
                byte[] possibleEndpoint = hashBytes;

                for (int j = i; j < chainLength; j++) {
                    reducedEndpoint = this.reducer.reduce(possibleEndpoint, j, (byte) passwordLength);
                    possibleEndpoint = this.messageDigest.digest(reducedEndpoint);
                }

                Bytes bytes = new Bytes(reducedEndpoint);

                // Add the endpoint to a hashset for further analysis.
                if (hashTable.contains(bytes)) {
                    foundEndpoints.insert(bytes);
                }
            }
        }

        return foundEndpoints;
    }

    private boolean eliminateFalseAlarms(HashTable foundEndpoints, HashTable table, byte[] hash) {
        // Eliminates false alarms, false alarms being endpoints that don't point to chains that produce the hash. This cracks the hash.
        for (Bytes endpoint : foundEndpoints) {
            Bytes currentPlaintext = table.search(endpoint);
            byte passwordLength = (byte) endpoint.getBytes().length;
            byte[] currentHash;

            for (int i = 0; i < chainLength; i++) {
                currentHash = this.messageDigest.digest(CommonUtils.bytesToString(currentPlaintext.getBytes(), charset).getBytes());

                if (CommonUtils.equalBytes(hash, currentHash)) {
                    this.plainText = CommonUtils.bytesToString(currentPlaintext.getBytes(), this.charset);
                    return true;
                }

                currentPlaintext = new Bytes(reducer.reduce(currentHash, i, passwordLength));
            }
        }

        // Hash was not found.
        return false;
    }

    public String getPlainText() {
        return plainText;
    }
}
