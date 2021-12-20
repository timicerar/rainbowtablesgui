package implementation.hash;

import utils.CommonUtils;

import java.util.Iterator;

public class HashTable implements Iterable<Bytes> {

    private final Bytes[] bytesTable;
    private final HashFunction hashFunction;
    private int size;

    public HashTable(final int size) {
        // Calculate the size for the table.
        int prime = CommonUtils.calculatePrime(size);
        this.bytesTable = new Bytes[prime];
        this.hashFunction = new HashFunction(prime);
    }

    public int size() {
        return this.size;
    }

    public Bytes[] getBytes() {
        return this.bytesTable;
    }

    @Override
    public Iterator<Bytes> iterator() {
        return new HashIterator(this.bytesTable, this.size);
    }

    public void insert(final Bytes key, final Bytes value) {
        // Insert operation for keys with values.
        key.value = value;
        this.insert(key);
    }

    public void insert(final Bytes key) {
        // Insert a byte array to the table. If the table index is taken, add it to a linked list.
        int index = this.hashFunction.hash(key);

        if (this.bytesTable[index] == null) {
            this.bytesTable[index] = key;
        } else {
            Bytes currentBytes = this.bytesTable[index];

            if (currentBytes.equals(key)) {
                return;
            }

            while (currentBytes.next != null) {
                if (currentBytes.equals(key)) {
                    return;
                }

                currentBytes = currentBytes.next;
            }

            currentBytes.next = key;
        }

        this.size++;
    }

    public boolean contains(Bytes key) {
        // Check if a byte array is a key in the table.
        Bytes found = this.findKey(key);
        return found != null;
    }

    public Bytes search(final Bytes key) {
        // Search a byte array from the table.
        Bytes found = this.findKey(key);

        if (found == null) {
            return null;
        }

        return found.value;
    }

    private Bytes findKey(final Bytes key) {
        int index = this.hashFunction.hash(key);
        Bytes otherBytes = this.bytesTable[index];

        while (otherBytes != null && !key.equals(otherBytes)) {
            if (otherBytes.next != null) {
                otherBytes = otherBytes.next;
            } else {
                break;
            }
        }

        return otherBytes;
    }
}
