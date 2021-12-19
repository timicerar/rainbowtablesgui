package implementation.hash;

import java.util.Iterator;

public class HashIterator implements Iterator<Bytes> {
    private int position;
    private final int size;
    private int iterations;
    private Bytes currentBytes;
    private final Bytes[] bytesTable;

    public HashIterator(final Bytes[] bytesTable, final int size) {
        this.bytesTable = bytesTable;
        this.size = size;
        this.position = -1;
    }

    @Override
    public boolean hasNext() {
        return this.iterations < this.size;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public Bytes next() {
        if (this.currentBytes == null || this.currentBytes.next == null) {
            while (this.bytesTable[++position] == null);
            this.currentBytes = this.bytesTable[position];
        } else {
            this.currentBytes = this.currentBytes.next;
        }

        iterations++;

        return currentBytes;
    }
}
