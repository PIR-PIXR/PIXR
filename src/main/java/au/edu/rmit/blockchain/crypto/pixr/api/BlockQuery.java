package au.edu.rmit.blockchain.crypto.pixr.api;

import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;

import java.io.IOException;

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
    public Block nextBlockWithCondition() throws APIException, IOException, InterruptedException {
        Block block = api.getBlock(currentHashCode);
        String nextBlockHash = null;
        while (!block.getNextBlockHashes().isEmpty() && !checkCondition(block.getSize())) {
            nextBlockHash = block.getNextBlockHashes().get(0);
            if (nextBlockHash.equals(currentHashCode))
                return null;
            block = api.getBlock(nextBlockHash);
            System.out.println(block.getSize());
            Thread.sleep(5000);
        }
        if (nextBlockHash != null)
            currentHashCode = nextBlockHash;
        return block;
    }


    /**
     * check if size satisfy the condition that 2^10 <= size and in the form of 2^n
     *
     * @param size number of tx
     * @return true|false
     */
    private boolean checkCondition(long size) {
        double logBase2 = Math.log(size) / Math.log(2);
        int floorValue = (int) logBase2;
        return logBase2 >= 10 && logBase2 == floorValue;
    }

    /**
     * restart steps to the start hash code
     */
    public void restart() {
        currentHashCode = startHashCode;
    }


}
