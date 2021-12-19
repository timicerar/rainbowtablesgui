package utils;

import implementation.hash.Bytes;
import implementation.hash.HashTable;

import java.io.*;

public class FileUtils {
    public static BufferedOutputStream getBufferedOutputStream(final File file) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public static BufferedInputStream getBufferedInputStream(final File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public static HashTable readFile(BufferedInputStream inputStream, final int chainsPerTable, final int minPasswordLength, final int maxPasswordLength) throws IOException {
        // Reads a previously opened table file into a hashmap.
        HashTable hashTable = new HashTable(chainsPerTable / 5, minPasswordLength, maxPasswordLength);
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
}
