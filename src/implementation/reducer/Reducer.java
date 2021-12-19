package implementation.reducer;

import utils.CharsetUtils;

public class Reducer {

    private final String charset;
    private final int maxPasswordLength;
    private final int minPasswordLength;

    public Reducer(final String charsetType, final int minPasswordLength, final int maxPasswordLength) {
        this.charset = CharsetUtils.getCharsetByType(charsetType);
        this.maxPasswordLength = maxPasswordLength;
        this.minPasswordLength = minPasswordLength;
    }

    public byte[] reduce(final byte[] hash, final int index) {
        // Plaintext length from a byte from the hash.
        byte passwordLength = (byte) ((index) % (this.maxPasswordLength - this.minPasswordLength + 1) + this.minPasswordLength);
        return this.reduce(hash, index, passwordLength);
    }

    public byte[] reduce(final byte[] hash, final int index, final byte passwordLength) {
        byte[] result = new byte[passwordLength];

        for (int i = 0; i < passwordLength; i++) {
            // Unique results for each reduction function.
            hash[i] ^= index;

            result[i] = (byte) (Math.abs(hash[i]) % this.charset.length());

            // Cancel XOR operation.
            hash[i] ^= index;
        }

        return result;
    }
}
