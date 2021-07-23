package au.edu.rmit.blockchain.crypto.pixr.algorithms.results;

import com.google.gson.GsonBuilder;

import java.util.Arrays;

public class PIXRBloomFilterResult implements AlgorithmResult {
    private final long txSize;
    private final double fpRate;
    private final byte[] filter;
    private final String filterInBinString;

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
        return filterInBinString.length();
    }
}
