package implementation.hash;

public class HashFunction {

    private final int tableSize;

    public HashFunction(final int tableSize) {
        this.tableSize = tableSize;
    }

    public int hash(final Bytes b) {
        int hash = 0;
        byte[] bytes = b.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            hash += Math.pow(bytes[i], i + 1);
        }

        return hash % this.tableSize;
    }
}
