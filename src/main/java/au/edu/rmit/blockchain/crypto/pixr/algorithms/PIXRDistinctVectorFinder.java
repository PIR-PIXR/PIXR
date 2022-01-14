package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;

import java.util.ArrayList;
import java.util.List;

public class PIXRDistinctVectorFinder {
    private final PIXRDistinctVectorStrategy strategy;
    private final List<String> txBinaryStringList;
    private final int hashLength;

    public PIXRDistinctVectorFinder(PIXRDistinctVectorStrategy strategy, int hashLength) {
        this.strategy = strategy;
        this.hashLength = hashLength;
        txBinaryStringList = new ArrayList<>();
    }

    public PIXRDistinctVectorFinder(PIXRDistinctVectorStrategy strategy) {
        this.strategy = strategy;
        this.hashLength = Setting.HASH_CODE_LENGTH;
        txBinaryStringList = new ArrayList<>();
    }

    /**
     * put hashcode into check list
     *
     * @param hash hashcode
     */
    public void put(String hash) throws NotMatchException {
        if (hash.length() != hashLength)
            throw new NotMatchException("Length does not match");
        txBinaryStringList.add(hash);
    }

    /**
     * find distinct vectors from check list
     *
     * @return distinct vectors and setting info
     */
    public PIXRDistinctVectorFinderResult find() throws NotMatchException {
        return strategy.run(txBinaryStringList, hashLength);
    }


}
