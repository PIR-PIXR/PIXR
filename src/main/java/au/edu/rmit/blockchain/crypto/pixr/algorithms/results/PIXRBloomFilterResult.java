package au.edu.rmit.blockchain.crypto.pixr.algorithms.results;

import au.edu.rmit.blockchain.crypto.common.utils.Util;
import com.google.gson.GsonBuilder;

public class PIXRBloomFilterResult implements AlgorithmResult {
    private final long txSize;
    private final double fpRate;
    private final byte[] filter;
    private final String filterInBinString;
    private long encodeTime;
    private long decodeTime;
    private int foundPositions;
    private boolean isCorrect;

    public PIXRBloomFilterResult(long txSize, double fpRate, byte[] filter, String filterInBinString) {
        this.txSize = txSize;
        this.fpRate = fpRate;
        this.filter = filter;
        this.filterInBinString = filterInBinString;
    }

    public byte[] getFilter() {
        return filter;
    }

    public String getFilterInBinString() {
        return filterInBinString;
    }

    public long getTxSize() {
        return txSize;
    }

    public double getFpRate() {
        return fpRate;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    @Override
    public long measureResultSize() {
        long txSizeRequiredBit = Util.log2(txSize);
        return filterInBinString.length() + txSizeRequiredBit;
    }

    public long getEncodeTime() {
        return encodeTime;
    }

    public void setEncodeTime(long encodeTime) {
        this.encodeTime = encodeTime;
    }

    public long getDecodeTime() {
        return decodeTime;
    }

    public void setDecodeTime(long decodeTime) {
        this.decodeTime = decodeTime;
    }

    public int getFoundPositions() {
        return foundPositions;
    }

    public void setFoundPositions(int foundPositions) {
        this.foundPositions = foundPositions;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
