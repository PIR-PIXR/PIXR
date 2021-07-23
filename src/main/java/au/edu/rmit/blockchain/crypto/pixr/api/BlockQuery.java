package au.edu.rmit.blockchain.crypto.pixr.api;

import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.utils.Setting;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;

import java.io.IOException;
import java.io.Serial;

public class BlockQuery {
    private final String startHashCode;
    private String currentHashCode;
    private final BlockChainApi api = new BlockChainApi();

    public BlockQuery(String startHashCode) {
        this.startHashCode = startHashCode;
        currentHashCode = startHashCode;
    }

    /**
     * Get block continuously from the start block
     *
     * @return next block
     */
    public Block nextBlock() throws APIException, IOException {
        Block block = api.getBlock(currentHashCode);
        String nextBlockHash = block.getNextBlockHashes().get(0);
        if (nextBlockHash.equals(currentHashCode))
            return null;
        currentHashCode = nextBlockHash;
        return block;
    }

    /**
     * Get block continuously from the start block with condition
     *
     * @return next block
     */
    public Block nextBlockWithCondition() throws APIException, IOException {
        Block block;
        String nextBlockHash = currentHashCode;
        do {
            block = api.getBlock(nextBlockHash);
            nextBlockHash = block.getNextBlockHashes().get(0);
        } while (!block.getNextBlockHashes().isEmpty() && !checkCondition(block.getTransactions().size()));
        if (!nextBlockHash.equals(currentHashCode))
            currentHashCode = nextBlockHash;
        return block;
    }


    /**
     * Check if size satisfy the condition that 2^10 <= size
     *
     * @param size number of tx
     * @return true|false
     */
    private boolean checkCondition(long size) {
        return size >= Setting.LOWER_BOUND_OF_NUM_TRANSACTION;
    }

    /**
     * restart steps to the start hash code
     */
    public void restart() {
        currentHashCode = startHashCode;
    }


}
