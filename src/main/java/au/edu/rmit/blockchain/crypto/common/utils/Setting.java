package au.edu.rmit.blockchain.crypto.common.utils;

public class Setting {
    public static final String DATA_HOME = "/Users/duyhuynh/data/";
    public static final String BLOCK_FILE = "block.json";
    public static final String TRANSACTION_FILE = "transaction.json";
    public static final String START_BLOCK = "00000000000000000002a23d6df20eecec15b21d32c75833cce28f113de888b7";
    public static final int NUM_BLOCK_TO_DOWNLOAD = 20;
    public static final long LOWER_BOUND_OF_NUM_TRANSACTION = (long) Math.pow(2, 10);
    public static final int HASH_CODE_LENGTH = 256;
}
