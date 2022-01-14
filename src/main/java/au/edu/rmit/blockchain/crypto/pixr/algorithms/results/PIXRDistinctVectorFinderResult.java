package au.edu.rmit.blockchain.crypto.pixr.algorithms.results;

import au.edu.rmit.blockchain.crypto.common.utils.Util;
import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;

import java.util.List;

public class PIXRDistinctVectorFinderResult implements AlgorithmResult {
    private final long txSize;
    private final int hashLength;
    private final List<Integer> positions;
    private final ImmutableList<String> vectors;
    private long encodeTime;
    private long decodeTime;
    private int initialStep;

    public PIXRDistinctVectorFinderResult(long txSize, int hashLength, List<Integer> positions, ImmutableList<String> vectors) {
        this.txSize = txSize;
        this.hashLength = hashLength;
        this.positions = positions;
        this.vectors = vectors;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    @Override
    public long measureResultSize() {
        // number of position * positionCost + (bit_per_vector * number of transaction)
        return (positions.size() * Util.log2(hashLength)) + (vectors.get(0).length() * txSize);
    }

    public ImmutableList<String> getVectors() {
        return vectors;
    }

    public long getTxSize() {
        return txSize;
    }

    public int getHashLength() {
        return hashLength;
    }

    public List<Integer> getPositions() {
        return positions;
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

    public int getInitialStep() {
        return initialStep;
    }

    public void setInitialStep(int initialStep) {
        this.initialStep = initialStep;
    }

}
