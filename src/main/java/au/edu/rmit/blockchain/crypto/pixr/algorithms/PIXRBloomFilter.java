package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRBloomFilterResult;
import au.edu.rmit.blockchain.crypto.pixr.dto.Transaction;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@SuppressWarnings("UnstableApiUsage")
public class PIXRBloomFilter {
    private final long txSize;
    private final double fpRate;
    private BloomFilter<String> bloomFilter;

    public PIXRBloomFilter(long txSize) {
        this(txSize, 0.03);
    }

    public PIXRBloomFilter(long txSize, double fpRate) {
        this.txSize = txSize;
        this.fpRate = fpRate;
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), txSize, fpRate);
    }

    /**
     * check whether tx_idx exist
     *
     * @param tx    transaction
     * @param index index of tx in database
     * @return true|false
     */
    public boolean checkMemberShip(Transaction tx, int index) {
        return checkMemberShip(tx.getHash(), index);
    }

    /**
     * check whether tx_idx exist
     *
     * @param hash  transaction hash
     * @param index index of tx in database
     * @return true|false
     */
    public boolean checkMemberShip(String hash, int index) {
        StringBuilder builder = new StringBuilder(hash);
        return bloomFilter.mightContain(builder.append(index).toString());
    }

    /**
     * put tx_idx into a filter
     *
     * @param tx    transaction
     * @param index index of transaction in database
     */
    public void put(Transaction tx, int index) {
        put(tx.getHash(), index);
    }


    /**
     * put tx_idx into a filter
     *
     * @param hash  transaction hashcode
     * @param index index of transaction in database
     */
    public void put(String hash, int index) {
        StringBuilder builder = new StringBuilder(hash);
        bloomFilter.put(builder.append(index).toString());
    }

    /**
     * Import filter from binary array
     *
     * @param bytes binary array
     */
    public void readFromBinary(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        bloomFilter = BloomFilter.readFrom(inputStream, Funnels.stringFunnel(Charset.defaultCharset()));
    }

    /**
     * Export filter to binary arrays
     *
     * @return binary array
     */
    public byte[] writeToByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] result;
        try {
            bloomFilter.writeTo(outputStream);
            result = outputStream.toByteArray();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
        return result;
    }

    /**
     * Export filter to binary string
     *
     * @return binary string
     */
    public String writeToBinaryString() throws IOException {
        byte[] bytes = writeToByteArray();
        StringBuilder binaryStringBuilder = new StringBuilder();
        if (bytes != null) {
            for (byte b : bytes) {
                binaryStringBuilder.append(String.format("%4s",
                        Integer.toBinaryString(b & 0xFF).replaceAll(" ", "0")));
            }
        }
        return binaryStringBuilder.toString();
    }

    public PIXRBloomFilterResult getResult() throws IOException {
        return new PIXRBloomFilterResult(txSize, fpRate, writeToByteArray(), writeToBinaryString());
    }


    public long getTxSize() {
        return txSize;
    }

    public double getFpRate() {
        return fpRate;
    }

}
