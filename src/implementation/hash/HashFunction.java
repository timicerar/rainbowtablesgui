package implementation.hash;

public class HashFunction {

    private final int firstBytes;
    private final int tableSize;

    public HashFunction(final int tableSize, final int minPasswordLength, final int maxPasswordLength) {
        this.firstBytes = minPasswordLength + (maxPasswordLength - minPasswordLength) / 2;
        this.tableSize = tableSize;
    }

    public int hash(final Bytes b) {
        int hash = 0;
        byte[] bytes = b.getBytes();

        for (int i = 1; i < this.firstBytes; i++) {
            hash += Math.pow(bytes[i - 1], i);
        }

        return hash % this.tableSize;
    }
}
