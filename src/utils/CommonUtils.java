package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {
    public static MessageDigest getMessageDigest(final String algorithm) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }

    public static byte[] hexToBytes(String s) {
        int length = s.length();
        byte[] data = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String bytesToString(byte[] bytes, String charset) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(charset.charAt(b % charset.length()));
        }

        return result.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (byte temp : bytes) {
            stringBuilder.append(Integer.toString((temp & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }

    public static boolean equalBytes(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length) {
            return false;
        }

        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }

        return true;
    }

    public static byte calculatePasswordLength(final int chainNr, final int minPasswordLength, final int maxPasswordLength) {
        return (byte) (chainNr % (maxPasswordLength - minPasswordLength + 1) + minPasswordLength);
    }

    public static int calculatePrime(int size) {
        int i = size / 2;

        while (true) {
            boolean isPrime = true;

            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                return i;
            }

            i++;
        }
    }

    public static long calculateKeyspace(final String charset, final int minPasswordLength, final int maxPasswordLength) {
        // Calculates the keyspace size.
        long keyspace = 0;

        for (int i = minPasswordLength; i <= maxPasswordLength; i++) {
            keyspace += (long) Math.pow(charset.length(), i);
        }

        return keyspace;
    }

    public static int[] calculateKeyspaceRatios(final String charset, final int minPasswordLength, final int maxPasswordLength, final int chainsPerTable) {
        // Calculates keyspace ratios in relation to chains per table. This is useful when creating a rainbow table.
        // If ratios aren't used, the table will have more chains for shorter passwords than is necessary.
        long keyspaceSize = CommonUtils.calculateKeyspace(charset, minPasswordLength, maxPasswordLength);
        int[] ratios = new int[maxPasswordLength - minPasswordLength + 1];

        for (int i = 0; i <= maxPasswordLength - minPasswordLength; i++) {
            ratios[i] = (int) (chainsPerTable / (keyspaceSize / CommonUtils.calculateKeyspace(charset, i, i)));
        }

        return ratios;
    }
}
