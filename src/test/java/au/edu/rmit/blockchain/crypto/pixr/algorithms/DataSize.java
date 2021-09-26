package au.edu.rmit.blockchain.crypto.pixr.algorithms;

public enum DataSize {
    ONE_THOUSAND(1_000),
    TWO_THOUSAND(2_000),
    FIVE_THOUSAND(5_000),
    TEN_THOUSAND(10_000),
    FIFTY_THOUSAND(50_000),
    ONE_HUNDRED_THOUSAND(100_000),
    FIVE_HUNDRED_THOUSAND(500_000),
    ONE_MILLION(1_000_000);


    private final int value;

    DataSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
