package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;

import java.util.ArrayList;
import java.util.List;

public class PIXRDistinctVectorFinder {
    private final PIXRDistinctVectorStrategy strategy;
    private final List<String> txBinaryStringList;
    private final int length;
    private int step = 1;

    public PIXRDistinctVectorFinder(PIXRDistinctVectorStrategy strategy, int length) {
        this.strategy = strategy;
        this.length = length;
        txBinaryStringList = new ArrayList<>();
    }

    public PIXRDistinctVectorFinder(PIXRDistinctVectorStrategy strategy, int length, int step) {
        this.strategy = strategy;
        this.length = length;
        this.step = step;
        txBinaryStringList = new ArrayList<>();
    }

    /**
     * put hashcode into check list
     *
     * @param hash hashcode
     */
    public void put(String hash) throws NotMatchException {
        if (hash.length() != length)
            throw new NotMatchException("Length does not match");
        txBinaryStringList.add(hash);
    }

    /**
     * find distinct vectors from check list
     *
     * @return distinct vectors and setting info
     */
    public PIXRDistinctVectorFinderResult find() throws NotMatchException {
        return strategy.run(txBinaryStringList, length, step);
    }


}
