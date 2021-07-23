package au.edu.rmit.blockchain.crypto.pixr.algorithms.results;

import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;

public class PIXRDistinctVectorFinderResult implements AlgorithmResult {
    private final long txSize;
    private final int hashLength;
    private final int length;
    private final int position;
    private final int step;
    private final ImmutableList<String> distinctList;

    public PIXRDistinctVectorFinderResult(long txSize, int hashLength, int length, int position, int step, ImmutableList<String> hashList) {
        this.txSize = txSize;
        this.hashLength = hashLength;
        this.length = length;
        this.position = position;
        this.step = step;
        this.distinctList = hashList;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public int getLength() {
        return length;
    }

    public int getPosition() {
        return position;
    }

    public ImmutableList<String> getDistinctList() {
        return distinctList;
    }

    public long getTxSize() {
        return txSize;
    }

    public int getHashLength() {
        return hashLength;
    }

    public int getStep() {
        return step;
    }

    @Override
    public long measureResultSize() {
        // number of bits required to represent hash length
        long representedBits = (long) (Math.log(hashLength) / Math.log(2));
        // lengthCost + positionCost + (bit_per_vector * number of transaction)
        return (representedBits * 2) + (length * txSize);
    }
}
