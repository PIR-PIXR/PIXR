package au.edu.rmit.blockchain.crypto.pixr.algorithms.results;

public interface AlgorithmResult {
    /**
     * Measure size of result in bit that used as communication cost
     *
     * @return bit-size
     */
    long measureResultSize();
}
