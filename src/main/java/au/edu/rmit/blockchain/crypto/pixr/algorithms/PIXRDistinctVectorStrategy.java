package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;

import java.util.List;

public interface PIXRDistinctVectorStrategy {
    /**
     * exec specific algorithm to find distinct vector in given tx list
     *
     * @param txBinaryList tx list in binary string
     * @param length       binary length
     * @return distinct vectors and setting info
     */
    PIXRDistinctVectorFinderResult run(List<String> txBinaryList, int length, int step) throws NotMatchException;
}
